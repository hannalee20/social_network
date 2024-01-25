package com.training.socialnetwork.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.LikeRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.LikeService;
import com.training.socialnetwork.util.constant.Constant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LikeServiceTest {

	@Autowired
	private LikeService likeService;
	
	@MockBean
	private LikeRepository likeRepository;
	
	@MockBean
	private PostRepository postRepository;

	@MockBean
	private UserRepository userRepository;
	
	@Test
	public void addLikeSuccess() throws Exception {
		int postId = 1;
		int userId = 1;
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		
		Post post1 = new Post();
		post1.setPostId(1);
		post1.setContent("content1");
		
		when(postRepository.findById(any())).thenReturn(Optional.of(post1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		
		Like like = new Like();
		like.setPost(post1);
		like.setUser(user);
		like.setDeleteFlg(Constant.UNDELETED_FLG);
		
		when(likeRepository.save(any())).thenReturn(like);
		
		likeService.addLikePost(postId, userId);
	}
	
	@Test
	public void unlikeSuccess() throws Exception {
		int postId = 1;
		int userId = 1;
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		
		Post post1 = new Post();
		post1.setPostId(1);
		post1.setContent("content1");
		
		when(postRepository.findById(any())).thenReturn(Optional.of(post1));
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		
		Like like = new Like();
		like.setPost(post1);
		like.setUser(user);
		
		when(likeRepository.findByPostAndUser(post1, user)).thenReturn(like);
		when(likeRepository.save(any())).thenReturn(like);
		
		likeService.unlikePost(postId, userId);
	}
}
