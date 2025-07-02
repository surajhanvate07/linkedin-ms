package com.suraj.linkedinms.post_service.service.impl;

import com.suraj.linkedinms.post_service.auth.UserContextHolder;
import com.suraj.linkedinms.post_service.client.ConnectionsClient;
import com.suraj.linkedinms.post_service.dto.PersonDto;
import com.suraj.linkedinms.post_service.dto.PostCreateRequestDto;
import com.suraj.linkedinms.post_service.dto.PostDto;
import com.suraj.linkedinms.post_service.entity.Post;
import com.suraj.linkedinms.post_service.exception.ResourceNotFoundException;
import com.suraj.linkedinms.post_service.repository.PostRepository;
import com.suraj.linkedinms.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final ModelMapper modelMapper;
	private final ConnectionsClient connectionsClient;

	@Override
	public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
		Post post = modelMapper.map(postCreateRequestDto, Post.class);
		post.setUserId(userId);

		Post savedPost = postRepository.save(post);
		return modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto getPostById(Long postId) {
		log.info("Fetching post with ID: {}", postId);
		Long userId = UserContextHolder.getCurrentUserId();
		log.info("User ID from UserContextHolder: {}", userId);
		List<PersonDto> firstConnections = connectionsClient.getFirstDegreeConnections();
		Post fetchedPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));
		return modelMapper.map(fetchedPost, PostDto.class);
	}

	@Override
	public List<PostDto> getAllPostsOfUser(Long userId) {
		log.info("Fetching all posts for user with ID: {}", userId);
		List<Post> posts = postRepository.findAllByUserId(userId);
		List<PostDto> postDtoList = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.toList();

		if (postDtoList.isEmpty()) {
			log.warn("No posts found for user with ID: {}", userId);
			throw new ResourceNotFoundException("No posts found for user with ID: " + userId);
		} else {
			return postDtoList;
		}
	}
}
