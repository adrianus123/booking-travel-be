package com.travel.booking.domain;

import com.travel.booking.common.payload.BaseResponse;
import com.travel.booking.common.payload.ResponseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/health-check")
    public ResponseEntity<BaseResponse> healthCheck() {
        return ResponseWrapper.ok("Healthy", null);
    }
}
