package com.escuela;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del backend hexagonal.
 *
 * Es la UNICA clase fuera de las tres capas (domain / application / infrastructure):
 * solo existe para arrancar Spring Boot y que el escaneo de componentes
 * cubra todo el paquete com.escuela.
 */
@SpringBootApplication
public class ClientesHexagonalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientesHexagonalApplication.class, args);
    }
}
