package crip.oferta.com.pe.Controller;

import crip.oferta.com.pe.Clients.PersonaClient;
import crip.oferta.com.pe.Clients.PracticaClient;
import crip.oferta.com.pe.Entities.EstadoDocumento;
import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Entities.Postulacion;
import crip.oferta.com.pe.Repository.DocumentoPostulacionRepository;
import crip.oferta.com.pe.Repository.PostulacionRepository;
import crip.oferta.com.pe.Services.PostulacionService;
import crip.oferta.com.pe.models.EstadoPractica;
import crip.oferta.com.pe.models.Persona;
import crip.oferta.com.pe.models.Practica;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/postulaciones")
@Tag(name = "Postulación Resource", description = "CRUD de Postulaciones de prácticas")
public class PostulacionController {

    private final PostulacionService postulacionService;
    private final PersonaClient personaClient;
    private final PostulacionRepository postulacionRepository;
    private final DocumentoPostulacionRepository documentoPostulacionRepository;
    private final PracticaClient practicaClient;

    private static final Logger log = LoggerFactory.getLogger(PostulacionController.class);

    public PostulacionController(PostulacionService postulacionService,
                                 PersonaClient personaClient,
                                 PostulacionRepository postulacionRepository,
                                 DocumentoPostulacionRepository documentoPostulacionRepository,
                                 PracticaClient practicaClient) {
        this.postulacionService = postulacionService;
        this.personaClient = personaClient;
        this.postulacionRepository = postulacionRepository;
        this.documentoPostulacionRepository = documentoPostulacionRepository;
        this.practicaClient = practicaClient;
    }

    // ✅ 1. Registrar nueva postulación
    @Operation(summary = "Registrar nueva postulación")
    @PostMapping
    public ResponseEntity<Postulacion> registrar(@RequestBody Postulacion postulacion) {
        log.info("Registrando nueva postulación con ID Persona: {}", postulacion.getIdPersona());
        Postulacion nueva = postulacionService.savePostulacion(postulacion);
        return ResponseEntity.created(URI.create("/postulaciones/" + nueva.getId())).body(nueva);
    }

    // ✅ 2. Actualizar estado del DOCUMENTO solamente
    @PutMapping("/{id}/documento")
    public ResponseEntity<?> actualizarEstadoDocumento(
            @PathVariable Long id,
            @RequestParam EstadoDocumento estado,
            @RequestParam(required = false) String comentario
    ) {
        var documentoOpt = documentoPostulacionRepository.findByIdPostulacion(id);
        if (documentoOpt.isEmpty()) return ResponseEntity.notFound().build();

        var documento = documentoOpt.get();
        documento.setEstado(estado);
        if (estado == EstadoDocumento.RECHAZADO && comentario != null) {
            documento.setComentario(comentario);
        }

        documentoPostulacionRepository.save(documento);
        return ResponseEntity.ok().build();
    }

    // ✅ 3. Actualizar estado de POSTULACION (y crear práctica si fue aceptada)
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPostulacion(
            @PathVariable Long id,
            @RequestParam EstadoPostulacion estado,
            @RequestParam(required = false) String comentario
    ) {
        Optional<Postulacion> postulacionOpt = postulacionRepository.findById(id);
        if (postulacionOpt.isEmpty()) return ResponseEntity.notFound().build();

        Postulacion postulacion = postulacionOpt.get();
        postulacion.setEstado(estado);
        if (comentario != null && !comentario.trim().isEmpty()) {
            postulacion.setComentario(comentario);
        }
        postulacionRepository.save(postulacion);

        if (estado == EstadoPostulacion.ACEPTADA) {
            Practica practica = new Practica();
            practica.setIdPostulacion(postulacion.getId());
            practica.setIdPersona(postulacion.getIdPersona());
            practica.setFechaInicio(LocalDate.now());
            practica.setEstado(EstadoPractica.EN_PROCESO);

            try {
                practicaClient.registrar(practica);
            } catch (Exception e) {
                log.error("❌ Error al crear la práctica:", e);
                return ResponseEntity.status(500).body("Error al crear la práctica: " + e.getMessage());
            }
        }

        return ResponseEntity.ok().build();
    }

    // ✅ 4. Otros endpoints (listar, buscar, eliminar...)

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

        Map<Long, Postulacion> unicas = new LinkedHashMap<>();
        for (Postulacion p : todas) {
            unicas.putIfAbsent(p.getIdPersona(), p);
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Postulacion p : unicas.values()) {
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

    @GetMapping("/estudiante/{idPersona}")
    public ResponseEntity<List<Postulacion>> listarPorEstudiante(@PathVariable Long idPersona) {
        List<Postulacion> lista = postulacionService.listarPorIdPersona(idPersona);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }
}
