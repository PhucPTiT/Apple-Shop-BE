package com.business.service.impl;

import javax.annotation.PostConstruct;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.business.converter.UserConverter;
import com.business.dto.UserDTO;
import com.business.entity.UserEntity;
import com.business.repository.UserRepository;
import com.business.service.IUserService;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserConverter userConverter;

	@Autowired
	private JwtService jwtService;

	@Override
	public UserDTO save(UserDTO userDTO) {
		UserEntity userEntity = new UserEntity();
		UserEntity existingUser = userRepository.findByUsername(userDTO.getUserName());
		if(existingUser != null) {
			throw new RuntimeException("Username đã được sử dụng");
		}
		String hashedPassWord = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
		userDTO.setPassword(hashedPassWord);
		userEntity = userRepository.save(userConverter.toEntity(userDTO));
		return userConverter.toDTO(userEntity);
	}

	@Override
	public ResponseEntity<String> login(String userName, String password) {
		UserEntity userEntity = userRepository.findByUsername(userName);
	    if(userEntity == null) {
	        throw new RuntimeException("Tên người dùng không tồn tại");
	    }
	    if(!BCrypt.checkpw(password, userEntity.getPassword())) {
	        throw new RuntimeException("Mật khẩu không chính xác");
	    } 
	    HttpStatus httpStatus = HttpStatus.OK;
	    String token = jwtService.generateTokenLogin(userEntity.getUsername(), userEntity.getRole());
	    return ResponseEntity.status(httpStatus).body(token);
	}
	@PostConstruct
    public void createAdminAccount() {
        UserEntity existingAdmin = userRepository.findByUsername("admin");
        if (existingAdmin == null) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
            admin.setRole(1);
            userRepository.save(admin);
        }
    }
	
	
	
}
