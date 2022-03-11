package com.study.todo.controller;

import com.study.todo.dto.ResponseDTO;
import com.study.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
