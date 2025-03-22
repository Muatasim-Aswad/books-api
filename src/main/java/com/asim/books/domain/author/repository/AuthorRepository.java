package com.asim.books.domain.author.repository;

import com.asim.books.domain.author.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    boolean existsByAge(Integer age);

    boolean existsByName(String name);

    boolean existsByNameAndAge(String name, Integer age);
}