package com.suraj.linkedinms.post_service.service;

import com.suraj.linkedinms.post_service.dto.PostCreateRequestDto;
import com.suraj.linkedinms.post_service.dto.PostDto;
import com.suraj.linkedinms.post_service.entity.Post;

import java.util.List;

public interface PostService {
	PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId);
	PostDto getPostById(Long postId);
	List<PostDto> getAllPostsOfUser(Long userId);
}
