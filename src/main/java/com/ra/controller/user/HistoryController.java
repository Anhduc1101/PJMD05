package com.ra.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class HistoryController {
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(){
        return null;
    }
}
