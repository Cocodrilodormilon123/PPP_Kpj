package crip.practica.com.pe.Configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gestión de Prácticas",
                version = "1.0.0",
                description = "Microservicio para el seguimiento de prácticas aceptadas y control de evidencias"
        )
)
public class OpenApiConfig {
}