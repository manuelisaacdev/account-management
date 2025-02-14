package com.accountmanagement.service;

import com.accountmanagement.dto.*;
import com.accountmanagement.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService extends AbstractService<User, Long> {
    User create(UserDTO userDTO, Optional<String> authorization);
    User update(Long userId, UpdateUserDTO updateUserDTO, String authorization);
    User updatePassword(Long userId, UpdateUserPasswordDTO updateUserPasswordDTO);
    User updateProfilePhoto(Long userId, MultipartFile multipartFile);
    void delete(Long userId, String authorization);
}
