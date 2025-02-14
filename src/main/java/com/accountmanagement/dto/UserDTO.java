package com.accountmanagement.dto;

import com.accountmanagement.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank(message = "User.name.notblank")
        @Size(max = 100, message = "User.name.size")
        String name,

        @Size(max = 20, message = "User.phone.size")
        @NotBlank(message = "User.phone.notblank")
        String phone,

        @NotNull(message = "User.role.notnull")
        Role role,

        @Email(message = "User.email.email")
        @NotBlank(message = "User.email.notblank")
        @Size(max = 100, message = "User.email.size")
        String email,

        @NotBlank(message = "User.password.notblank")
        @Size(min = 8, max = 16, message = "User.password.size")
        String password
) {
}
