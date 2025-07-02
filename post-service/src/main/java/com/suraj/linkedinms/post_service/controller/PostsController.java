package com.suraj.linkedinms.post_service.controller;

import com.suraj.linkedinms.post_service.auth.UserContextHolder;
import com.suraj.linkedinms.post_service.dto.PostCreateRequestDto;
import com.suraj.linkedinms.post_service.dto.PostDto;
import com.suraj.linkedinms.post_service.entity.Post;
import com.suraj.linkedinms.post_service.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {

	private final PostService postService;

	@PostMapping()
	public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto, HttpServletRequest httpServletRequest) {
		PostDto createdPost = postService.createPost(postCreateRequestDto, 1L);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Long postId) {
		Long userId = UserContextHolder.getCurrentUserId();
		log.info("User ID from UserContextHolder: {}", userId);
		PostDto fetchedPost = postService.getPostById(postId);
		return ResponseEntity.ok(fetchedPost);
	}

	@GetMapping("/users/{userId}/allPosts")
	public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable("userId") Long userId) {
		List<PostDto> posts = postService.getAllPostsOfUser(userId);
		return ResponseEntity.ok(posts);
	}
}
