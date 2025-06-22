package crip.oferta.com.pe.Controller;

import crip.oferta.com.pe.Clients.PersonaClient;
import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Entities.Postulacion;
import crip.oferta.com.pe.Services.PostulacionService;
import crip.oferta.com.pe.models.Persona;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/postulaciones")
@Tag(name = "Postulación Resource", description = "CRUD de Postulaciones de prácticas")
public class PostulacionController {

    private final PostulacionService postulacionService;
    private final PersonaClient personaClient;

    private static final Logger log = LoggerFactory.getLogger(PostulacionController.class);

    public PostulacionController(PostulacionService postulacionService, PersonaClient personaClient) {
        this.postulacionService = postulacionService;
        this.personaClient = personaClient;
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
    public ResponseEntity<?> updateEstado(
            @PathVariable Long id,
            @RequestParam EstadoPostulacion estado,
            @RequestParam(required = false) String comentario) {
        try {
            Postulacion actualizada = postulacionService.updatePostulacion(id, estado, comentario);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            e.printStackTrace(); // para ver en consola
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ocurrió un error interno: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
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
    @GetMapping("/persona/{idPersona}")
    public ResponseEntity<List<Postulacion>> listarPorPersonaId(@PathVariable Long idPersona) {
        return ResponseEntity.ok(postulacionService.getPostulacionesByIdPersona(idPersona));
    }

    @Operation(summary = "Eliminar postulación por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postulacionService.deletePostulacion(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/oferta/{idOferta}/postulantes-unicos-detallado")
    public ResponseEntity<List<Map<String, Object>>> listarPostulantesUnicosDetallado(@PathVariable Long idOferta) {
        List<Postulacion> todas = postulacionService.getPostulacionesByOfertaId(idOferta);

        // Para evitar duplicados por persona
        Map<Long, Postulacion> unicas = new LinkedHashMap<>();
        for (Postulacion p : todas) {
            unicas.putIfAbsent(p.getIdPersona(), p);
        }

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Postulacion p : unicas.values()) {
            // Consultamos datos de la persona usando Feign
            Persona persona = personaClient.buscarPorId(p.getIdPersona());

            Map<String, Object> item = new HashMap<>();
            item.put("id", p.getId());
            item.put("idPersona", persona.getId());
            item.put("codigo", persona.getCodigo());
            item.put("nombre", persona.getNombre() + " " + persona.getApellido());
            item.put("estado", p.getEstado().name());
            item.put("comentario", p.getComentario());

            resultado.add(item);
        }

        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Obtener estado de una postulación por ID")
    @GetMapping("/estado-actual/{idPostulacion}")
    public ResponseEntity<String> obtenerEstadoPostulacion(@PathVariable Long idPostulacion) {
        return postulacionService.getPostulacionById(idPostulacion)
                .map(p -> ResponseEntity.ok(p.getEstado().name()))
                .orElse(ResponseEntity.notFound().build());
    }

}