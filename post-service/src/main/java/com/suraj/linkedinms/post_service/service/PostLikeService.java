package com.suraj.linkedinms.post_service.service;

public interface PostLikeService {
	void likePost(Long postId, Long userId);

	void unlikePost(Long postId, Long userId);
}
