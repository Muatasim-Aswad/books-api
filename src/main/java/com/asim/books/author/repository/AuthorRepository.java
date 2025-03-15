package com.asim.books.author.repository;

import com.asim.books.author.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByAge(Integer age);

    boolean existsByName(String name);
}