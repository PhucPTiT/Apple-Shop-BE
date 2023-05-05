package com.business.service;

import org.springframework.http.ResponseEntity;

import com.business.dto.UserDTO;

public interface IUserService {
		UserDTO save(UserDTO userDTO);
		ResponseEntity<String> login(String userName, String password);
}
