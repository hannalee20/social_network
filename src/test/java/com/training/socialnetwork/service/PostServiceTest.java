package com.training.socialnetwork.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.training.socialnetwork.dto.request.post.PostCreateRequestDto;
import com.training.socialnetwork.dto.request.post.PostUpdateRequestDto;
import com.training.socialnetwork.dto.response.comment.CommentDetailResponseDto;
import com.training.socialnetwork.dto.response.post.PostCreateResponseDto;
import com.training.socialnetwork.dto.response.post.PostDetailResponseDto;
import com.training.socialnetwork.dto.response.post.PostListResponseDto;
import com.training.socialnetwork.dto.response.post.PostUpdateResponseDto;
import com.training.socialnetwork.entity.Comment;
import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Photo;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.FriendRepository;
import com.training.socialnetwork.repository.PhotoRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.impl.PostService;
import com.training.socialnetwork.util.constant.Constant;
import com.training.socialnetwork.util.exception.CustomException;
import com.training.socialnetwork.util.image.ImageUtils;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

	@InjectMocks
	private PostService postService;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private ImageUtils imageUtils;

	@Mock
	private PostRepository postRepository;
	
	@Mock
	private FriendRepository friendRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PhotoRepository photoRepository;

	@Test
	public void createPostSuccess() throws Exception {
		int userId = 1;
		String content = "test create post";
		List<Integer> photoIdList = new ArrayList<>();
		photoIdList.add(1);
		
		PostCreateRequestDto postCreateDto = new PostCreateRequestDto();
		postCreateDto.setContent(content);
		postCreateDto.setPhotoIdList(photoIdList);

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
		photo.setName("data1");
		photo.setCreateDate(new Date());

		PostCreateResponseDto postCreatedDto = new PostCreateResponseDto();

		when(photoRepository.save(any())).thenReturn(photo);
		when(postRepository.save(any())).thenReturn(post);
		when(modelMapper.map(any(), any())).thenReturn(postCreatedDto);

		postService.createPost(userId, postCreateDto);
	}

	@Test
	void createPostFail() {
		int userId = 1;
		String content = "test create post";
		List<Integer> photoIdList = new ArrayList<>();
		photoIdList.add(1);
		
		PostCreateRequestDto postCreateDto = new PostCreateRequestDto();
		postCreateDto.setContent(content);
		postCreateDto.setPhotoIdList(photoIdList);

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> postService.createPost(userId, postCreateDto));
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
//		photo.setPost(post1);
		photo.setName("test");
		photo.setDeleteFlg(Constant.UNDELETED_FLG);

		List<Photo> photoList1 = new ArrayList<>();
		List<Photo> photoList2 = new ArrayList<>();
		photoList1.add(photo);

		post1.setPhotoList(photoList1);
		post1.setLikeList(likeList1);
		post1.setCommentList(commentList1);
		post2.setPhotoList(photoList2);
		post2.setLikeList(likeList2);
		post2.setCommentList(commentList2);

		List<Post> postList = new ArrayList<>();
		postList.add(post1);
		postList.add(post2);
		
		Page<Post> postPage = new PageImpl<Post>(postList);

		List<Integer> friendUserIdList = new ArrayList<>();
		PostListResponseDto postListDto = new PostListResponseDto();

		when(friendRepository.findAllFriendUserId(anyInt())).thenReturn(friendUserIdList);
		when(postRepository.findAllByUserId(anyList(), any())).thenReturn(postPage);
		when(modelMapper.map(any(), any())).thenReturn(postListDto);
		postService.getTimeline(userId, null);
	}

	@Test
	public void getPostSuccess() throws Exception {
		int postId = 1;

		User user = new User();
		user.setUsername("test");

		Post post1 = new Post();
		post1.setUser(user);
		post1.setPostId(postId);
		post1.setContent("content1");

		Comment comment1 = new Comment();
		comment1.setUser(user);
		comment1.setCommentId(1);
		comment1.setPost(post1);
		comment1.setContent("comment1");
		comment1.setDeleteFlg(Constant.UNDELETED_FLG);
		comment1.setUpdateDate(new Date());

		Comment comment2 = new Comment();
		comment2.setUser(user);
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
		
		Photo photo1 = new Photo();
		photo1.setPhotoId(1);
//		photo1.setPost(post1);
		photo1.setName("test");
		
		List<Photo> photoList = new ArrayList<>();
		photoList.add(photo1);

		post1.setLikeList(likeList1);
		post1.setCommentList(commentList1);
		post1.setPhotoList(photoList);

		PostDetailResponseDto postDetailDto = new PostDetailResponseDto();
		CommentDetailResponseDto commentDetailDto = new CommentDetailResponseDto();
		CommentDetailResponseDto commentDetailDto2 = new CommentDetailResponseDto();

		when(postRepository.findById(postId)).thenReturn(Optional.of(post1));

		when(modelMapper.map(comment1, CommentDetailResponseDto.class)).thenReturn(commentDetailDto);
		when(modelMapper.map(comment2, CommentDetailResponseDto.class)).thenReturn(commentDetailDto2);

		when(modelMapper.map(post1, PostDetailResponseDto.class)).thenReturn(postDetailDto);

		postService.getPost(postId);
	}

	@Test
	void getPostFail() {
		int postId = 1;

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> postService.getPost(postId));
	}

	@Test
	public void updatePostSuccess() throws Exception {
		String content = "content update post";
		PostUpdateRequestDto postUpdateDto = new PostUpdateRequestDto();
		postUpdateDto.setContent(content);

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

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(postRepository.findById(postId)).thenReturn(Optional.of(post1));

		Photo photo = new Photo();
//		photo.setPost(post1);
		photo.setName("data1");
		photo.setCreateDate(new Date());

		PostUpdateResponseDto postUpdatedDto = new PostUpdateResponseDto();

		when(photoRepository.save(any())).thenReturn(photo);
		when(postRepository.save(any())).thenReturn(post1);
		when(modelMapper.map(any(), any())).thenReturn(postUpdatedDto);

		postService.updatePost(postUpdateDto, postId, userId);
	}

	@Test
	void updatePostFail() {
		int userId = 1;
		int postId = 2;
		String content = "Updated Content";
		PostUpdateRequestDto postUpdateDto = new PostUpdateRequestDto();
		postUpdateDto.setContent(content);

		when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> postService.updatePost(postUpdateDto, postId, userId));
	}

	@Test
	void updatePostFail2() {
		int userId = 1;
		int postId = 2;
		String content = "Updated Content";
		PostUpdateRequestDto postUpdateDto = new PostUpdateRequestDto();
		postUpdateDto.setContent(content);

		User user = new User();
		user.setUserId(userId);

		Post postToUpdate = new Post();
		postToUpdate.setPostId(postId);
		postToUpdate.setUser(new User());

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(postRepository.findById(postId)).thenReturn(Optional.of(postToUpdate));

		assertThrows(CustomException.class, () -> postService.updatePost(postUpdateDto, postId, userId));
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

	@Test
	void deletePostFail() {
		int userId = 1;
		int postId = 2;

		User user = new User();
		user.setUserId(2);

		Post postToUpdate = new Post();
		postToUpdate.setPostId(postId);
		postToUpdate.setUser(user);

		when(postRepository.findById(anyInt())).thenReturn(Optional.of(postToUpdate));

		assertThrows(CustomException.class, () -> postService.deletePost(postId, userId));
	}

	@Test
	void deletePostFail2() {
		int userId = 1;
		int postId = 2;

		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> postService.deletePost(postId, userId));
	}
}