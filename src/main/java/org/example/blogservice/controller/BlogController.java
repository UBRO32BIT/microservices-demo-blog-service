package org.example.blogservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.blogservice.dto.request.CreateBlogRequest;
import org.example.blogservice.dto.request.UpdateBlogRequest;
import org.example.blogservice.dto.response.BlogResponse;
import org.example.blogservice.dto.response.DeleteBlogResponse;
import org.example.blogservice.model.Blog;
import org.example.blogservice.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@RequestAttribute Long userId, @Valid @RequestBody CreateBlogRequest createBlogRequest) {
        createBlogRequest.setUserId(userId);
        BlogResponse blog = blogService.createBlog(createBlogRequest);
         return ResponseEntity.status(HttpStatus.CREATED).body(blog);
    }

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllBlogs() {
        List<BlogResponse> blogList = blogService.getAllBlogs();
        return ResponseEntity.status(HttpStatus.OK).body(blogList);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogResponse> getBlogById(@PathVariable Long blogId) {
        BlogResponse blog = blogService.getBlogById(blogId);
        return ResponseEntity.status(HttpStatus.OK).body(blog);
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<BlogResponse> updateBlogById(@PathVariable Long blogId, @Valid @RequestBody UpdateBlogRequest updateBlogRequest) {
        BlogResponse blog = blogService.updateBlogById(blogId, updateBlogRequest);
        return ResponseEntity.status(HttpStatus.OK).body(blog);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<DeleteBlogResponse> deleteBlogResponseResponseEntity(@PathVariable Long blogId) {
        DeleteBlogResponse response = blogService.deleteBlogById(blogId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
