package com.rafetcelik.dream_shops.controller;

import org.springframework.http.ResponseEntity;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafetcelik.dream_shops.request.LoginRequest;
import com.rafetcelik.dream_shops.response.ApiResponse;
import com.rafetcelik.dream_shops.response.JwtResponse;
import com.rafetcelik.dream_shops.security.jwt.JwtUtils;
import com.rafetcelik.dream_shops.security.user.ShopUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtUtils jwtUtils;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(
							request.getEmail(), request.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateToken(authentication);
			ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
			JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
			return ResponseEntity.ok(new ApiResponse("Giriş başarılı!", jwtResponse));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
		}
	}
}
