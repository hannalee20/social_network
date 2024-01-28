package com.training.socialnetwork.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.PostService;
import com.training.socialnetwork.util.constant.Constant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTest {

	@Autowired
	private PostService postService;
	
	@MockBean
	private PostRepository postRepository;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private PhotoRepository photoRepository;
	
	@Test
	public void createPostSuccess() throws Exception {
		int userId = 1;
		String content = "test create post";
		
		MockMultipartFile photo1 =
                new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data", "some xml".getBytes());
		MockMultipartFile photo2 =
                new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data", "some xml".getBytes());
		MockMultipartFile[] photos =
                {photo1, photo2};
		
		User user = new User();
		user.setUserId(userId);
		user.setUsername("test");
		user.setEmail("test@gmail.com");
		
		Post post = new Post();
		post.setUser(user);
		post.setContent(content);
		post.setCreateDate(new Date());
		post.setUpdateDate(new Date());

		
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		
		Photo photo = new Photo();
		photo.setPost(post);
		photo.setName("data1");
		photo.setCreateDate(new Date());
		
		when(photoRepository.save(any())).thenReturn(photo);
		when(postRepository.save(any())).thenReturn(post);
		
		postService.createPost(userId, content, photos);
	}
	
	@Test
	public void getTimelineSuccess() {
		int userId = 1;
		
		User user1 = new User();
		user1.setUserId(2);
		user1.setUsername("test2");
		user1.setEmail("test2@gmail.com");
		
		User user2 = new User();
		user2.setUserId(3);
		user2.setUsername("test3");
		user2.setEmail("test3@gmail.com");
		
		Post post1 = new Post();
		post1.setUser(user1);
		post1.setContent("content1");
		
		Post post2 = new Post();
		post2.setUser(user2);
		post2.setContent("content2");
		
		Like like1 = new Like();
		like1.setLikeId(1);
		like1.setPost(post1);
		like1.setDeleteFlg(Constant.UNDELETED_FLG);
		
		Like like2 = new Like();
		like2.setLikeId(2);
		like2.setPost(post1);
		like2.setDeleteFlg(Constant.UNDELETED_FLG);
		
		List<Like> likeList1 = new ArrayList<>();
		List<Like> likeList2 = new ArrayList<>();
		likeList1.add(like1);
		likeList1.add(like2);
		
		Comment comment1 = new Comment();
		comment1.setCommentId(1);
		comment1.setPost(post1);
		comment1.setContent("comment1");
		comment1.setDeleteFlg(Constant.UNDELETED_FLG);
		
		List<Comment> commentList1 = new ArrayList<>();
		List<Comment> commentList2 = new ArrayList<>();
		commentList1.add(comment1);
		
		Photo photo = new Photo();
		photo.setPhotoId(1);
		photo.setPost(post1);
		photo.setName("test");
		photo.setDeleteFlg(Constant.UNDELETED_FLG);
		
		List<Photo> photoList1 = new ArrayList<>();
		List<Photo> photoList2 = new ArrayList<>();
		photoList1.add(photo);
		
		post1.setListPhoto(photoList1);
		post1.setLikeList(likeList1);
		post1.setCommentList(commentList1);
		post2.setListPhoto(photoList2);
		post2.setLikeList(likeList2);
		post2.setCommentList(commentList2);
		
		List<Post> postList = new ArrayList<>();
		postList.add(post1);
		postList.add(post2);
		
		when(postRepository.findAllByUserId(1, null)).thenReturn(postList);
		
		postService.getTimeline(userId, null);
	}
	
	@Test
	public void getPostSuccess() throws Exception {
		int postId = 1;
		
		Post post1 = new Post();
		post1.setPostId(postId);
		post1.setContent("content1");
		
		Comment comment1 = new Comment();
		comment1.setCommentId(1);
		comment1.setPost(post1);
		comment1.setContent("comment1");
		comment1.setDeleteFlg(Constant.UNDELETED_FLG);
		comment1.setUpdateDate(new Date());
		
		Comment comment2 = new Comment();
		comment2.setCommentId(2);
		comment2.setPost(post1);
		comment2.setContent("comment2");
		comment2.setDeleteFlg(Constant.UNDELETED_FLG);
		comment2.setUpdateDate(new Date());
		
		List<Comment> commentList1 = new ArrayList<>();
		commentList1.add(comment1);
		commentList1.add(comment2);
		post1.setCommentList(commentList1);
		
		Like like1 = new Like();
		like1.setLikeId(1);
		like1.setPost(post1);
		like1.setDeleteFlg(Constant.UNDELETED_FLG);
		
		Like like2 = new Like();
		like2.setLikeId(2);
		like2.setPost(post1);
		like2.setDeleteFlg(Constant.UNDELETED_FLG);
		
		List<Like> likeList1 = new ArrayList<>();
		likeList1.add(like1);
		likeList1.add(like2);
		
		post1.setLikeList(likeList1);
		post1.setCommentList(commentList1);
		
		when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
		
		postService.getPost(postId);
	}
	
	@Test
	public void updatePostSuccess() throws Exception {
		String content = "content update post";
		int userId = 1;
		int postId = 1;
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		
		Post post1 = new Post();
		post1.setPostId(1);
		post1.setUser(user);
		post1.setContent("content1");
		
		MockMultipartFile photo1 =
                new MockMultipartFile("data1", "filename1.jpg", "multipart/form-data", "some xml".getBytes());
		MockMultipartFile photo2 =
                new MockMultipartFile("data2", "filename2.jpg", "multipart/form-data", "some xml".getBytes());
		MockMultipartFile[] photos =
                {photo1, photo2};
		
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
		
		Photo photo = new Photo();
		photo.setPost(post1);
		photo.setName("data1");
		photo.setCreateDate(new Date());
		
		when(photoRepository.save(any())).thenReturn(photo);
		when(postRepository.save(any())).thenReturn(post1);
		
		postService.updatePost(content, photos, postId, userId);
	}
	
	@Test
	public void deletePostSuccess() throws Exception {
		int userId = 1;
		int postId = 1;
		
		User user = new User();
		user.setUserId(1);
		user.setUsername("test");
		user.setPassword("123456");
		user.setEmail("test@gmail.com");
		
		Post post1 = new Post();
		post1.setPostId(1);
		post1.setUser(user);
		post1.setContent("content1");
		
		when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
		
		postService.deletePost(postId, userId);
	}
}