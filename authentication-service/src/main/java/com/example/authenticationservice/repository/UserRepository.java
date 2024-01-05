package com.example.authenticationservice.repository;

import java.util.Optional;

import com.example.authenticationservice.model.Role;
import com.example.authenticationservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Since email is unique, we'll find users by email
    Optional<User> findByEmail(String email);
    User findByRole(Role role);
}
