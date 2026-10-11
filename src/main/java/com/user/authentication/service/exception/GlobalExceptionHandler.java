package com.user.authentication.service.exception;

import com.user.authentication.service.dto.MessageError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    ResponseEntity<MessageError> response;

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<MessageError> handleException(UserAlreadyExistsException e) {
        MessageError errorDetail = MessageError.builder().mensaje(e.getMessage()).build();
        response = new ResponseEntity<>(errorDetail, HttpStatus.CONFLICT);

        String message = e.getMessage();
        log.error(message != null ? message : "No se logro obtener el mensajes de excepcion correspondiente");
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        Map<String, Object> bodyResponse = new HashMap<>();
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        bodyResponse.put("mensaje", errors);

        return new ResponseEntity<>(bodyResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(JwtInvalidExpiredException.class)
    public ResponseEntity<MessageError> handleException(JwtInvalidExpiredException e) {
        MessageError errorDetail = MessageError.builder().mensaje(e.getMessage()).build();
        response = new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);

        return response;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageError> handleException(RuntimeException e) {

        log.info("Error: {}", e.getMessage());
        MessageError errorDetail = MessageError.builder().mensaje("No fue posible completar la solicitud. Intente nuevamente en unos momentos.").build();
        response = new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);

        return response;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<MessageError> handleException(UnauthorizedException e) {

        log.info("Error: {}", e.getMessage());
        MessageError errorDetail = MessageError.builder().mensaje(e.getMessage()).build();
        response = new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);

        return response;
    }

}
