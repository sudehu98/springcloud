package com.su.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class OrderFallbackService implements OrderService{
    @Override
    public String paymentInfo_OK(Integer id) {
        return "-------------OrderFallbackService--paymentInfo_OK";
    }

    @Override
    public String paymentInfo_TIMEOUT(Integer id) {
        return "--------------OrderFallbackService--paymentInfo_TIMEOUT";
    }
}
