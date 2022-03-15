package com.study.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoUpdateDTO{
    private String id;
    private String title;
    private boolean done;
    private String updateTitle;
}
