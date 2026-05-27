package com.innowise.authenticationservice.repository;

import com.innowise.authenticationservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User,Long> {

    Optional<User> findByLogin(String login);

}
