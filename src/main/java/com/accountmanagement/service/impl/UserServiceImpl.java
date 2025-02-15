package com.accountmanagement.service.impl;

import com.accountmanagement.dto.UpdateUserDTO;
import com.accountmanagement.dto.UpdateUserPasswordDTO;
import com.accountmanagement.dto.UserDTO;
import com.accountmanagement.exception.AuthorizationException;
import com.accountmanagement.exception.DataNotFoundException;
import com.accountmanagement.exception.PasswordNotMatchException;
import com.accountmanagement.model.Role;
import com.accountmanagement.model.User;
import com.accountmanagement.repository.UserRepository;
import com.accountmanagement.service.JwtService;
import com.accountmanagement.service.StorageService;
import com.accountmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractServiceImpl<User, Long, UserRepository> implements UserService {
    private final JwtService jwtService;
    private final HttpServletRequest request;
    private final MessageSource messageSource;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, JwtService jwtService, HttpServletRequest request, MessageSource messageSource, StorageService storageService, PasswordEncoder passwordEncoder) {
        super(repository);
        this.jwtService = jwtService;
        this.request = request;
        this.messageSource = messageSource;
        this.storageService = storageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(Long userId) throws DataNotFoundException {
        return super.getRepository().findById(userId)
        .orElseThrow(() -> new DataNotFoundException(messageSource.getMessage("messages.user.not-found", new Object[]{userId}, request.getLocale())));
    }

    @Override
    public User create(UserDTO userDTO, Optional<String> authorization) {
        if (userDTO.role().equals(Role.CLIENT) || authorization.isEmpty()) {
            if (!userDTO.role().equals(Role.CLIENT)) {
                throw new AuthorizationException("Authorization required");
            }
            return create(userDTO);
        }

        JwtService.JWTPayload jwtPayload = jwtService.getJWTPayload(authorization.get());
        if (userDTO.role().equals(Role.ADMIN) && !jwtPayload.role().equals(Role.ROOT)) {
            throw new AuthorizationException("Authorization required");
        }

        return create(userDTO);
    }

    private User create(UserDTO userDTO) {
        return super.save(User.builder()
        .name(userDTO.name())
        .phone(userDTO.phone())
        .email(userDTO.email())
        .role(userDTO.role())
        .password(passwordEncoder.encode(userDTO.password()))
        .build());
    }

    @Override
    public User update(Long userId, UpdateUserDTO updateUserDTO, String authorization) {
        User user = findById(userId);

        if (user.getRole().equals(Role.CLIENT)) {
            return update(updateUserDTO, user);
        }

        JwtService.JWTPayload jwtPayload = jwtService.getJWTPayload(authorization);

        if (user.getRole().equals(Role.ROOT) && !user.getId().equals(jwtPayload.userId())) {
            throw new AuthorizationException("Authorization required");
        }

        if (user.getRole().equals(Role.ADMIN) &&
            !(user.getId().equals(jwtPayload.userId()) || jwtPayload.role().equals(Role.ROOT))) {
            throw new AuthorizationException("Authorization required");
        }

        return update(updateUserDTO, user);
    }

    private User update(UpdateUserDTO updateUserDTO, User user) {
        return super.save(user.toBuilder()
        .name(updateUserDTO.name())
        .phone(updateUserDTO.phone())
        .email(updateUserDTO.email())
        .role(updateUserDTO.role())
        .build());
    }

    @Override
    public User updatePassword(Long userId, UpdateUserPasswordDTO updateUserPasswordDTO) {
        User user = super.findById(userId);
        if (!passwordEncoder.matches(updateUserPasswordDTO.currentPassword(), user.getPassword())) {
            throw new PasswordNotMatchException(messageSource.getMessage("messages.user.password.invalid-password", null, request.getLocale()));
        }

        return super.save(user.toBuilder()
        .password(passwordEncoder.encode(updateUserPasswordDTO.newPassword()))
        .build());
    }

    @Override
    public User updateProfilePhoto(Long userId, MultipartFile multipartFile) {
        User user = super.findById(userId);
        String oldProfilePhoto = user.getProfilePhoto();
        user.setProfilePhoto(storageService.store(multipartFile).toFile().getName());
        storageService.delete(oldProfilePhoto);
        return super.save(user);
    }

    @Override
    public void delete(Long userId, String authorization) {
        User user = super.findById(userId);
        if (user.getRole().equals(Role.ROOT)) {
            throw new AuthorizationException("Authorization required");
        }

        JwtService.JWTPayload jwtPayload = jwtService.getJWTPayload(authorization);

        if (user.getRole().equals(Role.ADMIN) && !(jwtPayload.userId().equals(user.getId()) || jwtPayload.role().equals(Role.ROOT))) {
            throw new AuthorizationException("Authorization required");
        }

        super.getRepository().delete(user);
        storageService.delete(user.getProfilePhoto());
    }
}
