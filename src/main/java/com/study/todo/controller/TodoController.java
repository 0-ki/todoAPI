package com.study.todo.controller;

import com.study.todo.dto.ResponseDTO;
import com.study.todo.dto.TestRequestBodyDTO;
import com.study.todo.dto.TodoDTO;
import com.study.todo.dto.TodoUpdateDTO;
import com.study.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
