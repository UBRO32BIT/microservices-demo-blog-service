package org.example.blogservice.service;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.example.blogservice.dto.response.DeleteBlogResponse;
import org.example.blogservice.exception.BlogNotFoundException;
import org.example.blogservice.dto.request.CreateBlogRequest;
import org.example.blogservice.dto.request.UpdateBlogRequest;
import org.example.blogservice.dto.response.BlogResponse;
import org.example.blogservice.exception.ApiClientException;
import org.example.blogservice.model.Blog;
import org.example.blogservice.repository.BlogRepository;
import org.example.blogservice.service.client.UserClient;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = {"Blog"})
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final UserClient userClient;

    @Override
    @Retryable(retryFor = RetryableException.class, maxAttempts = 2, backoff = @Backoff(delay = 1000))
    public BlogResponse createBlog(CreateBlogRequest createBlogRequest) {
        try {
            userClient.getUserById(createBlogRequest.getUserId());
        }
        catch (Exception exception) {
            if (exception instanceof FeignException) {
                throw new ApiClientException(exception.getMessage());
            }
            throw exception;
        }
        Blog newBlog = new Blog(
                0L,
                createBlogRequest.getTitle(),
                null,
                createBlogRequest.getContent(),
                createBlogRequest.getUserId()
        );
        Blog.builder()
                .title(createBlogRequest.getTitle())
                .content(createBlogRequest.getContent())
                .userId(createBlogRequest.getUserId())
                .build();
        Blog result = blogRepository.save(newBlog);
        return BlogResponse.builder()
                .id(result.getId())
                .title(result.getTitle())
                .content(result.getContent())
                .userId(result.getUserId())
                .build();
    }

    @Override
    public List<BlogResponse> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs.stream()
                .map(this::toBlogResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(key = "#p0")
    public BlogResponse getBlogById(Long blogId) {
        Blog blog = this.getBlogModelById(blogId);
        return this.toBlogResponse(blog);
    }

    @Override
    @CachePut(key = "#p0")
    public BlogResponse updateBlogById(Long blogId, UpdateBlogRequest updateBlogRequest) {
        Blog blog = getBlogModelById(blogId);

        blog.setTitle(updateBlogRequest.getTitle());
        blog.setContent(updateBlogRequest.getContent());

        Blog newBlog = blogRepository.save(blog);
        return this.toBlogResponse(newBlog);
    }

    @Override
    @CacheEvict(key = "#p0")
    public DeleteBlogResponse deleteBlogById(Long blogId) {
        Blog blog = getBlogModelById(blogId);
        blogRepository.delete(blog);
        return new DeleteBlogResponse("Blog deleted");
    }

    public Blog getBlogModelById(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog not found"));
    }

    private BlogResponse toBlogResponse(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .slug(blog.getSlug())
                .content(blog.getContent())
                .userId(blog.getUserId())
                .build();
    }
}
