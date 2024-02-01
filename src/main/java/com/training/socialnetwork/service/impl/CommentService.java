package com.training.socialnetwork.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdatedDto;
import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.CommentRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.ICommentService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.image.ImageUtils;

@Service
@Transactional
public class CommentService implements ICommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ImageUtils imageUtils;

	@Override
	public CommentCreatedDto createComment(int userId, CommentCreateDto commentCreateDto, MultipartFile photo)
			throws Exception {
		Post post = postRepository.findById(commentCreateDto.getPostId()).orElse(null);
		User user = userRepository.findById(userId).orElse(null);

		if (post == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}

		Comment comment = new Comment();
		comment.setPost(post);
		comment.setUser(user);
		comment.setContent(commentCreateDto.getContent());
		if (photo != null) {
			String photoUrl = imageUtils.saveImage(photo);
			comment.setPhotoUrl(photoUrl);
		}
		comment.setCreateDate(new Date());
		comment.setUpdateDate(new Date());
		Comment commentCreated = commentRepository.save(comment);

		CommentCreatedDto commentCreatedDto = modelMapper.map(commentCreated, CommentCreatedDto.class);
		commentCreatedDto.setUsername(commentCreated.getUser().getUsername());

		return commentCreatedDto;
	}

	@Override
	public CommentUpdatedDto updateComment(String content, MultipartFile photo, int commentId, int userId)
			throws Exception {
		Comment commentToUpdate = commentRepository.findById(commentId).orElse(null);

		if (commentToUpdate == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Comment does not exist");
		}
		Post post = postRepository.findById(commentToUpdate.getPost().getPostId()).orElse(null);

		if (post == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}
		
		if (commentToUpdate.getUser().getUserId() != userId) {
			throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to update");
		}
		
		if(content != null) {
			commentToUpdate.setContent(content);
		}
		
		if(photo != null) {
			String photoUrl = imageUtils.saveImage(photo);
			commentToUpdate.setPhotoUrl(photoUrl);
		}

		Comment commentUpdated = commentRepository.save(commentToUpdate);

		CommentUpdatedDto commentUpdatedDto = modelMapper.map(commentUpdated, CommentUpdatedDto.class);
		commentUpdatedDto.setUsername(commentUpdated.getUser().getUsername());
		
		return commentUpdatedDto;
	}

	@Override
	public boolean deleteComment(int commentId, int userId) throws Exception {
		Comment comment = commentRepository.findById(commentId).orElse(null);

		if (comment == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Comment does not exist");
		}
		
		if (comment.getUser().getUserId() != userId) {
			throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to update");
		}
		
		comment.setDeleteFlg(Constant.DELETED_FlG);

		return commentRepository.save(comment) != null;
	}

	@Override
	public CommentDetailDto getCommentDetail(int commentId) throws Exception {
		Comment comment = commentRepository.findById(commentId).orElse(null);

		if (comment == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Comment does not exist");
		}

		CommentDetailDto commentDetailDto = modelMapper.map(comment, CommentDetailDto.class);
		commentDetailDto.setUsername(comment.getUser().getUsername());
		
		return commentDetailDto;
	}

}
