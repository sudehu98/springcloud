package com.su.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
    //可以正常运行的
    public String paymentInfo_OK(Integer id){
        return "线程池： "+Thread.currentThread().getName()+"  paymentInfo_OK   ,id:   "+id+"\t"+"哈哈";
    }
    @HystrixCommand(fallbackMethod = "paymentInfo_TIMEOUTHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    //会耗时3秒
    public String paymentInfo_TIMEOUT(Integer id){
        int timeNum = 13;
        try{
            TimeUnit.SECONDS.sleep(timeNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池： "+Thread.currentThread().getName()+"  paymentInfo_TIMEOUT   ,id:   "+id+"\t"+"耗时"+timeNum+"秒钟";
    }
    public String paymentInfo_TIMEOUTHandler(Integer id){
        return "线程池： "+Thread.currentThread().getName()+" 8001反应超时或运行报错  "+id+"\t"+"***超时了***";
    }
    //=========================服务熔断======================

    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),//请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60")//失败率达到什么时跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id")Integer id){
        if(id < 0){
            throw new RuntimeException("************id不能为负数");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName()+"\t"+"调用成功，流水号：  "+serialNumber;
    }
    public String paymentCircuitBreaker_fallback(Integer id){
        return "id不能为负数，请稍后重试   id：  "+id;
    }
}
