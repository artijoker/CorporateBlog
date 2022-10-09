package com.example.blog.domain.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import com.example.blog.domain.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends CrudRepository<Account, Integer> {
    List<Account> findAccountsByIsBannedTrue();

    Optional<Account> findAccountByEmail(String email);
    Optional<Account> findAccountByLogin(String login);


    List<Account> findAll();

}
