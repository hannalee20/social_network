package com.training.socialnetwork.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.socialnetwork.dto.request.comment.CommentCreateDto;
import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.CommentRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.CommentService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.image.ImageUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ImageUtils imageUtils;
	
	@MockBean
	private CommentRepository commentRepository;
	
	@MockBean
	private UserRepository userRepository;

	@MockBean
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
		
		CommentCreateDto commentCreateDto = new CommentCreateDto();
		commentCreateDto.setPostId(1);
		commentCreateDto.setContent("comment content");
		
		MockMultipartFile photo1 =
                new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data", "some xml".getBytes());
		
		when(postRepository.findById(any())).thenReturn(Optional.of(post));
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		
		Comment comment = new Comment();
		comment.setPost(post);
		comment.setUser(user);
		comment.setContent("comment content");
		comment.setPhotoUrl(imageUtils.saveImage(photo1));
		
		when(commentRepository.save(any())).thenReturn(comment);
		commentService.createComment(userId, commentCreateDto, photo1);
		
//		verify(commentRepository).save(any());
	}
	
	@Test
	public void updateCommentSuccess() throws Exception {
		int commentId = 1;
		int userId = 1;
		String content = "content update";
		MockMultipartFile photo1 =
                new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data", "some xml".getBytes());
		
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
		comment.setPhotoUrl(imageUtils.saveImage(photo1));
		
		when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
		when(postRepository.findById(any())).thenReturn(Optional.of(post));
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		when(commentRepository.save(any())).thenReturn(comment);
		
		commentService.updateComment(content, photo1, commentId, userId);
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
		
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
		when(commentRepository.save(any())).thenReturn(comment);
		
		commentService.deleteComment(commentId, userId);
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
		
		when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
		
		commentService.getCommentDetail(commentId);
	}
}
