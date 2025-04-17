package com.userService.repo;

import com.userService.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users , String> {
    Optional<Users> findByEmailIgnoreCase(String email);
}
