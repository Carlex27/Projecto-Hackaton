package com.example.project_hackaton;


import com.example.project_hackaton.config.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication
public class ProjectHackatonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectHackatonApplication.class, args);
	}


}
