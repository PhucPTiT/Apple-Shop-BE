package com.business.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.converter.UserConverter;
import com.business.dto.UserDTO;
import com.business.entity.UserEntity;
import com.business.repository.UserRepository;
import com.business.service.IUserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserConverter userConverter;


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
	public String login(String userName, String password) {
		UserEntity userEntity = userRepository.findByUsername(userName);
	    if(userEntity == null) {
	        throw new RuntimeException("Tên người dùng không tồn tại");
	    }
	    if(!BCrypt.checkpw(password, userEntity.getPassword())) {
	        throw new RuntimeException("Mật khẩu không chính xác");
	    }
	    
	    //Create TOKEN
	    
	    Map<String, Object> claims = new HashMap<>();
	    claims.put("sub", userEntity.getId()); // ID của user
	    claims.put("role", userEntity.getRole()); // Vai trò của user
	    
	    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	    Date now = new Date();
	    String token = Jwts.builder()
    		.setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + 3600 ))
            .signWith(key)
            .compact();
	   
	    
	    return token;
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
