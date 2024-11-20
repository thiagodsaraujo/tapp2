package com.felipe.arqsoftware.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(scanBasePackages = {"com.felipe.arqsoftware.demo", "com.example.boleto.BoletoApi"})
public class BancarioApplication {
	public static void main(String[] args) {
		SpringApplication.run(BancarioApplication.class, args);
	}
}