package com.training.socialnetwork.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.request.post.PostCreateDto;
import com.training.socialnetwork.dto.request.post.PostUpdateDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;
import com.training.socialnetwork.dto.response.post.PostCreatedDto;
import com.training.socialnetwork.dto.response.post.PostDetailDto;
import com.training.socialnetwork.dto.response.post.PostListDto;
import com.training.socialnetwork.dto.response.post.PostUpdatedDto;
import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

@Service
@Transactional
public class PostService implements IPostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private PhotoRepository photoRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostCreatedDto createPost(int userId, PostCreateDto postCreateDto) throws Exception {
		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}
		Post post = new Post();
		post.setUser(user);
		post.setContent(postCreateDto.getContent());
		post.setCreateDate(new Date());
		post.setUpdateDate(new Date());

		post = postRepository.save(post);
		if (post == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		List<Post> postList = new ArrayList<>();
		postList.add(post);
		
		if(null != postCreateDto.getPhotoIdList()) {
			for (int photoId : postCreateDto.getPhotoIdList()) {
				Photo photo = photoRepository.findById(photoId).orElse(null);
				
				if(photo == null) {
					throw new CustomException(HttpStatus.NOT_FOUND, "Photo does not exist");
				}
				if(photo.getUser().getUserId() != userId) {
					throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to use this photo");
				}
				photo.getPostList().add(post);
				photoRepository.save(photo);
			}
		}

		PostCreatedDto postCreatedDto = modelMapper.map(post, PostCreatedDto.class);
		postCreatedDto.setUsername(user.getUsername());
		return postCreatedDto;
	}

	@Override
	public Page<PostListDto> getTimeline(int userId, Pageable page) {
		List<Integer> friendUserIdList = friendRepository.findAllFriendUserId(userId);
		friendUserIdList.add(userId);
		Page<Post> postList = postRepository.findAllByUserId(friendUserIdList, page);
		List<PostListDto> postListDtos = new ArrayList<>();
		for (Post post : postList) {
			List<Photo> photoList = post.getPhotoList();
			List<String> photoUrlList = new ArrayList<>();
			photoList.stream().map(photo -> photoUrlList.add(photo.getName())).collect(Collectors.toList());
			PostListDto postListDto = modelMapper.map(post, PostListDto.class);
			postListDto.setPhotoUrl(photoUrlList);
			List<Like> likeList = new ArrayList<>();
			for (Like like : post.getLikeList()) {
				if (like.getDeleteFlg() != Constant.DELETED_FlG) {
					likeList.add(like);
				}
			}
			postListDto.setLikeCount(likeList.size());
			List<Comment> commentList = new ArrayList<>();
			for (Comment comment : post.getCommentList()) {
				if (comment.getDeleteFlg() != Constant.DELETED_FlG) {
					commentList.add(comment);
				}
			}
			postListDto.setCommentCount(commentList.size());
			postListDto.setUsername(post.getUser().getUsername());
			postListDtos.add(postListDto);
		}
		Page<PostListDto> result = new PageImpl<PostListDto>(postListDtos);
		
		return result;
	}

	@Override
	public PostDetailDto getPost(int postId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);

		if (post == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}

		PostDetailDto postDetailDto = modelMapper.map(post, PostDetailDto.class);
		if (!post.getCommentList().isEmpty()) {
			List<Comment> commentList = post.getCommentList();
			List<CommentDetailDto> comDetailDtoList = new ArrayList<>();
			for (Comment comment : commentList) {
				CommentDetailDto commentDetailDto = modelMapper.map(comment, CommentDetailDto.class);
				commentDetailDto.setUsername(comment.getUser().getUsername());
				comDetailDtoList.add(commentDetailDto);
			}
			postDetailDto.setCommentList(comDetailDtoList);
		}
		
		List<String> photoUrls = new ArrayList<>();
		for (Photo photo : post.getPhotoList()) {
			photoUrls.add(photo.getName());
		}

		List<Like> likeList = new ArrayList<>();
		for (Like like : post.getLikeList()) {
			if (like.getDeleteFlg() != Constant.DELETED_FlG) {
				likeList.add(like);
			}
		}
		postDetailDto.setUsername(post.getUser().getUsername());
		postDetailDto.setLikeCount(likeList.size());
		postDetailDto.setPhotoUrl(photoUrls);

		return postDetailDto;
	}

	@Override
	public PostUpdatedDto updatePost(PostUpdateDto postUpdateDto, int postId, int userId) throws Exception {
		User user = userRepository.findById(userId).orElse(null);
		Post postToUpdate = postRepository.findById(postId).orElse(null);

		if (postToUpdate == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}

		if (user.getUserId() != postToUpdate.getUser().getUserId()) {
			throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to update");
		}
		
		if(null != postToUpdate.getContent()) {
			postToUpdate.setContent(postToUpdate.getContent());
		}
		
		List<String> photoUrls = new ArrayList<>();
		if(null != postUpdateDto.getPhotoIdList()) {
			for (int photoId : postUpdateDto.getPhotoIdList()) {
				Photo photo = photoRepository.findById(photoId).orElse(null);
				
				if(photo == null) {
					throw new CustomException(HttpStatus.NOT_FOUND, "Photo does not exist");
				}
				if(photo.getUser().getUserId() != userId) {
					throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to use this photo");
				}
				photo.getPostList().add(postToUpdate);
				photoRepository.save(photo);
			}
		}
		postToUpdate.setUpdateDate(new Date());
		postToUpdate = postRepository.save(postToUpdate);

		PostUpdatedDto postUpdatedDto = modelMapper.map(postToUpdate, PostUpdatedDto.class);
		for (Photo photo : postToUpdate.getPhotoList()) {
			photoUrls.add(photo.getName());
		}
		postUpdatedDto.setPhotoUrls(photoUrls);
		postUpdatedDto.setUsername(postToUpdate.getUser().getUsername());

		return postUpdatedDto;
	}

	@Override
	public boolean deletePost(int postId, int userId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);

		if (post == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}

		if (post.getUser().getUserId() != userId) {
			throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to update");
		}
		post.setDeleteFlg(Constant.DELETED_FlG);
		post.setUpdateDate(new Date());

		return postRepository.save(post) != null;
	}

}
