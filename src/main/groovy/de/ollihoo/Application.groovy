package de.ollihoo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ['de.ollihoo'])
public class Application implements CommandLineRunner  {

    @Override
    void run(String... args) throws Exception {}

    public static void main(String[] args) {
        SpringApplication.run(Application, args)
    }
}