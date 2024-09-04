package com.wanted.gold.user.repository;

import com.wanted.gold.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // 회원이름으로 회원 조회
    Optional<User> findByUsername(String username);
}
