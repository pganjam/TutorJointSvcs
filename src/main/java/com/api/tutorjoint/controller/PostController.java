package com.api.tutorjoint.controller;

import com.api.tutorjoint.dto.PostDto;
import com.api.tutorjoint.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity createPost(@RequestBody PostDto postDto) {
        postService.createPost(postDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity updatePost(@RequestBody PostDto postDto) {
        postService.updatePost(postDto);
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(produces = "application/json",
            method = RequestMethod.DELETE,
            path = {"/{id}"})
    @PreAuthorize("hasRole('GUEST') or hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity deletePost(@PathVariable @RequestBody Long id) {
        postService.deletePost(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<PostDto>> getPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable @RequestBody Long id) {
        return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable @RequestBody String username) {
        return new ResponseEntity<>(postService.getPostsByUser(username), HttpStatus.OK);
    }

}
