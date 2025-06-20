package crip.oferta.com.pe.Clients;

import crip.oferta.com.pe.models.Persona;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PERSONA-MS", path = "/persona-ms/personas")
public interface PersonaClient {

    @GetMapping("/{id}")
    Persona buscarPorId(@PathVariable("id") Long id);
}