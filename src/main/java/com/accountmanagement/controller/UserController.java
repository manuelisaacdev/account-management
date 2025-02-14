package com.accountmanagement.controller;

import com.accountmanagement.dto.UpdateUserDTO;
import com.accountmanagement.dto.UpdateUserPasswordDTO;
import com.accountmanagement.dto.UserDTO;
import com.accountmanagement.model.User;
import com.accountmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String email,
        @RequestParam(defaultValue = "name") String orderBy,
        @RequestParam(defaultValue = "ASC") Direction direction
    ){
        return ResponseEntity.ok(userService.findAll(
            Example.of(User.builder()
            .name(name)
            .email(email)
            .build(),
            ExampleMatcher.matching()
        .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
        .withMatcher("email", ExampleMatcher.GenericPropertyMatcher::contains)
        .withIgnoreCase()
        ), orderBy, direction));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.findById(userId));
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<User>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "name") String orderBy,
            @RequestParam(defaultValue = "ASC") Direction direction
    ){
        return ResponseEntity.ok(userService.findAll(
        page, size,
        Example.of(User.builder()
            .name(name)
            .email(email)
            .build(),
        ExampleMatcher.matching()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("email", ExampleMatcher.GenericPropertyMatcher::contains)
            .withIgnoreCase()
        ), orderBy, direction));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid UserDTO userDTO, @RequestHeader Optional<String> authorization){
        return ResponseEntity.ok(userService.create(userDTO, authorization));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> update(@PathVariable Long userId, @RequestBody @Valid UpdateUserDTO updateUserDTO, @RequestHeader String authorization){
        return ResponseEntity.ok(userService.update(userId, updateUserDTO, authorization));
    }

    @PatchMapping("/password/{userId}")
    public ResponseEntity<User> updatePassword(@PathVariable Long userId, @RequestBody @Valid UpdateUserPasswordDTO updateUserPasswordDTO){
        return ResponseEntity.ok(userService.updatePassword(userId, updateUserPasswordDTO));
    }

    @PatchMapping("/profilePhoto/{userId}")
    public ResponseEntity<User> updateProfilePhoto(@PathVariable Long userId, @RequestParam MultipartFile profilePhoto){
        return ResponseEntity.ok(userService.updateProfilePhoto(userId, profilePhoto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @RequestHeader String authorization){
        userService.delete(userId, authorization);
        return ResponseEntity.noContent().build();
    }

}
