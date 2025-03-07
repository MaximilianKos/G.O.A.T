package ch.kos.goat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "ch.kos.goat.entities")
@EnableJpaRepositories(basePackages = "ch.kos.goat.repositories")
public class GoatApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoatApplication.class, args);
	}

}
