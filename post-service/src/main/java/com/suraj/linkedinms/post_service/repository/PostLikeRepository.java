package com.suraj.linkedinms.post_service.repository;

import com.suraj.linkedinms.post_service.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	boolean existsByPostIdAndUserId(Long postId, Long userId);

	@Transactional
	void deleteByPostIdAndUserId(Long postId, Long userId);
}
