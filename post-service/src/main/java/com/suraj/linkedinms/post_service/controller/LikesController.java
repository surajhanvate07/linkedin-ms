package com.suraj.linkedinms.post_service.controller;

import com.suraj.linkedinms.post_service.auth.UserContextHolder;
import com.suraj.linkedinms.post_service.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

	private final PostLikeService postLikeService;

	@PostMapping("/{postId}")
	public ResponseEntity<Void> likePost(@PathVariable("postId") Long postId) {
		Long userId = UserContextHolder.getCurrentUserId();
		postLikeService.likePost(postId, userId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> unlikePost(@PathVariable("postId") Long postId) {
		Long userId = UserContextHolder.getCurrentUserId();
		postLikeService.unlikePost(postId, userId);
		return ResponseEntity.noContent().build();
	}
}
