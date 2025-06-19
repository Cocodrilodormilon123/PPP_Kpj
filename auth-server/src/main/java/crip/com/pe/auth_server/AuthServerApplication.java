package crip.com.pe.auth_server;

import crip.com.pe.auth_server.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}
	@Bean
	public CommandLineRunner testDB(UserRepository userRepository) {
		return args -> {
			System.out.println("Usuarios en BD: " + userRepository.count());
		};
	}
	@Bean
	public ApplicationRunner runner(Environment env) {
		return args -> {
			System.out.println("Perfiles activos: " + Arrays.toString(env.getActiveProfiles()));
		};
	}
}