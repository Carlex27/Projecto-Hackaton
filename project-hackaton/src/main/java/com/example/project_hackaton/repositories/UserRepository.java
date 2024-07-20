package com.example.project_hackaton.repositories;

import com.example.project_hackaton.entities.Rol;
import com.example.project_hackaton.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPassword(String password);
    List<User> findAllByRole(Rol role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteByUsername(String username);
}
