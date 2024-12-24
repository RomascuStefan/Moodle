package com.POS_API_MONGO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class PosApiMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosApiMongoApplication.class, args);
	}

}
