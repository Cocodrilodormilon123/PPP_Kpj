package crip.oferta.com.pe.Configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gestión de Ofertas y Postulaciones",
                version = "1.0.0",
                description = "Microservicio para publicar ofertas de prácticas y registrar postulaciones de los estudiantes"
        )
)
public class OpenApiConfig {
}