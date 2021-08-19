package com.api.tutorjoint.service;

import com.api.tutorjoint.exception.PartyNotFoundException;
import com.api.tutorjoint.exception.PostNotFoundException;
import com.api.tutorjoint.model.Post;
import com.api.tutorjoint.repository.PostRepository;
import com.api.tutorjoint.dto.PostDto;
import com.api.tutorjoint.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    @Autowired
    private AuthService authService;

    @Autowired
    private PostRepository postRepository;

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(toList());
    }

    public List<PostDto> getPostsByUser(String username) {
        List<Post> posts = postRepository.findByUsername(username);
        return posts.stream().map(this::mapFromPostToDto).collect(toList());
    }

    public PostDto getPost(Long id) throws PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));

        return mapFromPostToDto(post);
    }


    @Transactional
    public void createPost(PostDto postDto) throws PostNotFoundException {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        UserDetailsImpl loggedInUser = authService.getCurrentUser().orElseThrow(() -> new PartyNotFoundException("User Not Found"));
        post.setCreatedOn(Instant.now());
        post.setUsername(loggedInUser.getUsername());
        post.setUpdatedOn(Instant.now());

        postRepository.save(post);
    }

    @Transactional
    public Long updatePost(PostDto postDto) throws PostNotFoundException {
        Post post = postRepository.findById(postDto.getId()).orElseThrow(() -> new PostNotFoundException("For id " + postDto.getId()));

        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());

        postRepository.save(post);
        return postDto.getId();
    }

    @Transactional
    public void deletePost(Long id) throws PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));

        postRepository.delete(post);
    }

    private PostDto mapFromPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setUsername(post.getUsername());
        return postDto;
    }

    private Post getPartyId(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        UserDetailsImpl loggedInUser = authService.getCurrentUser().orElseThrow(() -> new PartyNotFoundException("User Not Found"));
        post.setCreatedOn(Instant.now());
        post.setUsername(loggedInUser.getUsername());
        post.setUpdatedOn(Instant.now());
        return post;
    }
}
