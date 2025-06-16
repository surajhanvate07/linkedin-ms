package com.suraj.linkedinms.post_service.repository;

import com.suraj.linkedinms.post_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByUserId(Long userId);
}
