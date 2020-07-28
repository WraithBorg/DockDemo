package com.point.clddock;

import com.point.clddock.constant.CldConfigConst;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.point.clddock.mapper")
public class ClddockApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClddockApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {

        return args -> {
            System.out.println("启动自定义任务:");
            System.out.println(CldConfigConst.WEB_HOST);
            System.out.println(CldConfigConst.GET_OUT_STORE_DATA);
        };
    }
}
