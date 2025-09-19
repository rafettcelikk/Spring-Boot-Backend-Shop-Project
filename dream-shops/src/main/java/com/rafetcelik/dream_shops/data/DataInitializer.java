package com.rafetcelik.dream_shops.data;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rafetcelik.dream_shops.model.Role;
import com.rafetcelik.dream_shops.model.User;
import com.rafetcelik.dream_shops.repository.RoleRepository;
import com.rafetcelik.dream_shops.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent>{
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
		createDefaultRoleIfNotExists(defaultRoles);
		createDefaultUserIfNotExists();
		createDefaultAdminIfNotExists();
	}
	
	private void createDefaultUserIfNotExists() {
		Role userRole = roleRepository.findByName("ROLE_USER").get();
		for(int i = 1; i <= 5; i++) {
			String defaultEmail = "user" + i + "@email.com";
			if(userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("Kullanıcı");
			user.setLastName("Kullanıcı" + i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
			System.out.println("Varsayılan kullanıcı " + i + " başarıyla oluşturuldu!");
		}
	}
	
	private void createDefaultAdminIfNotExists() {
		Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
		for(int i = 1; i <= 2; i++) {
			String defaultEmail = "admin" + i + "@email.com";
			if(userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("Admin");
			user.setLastName("Admin" + i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(adminRole));
			userRepository.save(user);
			System.out.println("Varsayılan admin " + i + " başarıyla oluşturuldu!");
		}
	}
	
	private void createDefaultRoleIfNotExists(Set<String> roles) {
		roles.stream()
				.filter(role -> roleRepository.findByName(role).isEmpty())
				.map(Role :: new).forEach(roleRepository :: save);
	}
}
