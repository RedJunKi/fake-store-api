package com.project.fake_store_api;

import com.project.fake_store_api.global.aop.LogAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class FakeStoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FakeStoreApiApplication.class, args);
	}

}
