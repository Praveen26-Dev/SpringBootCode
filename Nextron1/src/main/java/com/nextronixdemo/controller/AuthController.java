package com.nextronixdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextronixdemo.dto.GoogleLoginRequest;
import com.nextronixdemo.dto.LoginRequest;
import com.nextronixdemo.dto.RegisterRequest;
import com.nextronixdemo.service.AuthService;
import com.nextronixdemo.service.GoogleAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
public class AuthController {
 
	@Autowired
	private AuthService authService;
	
	@Autowired
	private GoogleAuthService googleAuthService;
	@PostMapping("/register")
	public String regis(@RequestBody RegisterRequest req) {
		return authService.register(req);
	}
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
    	return authService.login(req);
    }
    
    @PostMapping("/google")
    public String googleLogin(@RequestBody GoogleLoginRequest req) {
    	 return googleAuthService.loginWithGoogle(req.getIdToken());
    }
    

}
