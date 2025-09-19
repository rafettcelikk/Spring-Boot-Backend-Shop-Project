package com.rafetcelik.dream_shops.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafetcelik.dream_shops.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByName(String role);

}
