package org.example.expert.domain.user.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.example.expert.config.security.UserDetailsImpl;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users")
    public void changePassword(@AuthenticationPrincipal UserDetailsImpl authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getUser().getId(), userChangePasswordRequest);
    }

    @GetMapping("/users/search-by-nickname")
    public ResponseEntity<UserResponse> getUserByNickname(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(userService.findByNickname(nickname));
    }
}
