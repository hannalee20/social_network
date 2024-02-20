package com.training.socialnetwork.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.training.socialnetwork.dto.request.comment.CommentCreateRequestDto;
import com.training.socialnetwork.dto.request.comment.CommentUpdateRequestDto;
import com.training.socialnetwork.dto.response.comment.CommentCreateResponseDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailResponseDto;
import com.training.socialnetwork.dto.response.comment.CommentUpdateResponseDto;
import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.CommentRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.CommentService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.image.ImageUtils;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

	@InjectMocks
	private CommentService commentService;
	
	@Mock
	private ImageUtils imageUtils;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private CommentRepository commentRepository;
	
	@Mock
	private UserRepository userRepository;

	@Mock
	private PostRepository postRepository;
	
	@Test
	public void createCommentSuccess() throws Exception {
		int userId = 1;
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setEmail("test@gmail.com");
		
		Post post = new Post();
		post.setPostId(1);
		post.setContent("content post");
		
		CommentCreateRequestDto commentCreateDto = new CommentCreateRequestDto();
		commentCreateDto.setPostId(1);
		commentCreateDto.setContent("comment content");
		
		Comment comment = new Comment();
		comment.setPost(post);
		comment.setUser(user);
		comment.setContent("comment content");
		
		when(postRepository.findById(any())).thenReturn(Optional.of(post));
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		
		CommentCreateResponseDto commentCreatedDto = new CommentCreateResponseDto();
		
		when(commentRepository.save(any())).thenReturn(comment);
		when(modelMapper.map(any(), any())).thenReturn(commentCreatedDto);
		
		commentService.createComment(userId, commentCreateDto);
	}
	
	@Test
	public void createCommentFail() {
		int userId = 1;
		CommentCreateRequestDto commentCreateDto = new CommentCreateRequestDto();
		commentCreateDto.setPostId(1);
		commentCreateDto.setContent("comment content");
		
		when(postRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(CustomException.class, () -> commentService.createComment(userId, commentCreateDto));
	}
	
	@Test
	public void updateCommentSuccess() throws Exception {
		int commentId = 1;
		int userId = 1;
		String content = "content update";
		CommentUpdateRequestDto commentUpdateDto = new CommentUpdateRequestDto();
		commentUpdateDto.setContent(content);
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setEmail("test@gmail.com");
		
		Post post = new Post();
		post.setPostId(1);
		post.setContent("content post");
		
		Comment comment = new Comment();
		comment.setCommentId(1);
		comment.setPost(post);
		comment.setUser(user);
		comment.setContent("comment update content");
		
		CommentUpdateResponseDto commentUpdatedDto = new CommentUpdateResponseDto();
		
		when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
		when(commentRepository.save(any())).thenReturn(comment);
		when(modelMapper.map(any(), any())).thenReturn(commentUpdatedDto);
		
		commentService.updateComment(commentUpdateDto, commentId, userId);
	}
	
	@Test
	public void updateCommentFail() {
		int userId = 1;
		int commentId = 2;
		String content = "test";
		CommentUpdateRequestDto commentUpdateDto = new CommentUpdateRequestDto();
		commentUpdateDto.setContent(content);
		
		when(commentRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> commentService.updateComment(commentUpdateDto, commentId, userId));
	}
	
	@Test
	public void updateCommentFail2() {
		int userId = 1;
		int commentId = 2;
		String content = "test";
		CommentUpdateRequestDto commentUpdateDto = new CommentUpdateRequestDto();
		commentUpdateDto.setContent(content);
		
		User user = new User();
		user.setUserId(2);
		
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setContent("content");
		
		when(commentRepository.findById(anyInt())).thenReturn(Optional.of(comment));

		assertThrows(CustomException.class, () -> commentService.updateComment(commentUpdateDto, commentId, userId));
	}
	
	@Test
	public void deleteCommentSuccess() throws Exception {
		int commentId = 1;
		int userId = 1;
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setEmail("test@gmail.com");
		
		Comment comment = new Comment();
		comment.setCommentId(1);
		comment.setUser(user);
		comment.setContent("comment content");
		comment.setDeleteFlg(Constant.DELETED_FlG);
		
		when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
		when(commentRepository.save(any())).thenReturn(comment);
		
		commentService.deleteComment(commentId, userId);
	}
	
	@Test
	public void deleteCommentFail() {
		int userId = 1;
		int commentId = 2;
		
		User user = new User();
		user.setUserId(2);
		
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setContent("content");
		
		when(commentRepository.findById(anyInt())).thenReturn(Optional.of(comment));

		assertThrows(CustomException.class, () -> commentService.deleteComment(commentId, userId));
	}
	
	@Test
	public void deleteCommentFail2() {
		int userId = 1;
		int commentId = 2;
		
		when(commentRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> commentService.deleteComment(commentId, userId));
	}
	
	@Test
	public void getCommentDetailSuccess() throws Exception {
		int commentId = 1;
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setEmail("test@gmail.com");
		
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setCommentId(1);
		comment.setContent("comment content");
		
		CommentDetailResponseDto commentDetailDto = new CommentDetailResponseDto();
		when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
		when(modelMapper.map(any(), any())).thenReturn(commentDetailDto);
		
		commentService.getCommentDetail(commentId);
	}
	
	@Test
	public void getCommentDetailFail() {
		int commentId = 2;
		
		when(commentRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> commentService.getCommentDetail(commentId));
	}
}
