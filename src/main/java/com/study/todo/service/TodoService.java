package com.study.todo.service;

import com.study.todo.dto.ResponseDTO;
import com.study.todo.dto.TestRequestBodyDTO;
import com.study.todo.dto.TodoDTO;
import com.study.todo.dto.TodoUpdateDTO;
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

    // TodoDTO를 Insert 하고 해당 PK를 리턴한다.
    public String testCreatePost(TodoDTO todoDTO) {

        TodoEntity entity = TodoEntity.builder().userId(todoDTO.getId()).title(todoDTO.getTitle()).build();
        repository.save(entity);
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getId();
    }

    // title이 일치하는 TodoDTO를 리턴한다.
    public TodoDTO testRead(String title) {
        TodoEntity entity = repository.findByTitleLike(title);
        return TodoDTO.builder().title(entity.getTitle()).id(entity.getUserId()).done(entity.isDone()).build();
    }

    public TodoDTO testUpdate(TodoUpdateDTO todoUpdateDTO) {
        TodoEntity entity = repository.findByTitle(todoUpdateDTO.getTitle());
        entity.setTitle(todoUpdateDTO.getUpdateTitle());
        repository.save(entity);
        return TodoDTO.builder().title(entity.getTitle()).build();
    }

    public void testDelete(String title) {
        TodoEntity entity = repository.findByTitle( title);
        repository.delete(entity);
    }
}
