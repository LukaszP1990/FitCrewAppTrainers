package com.fitcrew.FitCrewAppTrainers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FitCrewAppTrainersApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitCrewAppTrainersApplication.class, args);
	}

}
