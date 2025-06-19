package crip.oferta.com.pe.Clients;

import crip.oferta.com.pe.models.Practica;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PRACTICA-MS", path = "/practica-ms/practicas")
public interface PracticaClient {

    @PostMapping
    Practica registrar(@RequestBody Practica practica);
}