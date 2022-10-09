package com.example.blog.domain.repositories;

import com.example.blog.domain.entities.JwtToken;
import org.springframework.data.repository.CrudRepository;

public interface IJwtTokenRepository extends CrudRepository<JwtToken, Integer> {
}
