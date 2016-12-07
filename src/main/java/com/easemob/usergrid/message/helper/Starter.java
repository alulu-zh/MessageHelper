package com.easemob.usergrid.message.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by zhouhu on 1/10/2016.
 */
@SpringBootApplication
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
        System.out.println("zhouhu after run");
    }
}
