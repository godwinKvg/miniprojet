package com.ensah.core;

import com.ensah.core.bo.Filiere;
import com.ensah.core.services.impl.EtudiantServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication(scanBasePackages = { "com.ensah" })

public class NotesManagementBootApplication {



	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NotesManagementBootApplication.class);

	}




	public static void main(String[] args) throws Exception {

		SpringApplication.run(NotesManagementBootApplication.class, args);

	}


}
