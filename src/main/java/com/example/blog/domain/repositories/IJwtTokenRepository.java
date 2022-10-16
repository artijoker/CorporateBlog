package com.example.blog.domain.repositories;

import com.example.blog.domain.entities.JwtToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IJwtTokenRepository extends CrudRepository<JwtToken, Integer> {

    List<JwtToken> findJwtTokensByAccountId(int accountId);
}
