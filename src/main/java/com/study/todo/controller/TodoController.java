package com.study.todo.controller;

import com.study.todo.dto.ResponseDTO;
import com.study.todo.dto.TestRequestBodyDTO;
import com.study.todo.dto.TodoDTO;
import com.study.todo.dto.TodoUpdateDTO;
import com.study.todo.model.TodoEntity;
import com.study.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/testResEntity")
    public ResponseEntity<?> testTodo () {
        String str = todoService.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> res = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
        TodoEntity entity = TodoDTO.toEntity(dto);
        String temporaryUserId = "temporary-user";
        entity.setUserId(temporaryUserId);
        try {
            List<TodoEntity> entities = todoService.delete( entity);
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO res = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(res);
        } catch ( Exception e) {
            ResponseDTO errorRes = ResponseDTO.<TodoDTO>builder().result(e.getMessage()).build();
            return ResponseEntity.badRequest().body(errorRes);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
        String temporaryUserId = "temporary-user";
        TodoEntity entity = TodoDTO.toEntity( dto);
        entity.setUserId(temporaryUserId);
        List<TodoEntity> entities = todoService.update( entity);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> res = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@RequestBody(required = false) TodoDTO dto) {
        // Entity ????????? -> DTO??? ?????? -> ResponseDTO??? ?????? -> ResponseEntity ????????? ??????
        String temporaryUserId = "temporary-user";
        List<TodoEntity> entities = todoService.retrieve( temporaryUserId);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO res = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(res);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";

            // TodoEntity??? ??????
            TodoEntity entity = TodoDTO.toEntity(dto);

            //id??? null??? ?????????. ???????????? id??? ????????? ??????
            entity.setId(null);
            entity.setUserId(temporaryUserId);

            List<TodoEntity> entities = todoService.create(entity);

            // https://dpdpwl.tistory.com/81
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> res = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(res);

        }catch (Exception e){
            ResponseDTO<TodoDTO> res = ResponseDTO.<TodoDTO>builder().result(e.getMessage()).build();
            return ResponseEntity.badRequest().body(res);
        }
    }


    // JPARepository ??????????????? ?????? ??? ??? ???????????? CRUD ?????? ????????????.
    // TODO ????????? ????????? ?????? - NullPointerException ???, ?????? ????????? ????????? // DB ????????? ????????? ?????? , ????????? ?????? ?????? ???
    // 1. Create
    // ReqBody??? TodoDTO??? ?????????. ?????? ?????? ????????? PK??? ????????????.
    @PostMapping("/testCreate")
    public ResponseEntity<?> testPost(@RequestBody TodoDTO todoDTO) {
        String postPK = todoService.testCreatePost(todoDTO);
        List<String> list = new ArrayList<>();
        list.add(postPK);
        ResponseDTO<String> res = ResponseDTO.<String>builder().data(list).result("success").build();
        return ResponseEntity.ok().body(res);
    }

    // 2. Read
    // ?????? ????????? ?????? ??????(@PathVariable)??? ????????? Todo????????? ResBody ????????????.
    @GetMapping("/testRead/{title}")
    public ResponseEntity<?> testReadPost(@PathVariable (required = false) String title) {
        TodoDTO todoDTO = todoService.testRead(title);
        return ResponseEntity.ok().body(todoDTO);
    }

    // 3. Update - ???????????? Patch??? ??????
    // title??? ????????????. // ????????? ????????? ?????? ??????????????? ?????????.
    @PatchMapping("/testUpdate")
    public ResponseEntity<?> testUpdate(@RequestBody TodoUpdateDTO todoUpdateDTO) {
        TodoDTO todoDTO = todoService.testUpdate(todoUpdateDTO);
        return ResponseEntity.ok().body(todoDTO);
    }

    // 4. Delete
    // title??? ?????? ?????? // ?????? ????????? ???????????? ??????. ?????? ??????????????? Optional ?????? ??? ??????
    @DeleteMapping("/testDelete/{title}")
    public void testDelete(@PathVariable (required = true) String title) {
        todoService.testDelete(title);
    }

}
