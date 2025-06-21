package crip.practica.com.pe.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "OFERTA-MS", path = "/documento-postulacion")
public interface OfertaPostulacionClient {

    @GetMapping("/estado/{idPostulacion}")
    String obtenerEstadoPostulacion(@PathVariable("idPostulacion") Long idPostulacion);

    @GetMapping("/estado-documento/{idPostulacion}")
    String obtenerEstadoDocumento(@PathVariable("idPostulacion") Long idPostulacion);
}
