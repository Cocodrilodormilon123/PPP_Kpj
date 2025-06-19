package crip.persona.com.pe.Configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gestión de Personas",
                version = "1.0.0",
                description = "Microservicio encargado de registrar y administrar datos de personas"
        )
)
public class OpenApiConfig {
}