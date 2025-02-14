package com.accountmanagement.dto;

import com.accountmanagement.model.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"accessToken", "refreshToken", "user"})
public record AuthenticationResponse(User user, String accessToken, String refreshToken) {
}
