package com.su.springcloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.su.springcloud.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_fallbackMethod",commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
})
public class OrderController {
    @Resource
    private OrderService orderService;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){

        String result = orderService.paymentInfo_OK(id);
        return result;
    }


    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand
//    @HystrixCommand(fallbackMethod = "paymentInfo_TIMEOUTFallbackMethod",commandProperties = {
 //           @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
 //   })
    public String paymentInfo_TIMEOUT(@PathVariable("id")Integer id){
        String result = orderService.paymentInfo_TIMEOUT(id);
        return result;
    }
    public String paymentInfo_TIMEOUTFallbackMethod(@PathVariable("id")Integer id){
        return "80服务器繁忙，超时或运行出错";
    }
    //全局fallback
    public String payment_Global_fallbackMethod(){
        return "Global异常处理信息，请稍后再试";
    }
}
