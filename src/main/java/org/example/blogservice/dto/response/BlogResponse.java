package org.example.blogservice.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
public class BlogResponse implements Serializable {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private Long userId;
}
