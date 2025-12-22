package com.nextronixdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nextronixdemo.dto.LoginRequest;
import com.nextronixdemo.dto.RegisterRequest;
import com.nextronixdemo.model.User;
import com.nextronixdemo.repository.UserRepository;
import com.nextronixdemo.security.JwtUtil;

@Service
public class AuthService {

	@Autowired
    private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
	
	public String register(RegisterRequest req) {
		
		if(userRepository.existsByEmail(req.getEmail())) {
			return "Email already Registered";
		}
		
		User user = User.builder()
				.name(req.getName())
				.email(req.getEmail())
				.password(passwordEncoder.encode(req.getPassword()))
				.build();
		
		userRepository.save(user);
	    return "User registered";
	}
	public String login(LoginRequest req) {

	    User user = userRepository.findByEmail(req.getEmail())
	            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

	    if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
	        throw new RuntimeException("Invalid credentials");
	    }

	    return jwtUtil.generateToken(user.getEmail());
	}
	
}

