package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentForm {

    private int id;
    @NotBlank
    private String text;
    private int reportId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
