package com.accountmanagement;

import com.accountmanagement.model.Role;
import com.accountmanagement.model.User;
import com.accountmanagement.service.StorageService;
import com.accountmanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AccountManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountManagementApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(StorageService storageService, UserService userService, PasswordEncoder passwordEncoder) {
		return args -> {
			storageService.init();
			if (userService.exists(Example.of(User.builder().role(Role.ROOT).build()))) return;

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.findAndRegisterModules();
			User root =  objectMapper.readValue(getClass().getResourceAsStream("/json/UserRoot.json"), User.class);
			userService.save(root.toBuilder().password(passwordEncoder.encode(root.getPassword())).build());
		};
	}

}
