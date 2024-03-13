package com.spring.mallapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.mallapi.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long>{

}
