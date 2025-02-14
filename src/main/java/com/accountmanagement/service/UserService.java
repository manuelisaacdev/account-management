package com.accountmanagement.service;

import com.accountmanagement.dto.*;
import com.accountmanagement.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends AbstractService<User, Long> {
    User create(UserDTO userDTO);
    User update(Long userId, UpdateUserDTO updateUserDTO);
    User updatePassword(Long userId, UpdateUserPasswordDTO updateUserPasswordDTO);
    User updateProfilePhoto(Long userId, MultipartFile multipartFile);
}
