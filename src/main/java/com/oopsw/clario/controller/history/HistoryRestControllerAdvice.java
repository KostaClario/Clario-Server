package com.oopsw.clario.controller.history;

import com.oopsw.clario.exception.SaveFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

@RestControllerAdvice
@RequiredArgsConstructor
public class HistoryAdvice {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> historyNullException(NullPointerException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NULL발생" + e.getMessage());
    }
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<String> historyResourceAccessException(ResourceAccessException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(SaveFailedException.class)
    public ResponseEntity<String> historySaveFailException(SaveFailedException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패" + e.getMessage());
    }
}
