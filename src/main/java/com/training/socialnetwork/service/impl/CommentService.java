package com.training.socialnetwork.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

		if (post == null || user == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		Comment comment = modelMapper.map(commentCreateDto, Comment.class);
		comment.setPost(post);
		comment.setUser(user);
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
			throw new Exception(Constant.SERVER_ERROR);
		}
		Post post = postRepository.findById(commentToUpdate.getPost().getPostId()).orElse(null);
		User user = userRepository.findById(userId).orElse(null);

		if (post == null || user == null || commentToUpdate.getUser().getUserId() != userId) {
			throw new Exception(Constant.SERVER_ERROR);
		}
		
		commentToUpdate.setContent(content);
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

		if (comment == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		CommentDetailDto commentDetailDto = modelMapper.map(comment, CommentDetailDto.class);
		commentDetailDto.setUsername(comment.getUser().getUsername());
		
		return commentDetailDto;
	}

//	@Override
//	public int countComment(int userId) {
//		LocalDate date = LocalDate.now();
//		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
//		Date dateStart = Date.from(date.with(fieldISO, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
//		Date dateEnd = Date.from(date.with(fieldISO, 7).atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//		return commentRepository.countComment(userId, dateStart, dateEnd);
//	}

}
