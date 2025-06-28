package crip.practica.com.pe.Clients;

import crip.practica.com.pe.models.Postulacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "OFERTA-MS", path = "/oferta-ms")
public interface OfertaPostulacionClient {

    // Para verificar si puede crearse una práctica
    @GetMapping("/documento-postulacion/estado/{idPostulacion}")
    String obtenerEstadoPostulacion(@PathVariable("idPostulacion") Long idPostulacion);

    @GetMapping("/documento-postulacion/estado-documento/{idPostulacion}")
    String obtenerEstadoDocumento(@PathVariable("idPostulacion") Long idPostulacion);

    // Para obtener detalles completos de una postulación
    @GetMapping("/postulaciones/{id}")
    Postulacion obtenerPostulacionPorId(@PathVariable("id") Long id);
}
