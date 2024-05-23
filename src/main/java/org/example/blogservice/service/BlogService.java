package org.example.blogservice.service;

import org.example.blogservice.dto.request.CreateBlogRequest;
import org.example.blogservice.dto.request.UpdateBlogRequest;
import org.example.blogservice.dto.response.BlogResponse;
import org.example.blogservice.dto.response.DeleteBlogResponse;
import org.example.blogservice.model.Blog;

import java.util.List;

public interface BlogService {
    BlogResponse createBlog(CreateBlogRequest createBlogRequest);
    List<BlogResponse> getAllBlogs();
    BlogResponse getBlogById(Long blogId);
    BlogResponse updateBlogById(Long blogId, UpdateBlogRequest updateBlogRequest);
    DeleteBlogResponse deleteBlogById(Long blogId);
}
