package com.training.socialnetwork.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.response.post.PostCreatedDto;
import com.training.socialnetwork.dto.response.post.PostDetailDto;
import com.training.socialnetwork.dto.response.post.PostListDto;
import com.training.socialnetwork.dto.response.post.PostUpdatedDto;
import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.image.ImageUtils;

@Service
@Transactional
public class PostService implements IPostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PhotoRepository photoRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ImageUtils imageUtils;

	@Override
	public PostCreatedDto createPost(int userId, String content, MultipartFile[] photos) throws Exception {
		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		Post post = new Post();
		post.setUser(user);
		post.setContent(content);
		post.setCreateDate(new Date());
		post.setUpdateDate(new Date());

		Post postCreated = postRepository.save(post);
		if (postCreated == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		
		List<Photo> photoList = new ArrayList<>();
		List<String> photoUrls = new ArrayList<>();
		if (photos != null) {
			for (MultipartFile file : photos) {
				String photoUrl = imageUtils.saveImage(file);
				Photo photo = new Photo();
				photo.setPost(postCreated);
				photo.setName(photoUrl);
				photo.setCreateDate(new Date());
				
				photo = photoRepository.save(photo);
				if (photo == null) {
					throw new Exception(Constant.SERVER_ERROR);
				}
				photoList.add(photo);
				photoUrls.add(photoUrl);
			}
			postCreated.setListPhoto(photoList);
		}
		
		PostCreatedDto postCreatedDto = modelMapper.map(postCreated, PostCreatedDto.class);
		postCreatedDto.setPhotoUrl(photoUrls);
		postCreatedDto.setUsername(user.getUsername());
		return postCreatedDto;
	}

	@Override
	public List<PostListDto> getTimeline(int userId, Pageable page) {
		List<Post> postList = postRepository.findAllByUserId(userId, page);
		List<PostListDto> postListDtos = new ArrayList<>();
		for (Post post : postList) {
			List<Photo> photoList = post.getListPhoto();
			List<String> photoUrlList = new ArrayList<>();
			photoList.stream().map(photo -> photoUrlList.add(photo.getName())).collect(Collectors.toList());
			PostListDto postListDto = modelMapper.map(post, PostListDto.class);
			postListDto.setPhotoUrl(photoUrlList);
			List<Like> likeList = new ArrayList<>();
			for (Like like : post.getLikeList()) {
				if(like.getDeleteFlg() != Constant.DELETED_FlG) {
					likeList.add(like);
				}
			}
			postListDto.setLikeCount(likeList.size());
			List<Comment> commentList = new ArrayList<>();
			for (Comment comment : post.getCommentList()) {
				if(comment.getDeleteFlg() != Constant.DELETED_FlG) {
					commentList.add(comment);
				}
			}
			postListDto.setCommentCount(commentList.size());
			postListDto.setUsername(post.getUser().getUsername());
			postListDtos.add(postListDto);
		}
		return postListDtos;
	}

	@Override
	public PostDetailDto getPost(int postId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);

		if (post == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		PostDetailDto postDetailDto = modelMapper.map(post, PostDetailDto.class);
		List<Comment> commentList = post.getCommentList().stream().sorted((comment1, comment2) -> {
			if (comment1.getUpdateDate().after(comment2.getUpdateDate())) {
				return 1;
			} else if (comment1.getUpdateDate().before(comment2.getUpdateDate())) {
				return -1;
			}
			return 0;
		}).collect(Collectors.toList());

		postDetailDto.setLikeCount(post.getLikeList().size());
		postDetailDto.setCommentList(commentList);

		return postDetailDto;
	}

	@Override
	public PostUpdatedDto updatePost(String content, MultipartFile[] photos, int postId, int userId) throws Exception {
		User user = userRepository.findById(userId).orElse(null);
		Post postToUpdate = postRepository.findById(postId).orElse(null);

		if (user == null || postToUpdate == null || user.getUserId() != postToUpdate.getUser().getUserId()) {
			throw new Exception(Constant.SERVER_ERROR);
		}

//		Post post = new Post();
		postToUpdate.setContent(content);
		List<String> photoUrls = new ArrayList<>();
		if (photos != null) {
			List<Photo> photoList = new ArrayList<>();
			for (MultipartFile file : photos) {
				String photoUrl = imageUtils.saveImage(file);
				Photo photo = new Photo();
				photo.setPost(postToUpdate);
				photo.setName(photoUrl);
				photo.setCreateDate(new Date());
				
				photo = photoRepository.save(photo);
				if (photo == null) {
					throw new Exception(Constant.SERVER_ERROR);
				}
				photoList.add(photo);
				photoUrls.add(photoUrl);
			}
			postToUpdate.setListPhoto(photoList);
		}
		postToUpdate.setUpdateDate(new Date());
//		post.setLikeList(postToUpdate.getLikeList());
//		post.setCommentList(postToUpdate.getCommentList());
//		post.setDeleteFlg(postToUpdate.getDeleteFlg());

		postToUpdate = postRepository.save(postToUpdate);

		PostUpdatedDto postUpdatedDto = modelMapper.map(postToUpdate, PostUpdatedDto.class);
		postUpdatedDto.setPhotoUrls(photoUrls);
		
		return postUpdatedDto;
	}

	@Override
	public boolean deletePost(int postId, int userId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);
		
		if (post.getUser().getUserId() != userId) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		post.setDeleteFlg(Constant.DELETED_FlG);
		
		return postRepository.save(post) != null;
	}

//	@Override
//	public int countPost(int userId) {
//		LocalDate date = LocalDate.now();
//		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
//		Date dateStart = Date.from(date.with(fieldISO, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
//		Date dateEnd = Date.from(date.with(fieldISO, 7).atStartOfDay(ZoneId.systemDefault()).toInstant());
//		
//		return postRepository.countPost(userId, dateStart, dateEnd);
//	}

}
