package com.matricula.principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.picoyplaca.controller", "com.validacion.services", "com.vehiculo.entities"})
public class MatriculaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatriculaAppApplication.class, args);
	}

}
