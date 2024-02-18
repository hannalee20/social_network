package com.training.socialnetwork.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdatedDto;
import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.CommentRepository;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.ICommentService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;

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
	private PhotoRepository photoRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentCreatedDto createComment(int userId, CommentCreateDto commentCreateDto) throws Exception {
		Post post = postRepository.findById(commentCreateDto.getPostId()).orElse(null);
		User user = userRepository.findById(userId).orElse(null);

		if (post == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Post does not exist");
		}

		Photo photo = null;
		if(null != commentCreateDto.getPhotoId()) {
			photo = photoRepository.findById(commentCreateDto.getPhotoId()).orElse(null);
			
			if(photo == null) {
				throw new CustomException(HttpStatus.NOT_FOUND, "Photo does not exist");
			}
			if(photo.getUser().getUserId() != userId) {
				throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to use this photo");
			}
		}
		Comment comment = new Comment();
		comment.setPost(post);
		comment.setUser(user);
		comment.setContent(commentCreateDto.getContent());
		comment.setPhoto(photo);
		comment.setCreateDate(new Date());
		comment.setUpdateDate(new Date());
		comment = commentRepository.save(comment);

		CommentCreatedDto commentCreatedDto = modelMapper.map(comment, CommentCreatedDto.class);
		commentCreatedDto.setUsername(comment.getUser().getUsername());

		return commentCreatedDto;
	}

	@Override
	public CommentUpdatedDto updateComment(CommentUpdateDto commentUpdateDto, int commentId, int userId)
			throws Exception {
		Comment commentToUpdate = commentRepository.findById(commentId).orElse(null);

		if (commentToUpdate == null) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Comment does not exist");
		}

		if (commentToUpdate.getUser().getUserId() != userId) {
			throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to update");
		}

		if (null != commentUpdateDto.getContent()) {
			commentToUpdate.setContent(commentUpdateDto.getContent());
		}

		if (null != commentUpdateDto.getPhotoId()) {
			Photo photo = photoRepository.findById(commentUpdateDto.getPhotoId()).orElse(null);
			
			if(photo == null) {
				throw new CustomException(HttpStatus.NOT_FOUND, "Photo does not exist");
			}
			if(photo.getUser().getUserId() != userId) {
				throw new CustomException(HttpStatus.FORBIDDEN, "You do not have permission to use this photo");
			}
			commentToUpdate.setPhoto(photo);
		}

		commentToUpdate.setUpdateDate(new Date());
		commentToUpdate = commentRepository.save(commentToUpdate);

		CommentUpdatedDto commentUpdatedDto = modelMapper.map(commentToUpdate, CommentUpdatedDto.class);
		commentUpdatedDto.setUsername(commentToUpdate.getUser().getUsername());

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
		comment.setUpdateDate(new Date());

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
