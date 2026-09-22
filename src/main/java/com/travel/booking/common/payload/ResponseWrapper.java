package com.travel.booking.common.payload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseWrapper {
    private ResponseWrapper() {
        /* This utility class should not be instantiated */
    }

    public static ResponseEntity<BaseResponse> created(String message, Object data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(constructSuccessResponse(message, data));
    }

    public static ResponseEntity<BaseResponse> ok(String message, Object data) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(constructSuccessResponse(message, data));
    }

    public static ResponseEntity<BaseResponse> badRequest(String errorMessage, Object error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(constructErrorResponse(errorMessage, error));
    }

    public static ResponseEntity<BaseResponse> notFound(String errorMessage, Object error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(constructErrorResponse(errorMessage, error));
    }

    public static ResponseEntity<BaseResponse> internalServerError(String errorMessage, Object error) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(constructErrorResponse(errorMessage, error));
    }

    private static BaseResponse constructSuccessResponse(String message, Object data) {
        return BaseResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    private static BaseResponse constructErrorResponse(String errorMessage, Object error) {
        return BaseResponse.builder()
                .success(false)
                .message(errorMessage)
                .errors(error)
                .build();
    }
}
