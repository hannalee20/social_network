package com.training.socialnetwork.service.impl;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateDto;
import com.training.socialnetwork.dto.response.comment.CommentCreatedDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailDto;
import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.CommentRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.ICommentService;
import com.training.socialnetwork.util.constant.Constant;

@Service
public class CommentService implements ICommentService{
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentCreatedDto createComment(CommentCreateDto commentCreateDto) throws Exception {
		Post post = postRepository.findById(commentCreateDto.getPostId()).orElse(null);
		User user = userRepository.findById(commentCreateDto.getUserId()).orElse(null);
		
		if (post == null || user == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		
		Comment comment = modelMapper.map(commentCreateDto, Comment.class);
		comment.setPost(post);
		comment.setUser(user);
		
		Comment commentCreated = commentRepository.save(comment);
		
		if(commentCreated != null) {
			return modelMapper.map(commentCreated, CommentCreatedDto.class);
		}
		
		throw new Exception(Constant.SERVER_ERROR);
	}

	@Override
	public CommentCreatedDto updateComment(CommentUpdateDto commentUpdateDto, int userId) throws Exception {
		Post post = postRepository.findById(commentUpdateDto.getPostId()).orElse(null);
		User user = userRepository.findById(commentUpdateDto.getUserId()).orElse(null);
		Comment commentToUpdate = commentRepository.findById(commentUpdateDto.getCommentId()).orElse(null);
		
		if (post == null || user == null || commentToUpdate == null || commentUpdateDto.getUserId() != userId) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		
		Comment comment = modelMapper.map(commentToUpdate, Comment.class);
		comment.setPost(commentToUpdate.getPost());
		comment.setUser(commentToUpdate.getUser());
		
		Comment commentUpdated =  commentRepository.save(comment);
		
		if (commentUpdated != null) {
			return modelMapper.map(commentUpdated, CommentCreatedDto.class);
		}
		
		throw new Exception(Constant.SERVER_ERROR);
	}

	@Override
	public boolean deleteComment(int commentId, int userId) throws Exception {
		Comment comment = commentRepository.findById(commentId).orElse(null);
		User user = userRepository.findById(userId).orElse(null);
		
		if (comment == null || user == null || comment.getUser().getUserId() != userId) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		comment.setDeleteFlg(Constant.DELETED_FlG);
		
		return commentRepository.save(comment) != null;
	}

	@Override
	public CommentDetailDto getCommentDetail(int commentId) throws Exception {
		Comment comment = commentRepository.findById(commentId).orElse(null);
		
		if(comment == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		
		return modelMapper.map(comment, CommentDetailDto.class);
	}

	@Override
	public int countComment(int userId) {
		LocalDate date = LocalDate.now();
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		LocalDate dateStart = date.with(fieldISO, 1);
		LocalDate dateEnd = date.with(fieldISO, 7);
		
		return commentRepository.countComment(userId, dateStart, dateEnd);
	}

}
