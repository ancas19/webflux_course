package co.com.ancas.playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@EnableR2dbcRepositories(basePackages = "co.com.ancas.playground.${sec}")
@SpringBootApplication(scanBasePackages = "co.com.ancas.playground.${sec}")
public class PlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaygroundApplication.class, args);
	}

}
