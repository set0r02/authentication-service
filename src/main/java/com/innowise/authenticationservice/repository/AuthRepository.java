package com.innowise.authenticationservice.repository;

import com.innowise.authenticationservice.model.AuthUser;
import com.innowise.authenticationservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthUser,Long> {

    Optional<AuthUser> findByLogin(String login);

    boolean existsAuthUserByLogin(String login);

    boolean existsByRole(Role role);
}
