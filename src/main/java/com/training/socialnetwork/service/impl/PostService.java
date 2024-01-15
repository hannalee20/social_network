package com.training.socialnetwork.service.impl;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.training.socialnetwork.dto.request.post.PostUpdateDto;
import com.training.socialnetwork.dto.response.post.PostCreatedDto;
import com.training.socialnetwork.dto.response.post.PostDetailDto;
import com.training.socialnetwork.dto.response.post.PostListDto;
import com.training.socialnetwork.dto.response.post.PostUpdatedDto;
import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.IPostService;
import com.training.socialnetwork.util.constant.Constant;

@Service
public class PostService implements IPostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PhotoRepository photoRepository;

	@Autowired
	private ModelMapper modelMapper;

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
		if (photos.length > 0) {
			for (MultipartFile file : photos) {
				String name = StringUtils.cleanPath(file.getOriginalFilename());
				Photo photo = new Photo();
				photo.setPost(postCreated);
				photo.setName(name);
				photo.setType(file.getContentType());
				photo.setData(file.getBytes());
				
				photo = photoRepository.save(photo);
				if (photo == null) {
					throw new Exception(Constant.SERVER_ERROR);
				}
				String photoUrl = ServletUriComponentsBuilder
				          .fromCurrentContextPath()
				          .path("/files/")
				          .path(Integer.toString(photo.getPhotoId()))
				          .toUriString();
				photoList.add(photo);
				photoUrls.add(photoUrl);
			}
			postCreated.setListPhoto(photoList);
		}
		
		PostCreatedDto postCreatedDto = modelMapper.map(postCreated, PostCreatedDto.class);
		postCreatedDto.setPhotoUrl(photoUrls);
		return postCreatedDto;
	}

	@Override
	public List<PostListDto> getAllPosts(int userId) {
		List<Post> postList = postRepository.findAllByUserId(userId);

		return postList.stream().map(post -> {
			PostListDto postListDto = modelMapper.map(post, PostListDto.class);
			postListDto.setLikeCount(post.getLikeList().size());
			postListDto.setCommentCount(post.getCommentList().size());
			return postListDto;
		}).collect(Collectors.toList());
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
	public PostUpdatedDto updatePost(PostUpdateDto postUpdateDto, int postId, int userId) throws Exception {
		User user = userRepository.findById(userId).orElse(null);
		Post postToUpdate = postRepository.findById(postId).orElse(null);

		if (user == null || postToUpdate == null || user.getUserId() != postToUpdate.getUser().getUserId()) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		Post post = modelMapper.map(postUpdateDto, Post.class);
		post.setLikeList(postToUpdate.getLikeList());
		post.setCommentList(postToUpdate.getCommentList());
		post.setDeleteFlg(postToUpdate.getDeleteFlg());

		Post postUpdated = postRepository.save(post);

		if (postUpdated != null) {
			return modelMapper.map(postUpdated, PostUpdatedDto.class);
		}

		throw new Exception(Constant.SERVER_ERROR);
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

	@Override
	public int countPost(int userId) {
		LocalDate date = LocalDate.now();
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		LocalDate dateStart = date.with(fieldISO, 1);
		LocalDate dateEnd = date.with(fieldISO, 7);
		
		return postRepository.countPost(userId, dateStart, dateEnd);
	}

}
