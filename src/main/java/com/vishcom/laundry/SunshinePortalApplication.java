package com.vishcom.laundry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication(scanBasePackages = "com.vishcom.laundry")
public class SunshinePortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SunshinePortalApplication.class, args);
	}

}
