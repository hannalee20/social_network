package com.training.socialnetwork.service.impl;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.socialnetwork.entity.Like;
import com.training.socialnetwork.entity.Post;
import com.training.socialnetwork.entity.User;
import com.training.socialnetwork.repository.LikeRepository;
import com.training.socialnetwork.repository.PostRepository;
import com.training.socialnetwork.repository.UserRepository;
import com.training.socialnetwork.service.ILikeService;
import com.training.socialnetwork.util.constant.Constant;

@Service
public class LikeService implements ILikeService {

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean addLikePost(int postId, int userId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);
		User user = userRepository.findById(userId).orElse(null);

		if (post == null || user == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		Like like = new Like();
		like.setPost(post);
		like.setUser(user);
		like.setDeleteFlg(Constant.UNDELETED_FLG);

		return likeRepository.save(like) != null;
	}

	@Override
	public boolean unlikePost(int postId, int userId) throws Exception {
		Post post = postRepository.findById(postId).orElse(null);
		User user = userRepository.findById(userId).orElse(null);

		if (post == null || user == null) {
			throw new Exception(Constant.SERVER_ERROR);
		}

		Like like = likeRepository.findByPostAndUser(post, user);
		like.setDeleteFlg(Constant.DELETED_FlG);

		return likeRepository.save(like) != null;
	}

	@Override
	public int countLike(int userId) {
		LocalDate date = LocalDate.now();
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		LocalDate dateStart = date.with(fieldISO, 1);
		LocalDate dateEnd = date.with(fieldISO, 7);
		
		return likeRepository.countLike(userId, dateStart, dateEnd);
	}

}
