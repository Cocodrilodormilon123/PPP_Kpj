package crip.practica.com.pe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PracticaMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticaMsApplication.class, args);
	}

}
