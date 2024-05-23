package org.example.blogservice.service.client;

import jakarta.validation.Valid;
import org.example.blogservice.dto.client.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-CLIENT", url = "${application.config.user-client-url}")
public interface UserClient {
    @GetMapping(value = "/{userId}")
    User getUserById(@PathVariable Long userId);

    @PostMapping(value = "/check-auth")
    User checkAuth(@Valid @RequestBody CheckAuthRequest request);
}
