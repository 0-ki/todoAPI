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
        // Entity 가져옴 -> DTO로 변환 -> ResponseDTO로 변환 -> ResponseEntity 통해서 응답
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

            // TodoEntity로 반환
            TodoEntity entity = TodoDTO.toEntity(dto);

            //id를 null로 초기화. 생성시는 id가 없어야 한다
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


    // JPARepository 상속까지만 배운 후 내 생각대로 CRUD 구현 연습하기.
    // TODO 구현시 생략된 부분 - NullPointerException 등, 예외 처리가 필요함 // DB 요청전 유효성 검사 , 요청에 대한 검사 등
    // 1. Create
    // ReqBody로 TodoDTO를 받는다. 이후 성공 여부와 PK를 응답한다.
    @PostMapping("/testCreate")
    public ResponseEntity<?> testPost(@RequestBody TodoDTO todoDTO) {
        String postPK = todoService.testCreatePost(todoDTO);
        List<String> list = new ArrayList<>();
        list.add(postPK);
        ResponseDTO<String> res = ResponseDTO.<String>builder().data(list).result("success").build();
        return ResponseEntity.ok().body(res);
    }

    // 2. Read
    // 글의 제목을 경로 변수(@PathVariable)로 받아서 Todo객체를 ResBody 응답한다.
    @GetMapping("/testRead/{title}")
    public ResponseEntity<?> testReadPost(@PathVariable (required = false) String title) {
        TodoDTO todoDTO = todoService.testRead(title);
        return ResponseEntity.ok().body(todoDTO);
    }

    // 3. Update - 테스트시 Patch로 수행
    // title을 수정한다. // 되기는 하지만 매우 비정상적인 형태임.
    @PatchMapping("/testUpdate")
    public ResponseEntity<?> testUpdate(@RequestBody TodoUpdateDTO todoUpdateDTO) {
        TodoDTO todoDTO = todoService.testUpdate(todoUpdateDTO);
        return ResponseEntity.ok().body(todoDTO);
    }

    // 4. Delete
    // title을 받아 삭제 // 삭제 여부를 보장해야 한다. 추후 영속성이나 Optional 공부 후 구현
    @DeleteMapping("/testDelete/{title}")
    public void testDelete(@PathVariable (required = true) String title) {
        todoService.testDelete(title);
    }

}
