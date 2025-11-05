package com.example.Backend_web.repository;

import com.example.Backend_web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String UserName);

    Optional<User> findByUsername(String UserName);
}
