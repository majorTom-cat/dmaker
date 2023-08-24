package com.fastcampus.programming.dmaker.exception;

import com.fastcampus.programming.dmaker.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.INTERNAL_SERVER_ERROR;
import static com.fastcampus.programming.dmaker.exception.DMakerErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {

    //@ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(DMakerException e, HttpServletRequest request) {

        log.error("errorCode: {}, url: {}, message: {}",
                e.getDMakerErrorCode(),
                request.getRequestURI(),
                e.getDetailMessage()
        );

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url: {}, message: {}",
                request.getRequestURI(),
                e.getMessage()
        );

        return DMakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST) // Post로 요청해야 하나 Delete로 요청 하는 등
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    // 모든 에러를 예상할 수 없기 때문에
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url: {}, message: {}",
                request.getRequestURI(),
                e.getMessage()
        );

        return DMakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }

}
