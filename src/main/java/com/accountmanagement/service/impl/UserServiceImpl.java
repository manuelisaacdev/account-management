package com.accountmanagement.service.impl;

import com.accountmanagement.dto.UpdateUserDTO;
import com.accountmanagement.dto.UpdateUserPasswordDTO;
import com.accountmanagement.dto.UserDTO;
import com.accountmanagement.exception.DataNotFoundException;
import com.accountmanagement.exception.PasswordNotMatchException;
import com.accountmanagement.model.User;
import com.accountmanagement.repository.UserRepository;
import com.accountmanagement.service.StorageService;
import com.accountmanagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl extends AbstractServiceImpl<User, Long, UserRepository> implements UserService {
    private final HttpServletRequest request;
    private final MessageSource messageSource;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, HttpServletRequest request, MessageSource messageSource, StorageService storageService, PasswordEncoder passwordEncoder) {
        super(repository);
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
    public User create(UserDTO userDTO) {
        return super.save(User.builder()
        .name(userDTO.name())
        .phone(userDTO.phone())
        .email(userDTO.email())
        .role(userDTO.role())
        .password(passwordEncoder.encode(userDTO.password()))
        .build());
    }

    @Override
    public User update(Long userId, UpdateUserDTO updateUserDTO) {
        return super.save(super.findById(userId).toBuilder()
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
}
