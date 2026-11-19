package com.uass.chacomer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la tienda Chacomer.
 * Arranca Tomcat en el puerto 8080 y conecta a PostgreSQL.
 */
@SpringBootApplication
public class ChacomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChacomerApplication.class, args);
	}

}
