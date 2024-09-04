package com.wanted.gold.user.repository;

import com.wanted.gold.user.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
