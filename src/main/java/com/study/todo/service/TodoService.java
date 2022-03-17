package com.study.todo.service;

import com.study.todo.dto.ResponseDTO;
import com.study.todo.dto.TestRequestBodyDTO;
import com.study.todo.dto.TodoDTO;
import com.study.todo.dto.TodoUpdateDTO;
import com.study.todo.model.TodoEntity;
import com.study.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public List<TodoEntity> delete(final TodoEntity entity) {
        validate( entity);

        try {
            repository.delete(entity);
        } catch (Exception e) {
            log.error("error deleting entity", entity.getId(), e);
            // 컨트롤러로 e를 보낸다. DB 내부 로직 캡슐화를 하기 위해 e 대신 new Exception을 리턴한다.
            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        return retrieve( entity.getUserId());
    }

    public List<TodoEntity> update(final TodoEntity entity) {
        validate(entity);
        // https://hbase.tistory.com/212
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save( todo);
        });

        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId( userId);
    }

    public List<TodoEntity> create(final TodoEntity entity) {
        validate(entity);
        repository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

    private void validate(final TodoEntity entity) {
        //Validations
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }


    // 연습 코드
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
