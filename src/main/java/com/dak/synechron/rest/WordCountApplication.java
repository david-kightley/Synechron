package com.dak.synechron.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class WordCountApplication {

	public static void main(String[] args) {
		SpringApplication.run(com.dak.synechron.rest.WordCountController.class, args);
	}

}
