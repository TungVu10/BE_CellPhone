package com.example.Backend_web.repository;

import com.example.Backend_web.entity.User;
import com.example.Backend_web.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String UserName);

    Optional<User> findByUsername(String UserName);

    List<User> findByRoles(Role role);

    //Tổng số khách hàng
    @Query("""
        SELECT COUNT(u)
        FROM User u
        JOIN u.roles r
        WHERE r = com.example.Backend_web.enums.Role.USER
""")
    long countUsers();

}
