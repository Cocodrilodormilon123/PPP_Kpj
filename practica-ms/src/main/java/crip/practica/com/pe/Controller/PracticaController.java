package crip.practica.com.pe.Controller;

import crip.practica.com.pe.Entities.EstadoPractica;
import crip.practica.com.pe.Entities.Practica;
import crip.practica.com.pe.Services.PracticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/practicas")
@Tag(name = "Practica Resource", description = "CRUD de Prácticas")
public class PracticaController {

    private final PracticaService practicaService;
    private static final Logger log = LoggerFactory.getLogger(PracticaController.class);

    public PracticaController(PracticaService practicaService) {
        this.practicaService = practicaService;
    }
    @Operation(summary = "Registrar nueva práctica")
    @PostMapping
    public ResponseEntity<Practica> registrar(@RequestBody Practica practica) {
        if (practica.getIdPostulacion() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        if (practica.getIdPersona() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        log.info("Registrando práctica con postulación ID: {}", practica.getIdPostulacion());
        Practica nueva = practicaService.savePractica(practica);
        return ResponseEntity.created(URI.create("/practicas/" + nueva.getId())).body(nueva);
    }

    @Operation(summary = "Actualizar estado de una práctica por ID")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Practica> update(@PathVariable Long id, @RequestParam EstadoPractica estado) {
        log.info("Actualizando estado de práctica ID {} a {}", id, estado);
        Practica actualizada = practicaService.updatePractica(id, estado);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Listar todas las prácticas")
    @GetMapping
    public ResponseEntity<List<Practica>> listarTodo() {
        return ResponseEntity.ok(practicaService.getAllPracticas());
    }

    @Operation(summary = "Listar prácticas por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Practica>> listarPorEstado(@PathVariable EstadoPractica estado) {
        return ResponseEntity.ok(practicaService.getPracticasByEstado(estado));
    }

    @Operation(summary = "Buscar práctica por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Practica> buscarPorId(@PathVariable Long id) {
        return practicaService.getPracticaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar práctica por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        practicaService.deletePractica(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar prácticas por ID de persona")
    @GetMapping("/persona/{idPersona}")
    public ResponseEntity<List<Practica>> listarPorIdPersona(@PathVariable Long idPersona) {
        return ResponseEntity.ok(practicaService.getPracticasByPersonaId(idPersona));
    }

    @Operation(summary = "Generar práctica automáticamente si la postulación y el documento están aceptados")
    @PostMapping("/generar/{idPostulacion}")
    public ResponseEntity<?> generarDesdeEstados(@PathVariable Long idPostulacion) {
        log.info("Intentando generar práctica para postulación ID {}", idPostulacion);
        try {
            Practica nueva = practicaService.verificarYCrearPractica(idPostulacion);
            return ResponseEntity.created(URI.create("/practicas/" + nueva.getId())).body(nueva);
        } catch (Exception e) {
            log.error("Error al generar práctica: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Operation(summary = "Obtener detalle completo de la práctica")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> getDetalleCompleto(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(practicaService.obtenerDetalleCompleto(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Operation(summary = "Verificar si un estudiante ya tiene una práctica EN_PROCESO")
    @GetMapping("/persona/{idPersona}/tiene-activa")
    public ResponseEntity<Boolean> verificarPracticaActiva(@PathVariable Long idPersona) {
        boolean tieneActiva = practicaService.existePracticaActiva(idPersona);
        return ResponseEntity.ok(tieneActiva);
    }
    @Operation(summary = "Obtener detalle completo de la práctica por estudiante")
    @GetMapping("/detalle/estudiante/{idPersona}")
    public ResponseEntity<?> obtenerDetallePorEstudiante(@PathVariable Long idPersona) {
        try {
            return ResponseEntity.ok(practicaService.obtenerDetallePorEstudiante(idPersona));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}