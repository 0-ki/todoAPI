package com.study.todo.service;

import com.study.todo.model.TodoEntity;
import com.study.todo.persistence.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public String testService() {

        TodoEntity entity = TodoEntity.builder().title("1st item").build();
        repository.save(entity);
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }
}
