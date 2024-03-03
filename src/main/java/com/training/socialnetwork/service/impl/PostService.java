package com.training.socialnetwork.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.request.post.PostCreateRequestDto;
import com.training.socialnetwork.dto.request.post.PostUpdateRequestDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailResponseDto;
import com.training.socialnetwork.dto.response.post.PostCreateResponseDto;
import com.training.socialnetwork.dto.response.post.PostDetailResponseDto;
import com.training.socialnetwork.dto.response.post.PostListResponseDto;
import com.training.socialnetwork.dto.response.post.PostUpdateResponseDto;
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
	public PostCreateResponseDto createPost(int userId, PostCreateRequestDto postCreateDto) throws Exception {
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
		
		List<Integer> photoIdList = new ArrayList<>();
		List<Photo> photoList = new ArrayList<>();
		if(null != postCreateDto.getPhotoIdList()) {
			for (int photoId : postCreateDto.getPhotoIdList()) {
				Photo photo = photoRepository.findById(photoId).orElse(null);
				
				if(photo == null || photo.getDeleteFlg() == Constant.DELETED_FlG) {
					throw new CustomException(HttpStatus.NOT_FOUND, "Photo does not exist");
				}
				if(photo.getUser().getUserId() != userId) {
					throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to use this photo");
				}
				photo.getPostList().add(post);
				photoRepository.save(photo);
				photoList.add(photo);
			}
			post.setPhotoList(photoList);
			postRepository.save(post);
		}

		PostCreateResponseDto postCreateResponseDto = modelMapper.map(post, PostCreateResponseDto.class);
		postCreateResponseDto.setUsername(user.getUsername());
		if(null != post.getPhotoList()) {
			for (Photo photo : post.getPhotoList()) {
				photoIdList.add(photo.getPhotoId());
			}
		}
		
		postCreateResponseDto.setPhotoIdList(photoIdList);
		
		return postCreateResponseDto;
	}

	@Override
	public Map<String, Object> getTimeline(int userId, Pageable page) {
		List<Integer> friendUserIdList = friendRepository.findAllFriendUserId(userId);
		friendUserIdList.add(userId);
		Page<Post> postList = postRepository.findAllByUserId(friendUserIdList, page);
		List<PostListResponseDto> postListDtos = new ArrayList<>();
		for (Post post : postList) {
			List<Photo> photoList = post.getPhotoList();
			List<Integer> photoIdList = new ArrayList<>();
			photoList.stream().map(photo -> photoIdList.add(photo.getPhotoId())).collect(Collectors.toList());
			PostListResponseDto postListDto = modelMapper.map(post, PostListResponseDto.class);
			postListDto.setPhotoIdList(photoIdList);
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
		Page<PostListResponseDto> postListDtoPage = new PageImpl<PostListResponseDto>(postListDtos);
		Map<String, Object> result = new HashMap<>();
		result.put("postList", postListDtoPage.getContent());
		result.put("currentPage", postList.getNumber() + 1);
		result.put("totalItems", postList.getTotalElements());
		result.put("totalPages", postList.getTotalPages());
		
		return result;
	}

	@Override
	public PostDetailResponseDto getPost(int postId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);

		if (post == null || post.getDeleteFlg() == Constant.DELETED_FlG) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}

		PostDetailResponseDto postDetailDto = modelMapper.map(post, PostDetailResponseDto.class);
		if (!post.getCommentList().isEmpty()) {
			List<Comment> commentList = post.getCommentList();
			List<CommentDetailResponseDto> comDetailDtoList = new ArrayList<>();
			for (Comment comment : commentList) {
				CommentDetailResponseDto commentDetailDto = modelMapper.map(comment, CommentDetailResponseDto.class);
				commentDetailDto.setUsername(comment.getUser().getUsername());
				comDetailDtoList.add(commentDetailDto);
			}
			postDetailDto.setCommentList(comDetailDtoList);
		}
		
		List<Integer> photoIdList = new ArrayList<>();
		for (Photo photo : post.getPhotoList()) {
			photoIdList.add(photo.getPhotoId());
		}

		List<Like> likeList = new ArrayList<>();
		for (Like like : post.getLikeList()) {
			if (like.getDeleteFlg() != Constant.DELETED_FlG) {
				likeList.add(like);
			}
		}
		postDetailDto.setUsername(post.getUser().getUsername());
		postDetailDto.setLikeCount(likeList.size());
		postDetailDto.setPhotoIdList(photoIdList);

		return postDetailDto;
	}

	@Override
	public PostUpdateResponseDto updatePost(PostUpdateRequestDto postUpdateDto, int postId, int userId) throws Exception {
		User user = userRepository.findById(userId).orElse(null);
		Post postToUpdate = postRepository.findById(postId).orElse(null);

		if (postToUpdate == null || postToUpdate.getDeleteFlg() == Constant.DELETED_FlG) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}

		if (user.getUserId() != postToUpdate.getUser().getUserId()) {
			throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to update");
		}
		
		if(null != postToUpdate.getContent()) {
			postToUpdate.setContent(postToUpdate.getContent());
		}
		
		List<Integer> photoIdList = new ArrayList<>();
		List<Photo> photoList = new ArrayList<>();
		if(null != postUpdateDto.getPhotoIdList()) {
			for (int photoId : postUpdateDto.getPhotoIdList()) {
				Photo photo = photoRepository.findById(photoId).orElse(null);
				
				if(photo == null || photo.getDeleteFlg() == Constant.DELETED_FlG) {
					throw new CustomException(HttpStatus.NOT_FOUND, "Photo does not exist");
				}
				if(photo.getUser().getUserId() != userId) {
					throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to use this photo");
				}
				photo.getPostList().add(postToUpdate);
				photoRepository.save(photo);
				photoList.add(photo);
			}
			postToUpdate.setPhotoList(photoList);
		}
		postToUpdate.setUpdateDate(new Date());
		postToUpdate = postRepository.save(postToUpdate);

		PostUpdateResponseDto postUpdatedDto = modelMapper.map(postToUpdate, PostUpdateResponseDto.class);
		for (Photo photo : postToUpdate.getPhotoList()) {
			photoIdList.add(photo.getPhotoId());
		}
		postUpdatedDto.setPhotoIdList(photoIdList);
		postUpdatedDto.setUsername(postToUpdate.getUser().getUsername());

		return postUpdatedDto;
	}

	@Override
	public boolean deletePost(int postId, int userId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);

		if (post == null || post.getDeleteFlg() == Constant.DELETED_FlG) {
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
