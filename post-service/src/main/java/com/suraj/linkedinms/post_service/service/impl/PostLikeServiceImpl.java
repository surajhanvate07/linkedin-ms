package com.suraj.linkedinms.post_service.service.impl;

import com.suraj.linkedinms.post_service.entity.Post;
import com.suraj.linkedinms.post_service.entity.PostLike;
import com.suraj.linkedinms.post_service.event.PostLikedEvent;
import com.suraj.linkedinms.post_service.exception.BadRequestException;
import com.suraj.linkedinms.post_service.exception.ResourceNotFoundException;
import com.suraj.linkedinms.post_service.repository.PostLikeRepository;
import com.suraj.linkedinms.post_service.repository.PostRepository;
import com.suraj.linkedinms.post_service.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

	public final PostRepository postRepository;
	private final PostLikeRepository postLikeRepository;
	private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

	@Override
	public void likePost(Long postId, Long userId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));

		boolean isPostExists = postRepository.existsById(postId);
		if (!isPostExists) {
			throw new ResourceNotFoundException("Post not found with ID: " + postId);
		}
		log.info("Checking if post with ID: {} is already liked by user with ID: {}", postId, userId);
		boolean isAlreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);
		if (isAlreadyLiked) throw new BadRequestException("Post already liked by user with ID: " + userId);

		PostLike postLike = new PostLike();
		postLike.setPostId(postId);
		postLike.setUserId(userId);

		PostLike savedPostLike = postLikeRepository.save(postLike);
		log.info("Post with ID: {} liked by user with ID: {}", postId, userId);

		PostLikedEvent postLikedEvent = PostLikedEvent.builder()
				.postId(postId)
				.likedByUserId(userId)
				.creatorId(post.getUserId())
				.build();

		log.info("Publishing PostLikedEvent to Kafka: {}", postLikedEvent);
		kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
	}

	@Override
	public void unlikePost(Long postId, Long userId) {
		boolean isPostExists = postRepository.existsById(postId);
		if (!isPostExists) {
			throw new ResourceNotFoundException("Post not found with ID: " + postId);
		}
		boolean isAlreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);
		if (!isAlreadyLiked)
			throw new BadRequestException("Post is not liked by user with ID: " + userId + ". Cannot remove like.");

		log.info("Removing like for post with ID: {} by user with ID: {}", postId, userId);
		postLikeRepository.deleteByPostIdAndUserId(postId, userId);
		log.info("Like removed for post with ID: {} by user with ID: {}", postId, userId);
	}
}
