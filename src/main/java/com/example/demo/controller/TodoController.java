package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        System.out.println("test Controller 진입");
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list). build();
        return ResponseEntity.ok().body(response);
    }

    //사용자 아이디에 해당하는 todo entity의 객체 리스트를 가져온다.
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(
            @AuthenticationPrincipal String userId
    ) {
//        String temporaryUserId = "temporary-user";
        System.out.println("userId -> "+userId);
        System.out.println("todo get요청 작동완료");
        List<TodoEntity> entities = service.retrieve(userId);

        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    //todo 생성
    @PostMapping
    public ResponseEntity<?> createTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto
    ){
        try{
//            String temporaryUserId = "temporary-user"; //security 적용 전

            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setId(null);

            //임시 사용자 아이디 설정
            entity.setUserId(userId);

            //
            System.out.println(userId);
            //

            List<TodoEntity> entities = service.create(entity);

            //자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환함.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String error = e.getMessage();

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    //todo 수정
    @PutMapping
    public ResponseEntity<?> updateTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto){
//        String temporaryUserId = "temporary-user"; //security 적용 전

        TodoEntity entity = TodoDTO.toEntity(dto);

        entity.setUserId(userId);

        List<TodoEntity> entities = service.update(entity);

        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    //삭제
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto){
        try {
//            String temporaryUserId = "temporary-user"; //security 적용 전

            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setUserId(userId);

            List<TodoEntity> entities = service.delete(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return  ResponseEntity.ok().body(response);
        }catch (Exception e){
            String error = e.getMessage();

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
