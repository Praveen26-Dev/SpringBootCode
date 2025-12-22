package com.nextronixdemo.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nextronixdemo.model.User;
import com.nextronixdemo.repository.UserRepository;
import com.nextronixdemo.security.JwtUtil;

@Service
public class GoogleAuthService {
	
 @Autowired
 private UserRepository userRepository;
 
 @Autowired
 private JwtUtil jwtUtil;
 @Autowired 
 private PasswordEncoder passwordEncoder;
 @Value("${google.client-id}")
 private String googleClientId;
 public String loginWithGoogle(String idTokenString) {
	 GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                                      new NetHttpTransport(),
                                      GsonFactory.getDefaultInstance()
)
			 .setAudience(Collections.singletonList(googleClientId))
//			 .setIssuer("https://accounts.google.com")
			 .build();
	
	 System.out.println("Id Token: "+idTokenString);
	 
	 GoogleIdToken idToken;
	 try {
		 idToken = verifier.verify(idTokenString);
	 }catch(Exception e) {
		 throw new RuntimeException("Invalid Google token");
		 
	 }
	 if (idToken == null) {
         throw new RuntimeException("Google ID token verification failed (idToken is null)");
     }
	 GoogleIdToken.Payload payload = idToken.getPayload();
	 String email = payload.getEmail();
	 boolean emailVarified = Boolean.TRUE.equals(payload.getEmailVerified());
	 if(!emailVarified) {
		 throw new RuntimeException("Email is  not varified");
		 
	 }
	 
	 User user = userRepository.findByEmail(email).orElseGet(()->{
		  User newUser = User.builder().
				  name("Praveen")
					.email(email)
					.password(passwordEncoder.encode("Google_Login"))
					.build();
	 
	 return userRepository.save(newUser);
	 });
	 return jwtUtil.generateToken(user.getEmail());
	}
}

