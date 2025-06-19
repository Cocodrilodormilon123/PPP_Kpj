package crip.oferta.com.pe.Controller;

import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Entities.Postulacion;
import crip.oferta.com.pe.Services.PostulacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/postulaciones")
@Tag(name = "Postulación Resource", description = "CRUD de Postulaciones de prácticas")
public class PostulacionController {

    private final PostulacionService postulacionService;
    private static final Logger log = LoggerFactory.getLogger(PostulacionController.class);

    public PostulacionController(PostulacionService postulacionService) {
        this.postulacionService = postulacionService;
    }

    @Operation(summary = "Registrar nueva postulación")
    @PostMapping
    public ResponseEntity<Postulacion> registrar(@RequestBody Postulacion postulacion) {
        log.info("Registrando nueva postulación con ID Persona: {}", postulacion.getIdPersona());
        Postulacion nueva = postulacionService.savePostulacion(postulacion);
        return ResponseEntity.created(URI.create("/postulaciones/" + nueva.getId())).body(nueva);
    }

    @Operation(summary = "Actualizar estado de una postulación por ID (con comentario opcional)")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Postulacion> updateEstado(
            @PathVariable Long id,
            @RequestParam EstadoPostulacion estado,
            @RequestParam(required = false) String comentario) {

        log.info("Actualizando estado de postulación ID {} a {} con comentario '{}'", id, estado, comentario);
        Postulacion actualizada = postulacionService.updatePostulacion(id, estado, comentario);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Listar todas las postulaciones")
    @GetMapping
    public ResponseEntity<List<Postulacion>> listarTodo() {
        return ResponseEntity.ok(postulacionService.getAllPostulaciones());
    }

    @Operation(summary = "Listar postulaciones por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Postulacion>> listarPorEstado(@PathVariable EstadoPostulacion estado) {
        return ResponseEntity.ok(postulacionService.getPostulacionesByEstado(estado));
    }

    @Operation(summary = "Buscar postulación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Postulacion> buscarPorId(@PathVariable Long id) {
        return postulacionService.getPostulacionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar postulaciones por ID de oferta")
    @GetMapping("/oferta/{ofertaId}")
    public ResponseEntity<List<Postulacion>> listarPorOfertaId(@PathVariable Long ofertaId) {
        return ResponseEntity.ok(postulacionService.getPostulacionesByOfertaId(ofertaId));
    }

    @Operation(summary = "Listar postulaciones por ID de persona")
    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<Postulacion>> listarPorPersonaId(@PathVariable Long personaId) {
        return ResponseEntity.ok(postulacionService.getPostulacionesByIdPersona(personaId));
    }

    @Operation(summary = "Eliminar postulación por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postulacionService.deletePostulacion(id);
        return ResponseEntity.noContent().build();
    }
}