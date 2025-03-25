package com.sparta.eureka.client.auth.domain.user.repository;

import com.sparta.eureka.client.auth.domain.user.entity.User;
import com.sparta.eureka.client.auth.domain.user.repository.custom.UserRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

  Optional<User> findByUsername(String username);
}
