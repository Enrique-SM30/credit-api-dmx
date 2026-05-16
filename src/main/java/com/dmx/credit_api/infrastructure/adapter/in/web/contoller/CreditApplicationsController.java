package com.dmx.credit_api.infrastructure.adapter.in.web.contoller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/credit-applications")
public class CreditApplicationsController {
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
