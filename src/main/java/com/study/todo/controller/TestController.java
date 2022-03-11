package com.study.todo.controller;

import com.study.todo.dto.ResponseDTO;
import com.study.todo.dto.TestRequestBodyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController // = @ResponseBody + @Controller
@RequestMapping("test")
public class TestController {

    @GetMapping("/testResEntity")
    public ResponseEntity<?> testControllerResEntity() {
        List<String> list = new ArrayList<>();
        list.add("hey, this is Response Entity. ");
        ResponseDTO<String> res = ResponseDTO.<String>builder().data(list).build();
//        return ResponseEntity.badRequest().body(res);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/testResBody")
    public ResponseDTO<String> testControllerResBody() {
        List<String> list = new ArrayList<>();
        list.add("Hello , receive my ResponseDTO");
        ResponseDTO<String> res = ResponseDTO.<String>builder().data(list).build();
        return res;
    }

    @GetMapping("/testReqBody")
    public String testControllerReqBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
        return "Hello Req Body, id :" + testRequestBodyDTO.getId() + "msg :" + testRequestBodyDTO.getMsg();
    }

    @GetMapping("/testReqParam") // 127.0.0.1:8080/test/testReqParam?id=123
    public String testControllerReqParam(@RequestParam(required = false) int id) {
        return "okok "+ id;
    }

    @GetMapping("/{id}") // 127.0.0.1:8080/test/123123
    public String testControllerWithPath(@PathVariable(required = false) int id) {
        return "hello " + id;
    }

    @GetMapping("/testGet")
    public String testGetController() {
        return "hi test Get";
    }
    @GetMapping
    public String testController() {
        return "hi";
    }
}
