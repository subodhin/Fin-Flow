package com.finflow.repository;

import com.finflow.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  //  boolean existsByEmail(@Email @NotBlank String email);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
