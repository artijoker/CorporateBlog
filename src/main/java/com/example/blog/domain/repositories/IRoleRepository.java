package com.example.blog.domain.repositories;

import com.example.blog.domain.entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRoleRepository  extends CrudRepository<Role, Integer> {

    Optional<Role> findRoleByName(String name);

    Role getRoleByName(String name);
    Role getRoleById(Integer id);
}
