package com.siwoo.azurevm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * VM 구성 요소.
 * 1. 이미지
 * 2. 네트워크
 */
@SpringBootApplication
public class AzureVmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzureVmApplication.class, args);
	}

}
