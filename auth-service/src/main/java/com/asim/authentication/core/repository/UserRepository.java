package com.asim.authentication.core.repository;

import com.asim.authentication.core.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName (String name);

    Optional<User> findByName (String name);
}
