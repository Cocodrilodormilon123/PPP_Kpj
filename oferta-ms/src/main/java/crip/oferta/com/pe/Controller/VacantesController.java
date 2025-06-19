package crip.oferta.com.pe.Controller;

import crip.oferta.com.pe.Entities.Oferta;
import crip.oferta.com.pe.Entities.Vacantes;
import crip.oferta.com.pe.Services.VacantesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vacantes")
@Tag(name = "Vacantes Resource", description = "Gestión de cupos disponibles para cada oferta")
public class VacantesController {

    private final VacantesService vacantesService;
    private static final Logger log = LoggerFactory.getLogger(VacantesController.class);

    public VacantesController(VacantesService vacantesService) {
        this.vacantesService = vacantesService;
    }

    @PostMapping("/oferta/{id}/cupos")
    public ResponseEntity<Vacantes> registrarVacantesPorOferta(
            @PathVariable Long id,
            @RequestParam int total) {

        log.info("Registrando o actualizando vacantes para oferta ID: {} con total={}", id, total);

        Optional<Vacantes> existente = vacantesService.findByOfertaId(id);

        if (existente.isPresent()) {
            Vacantes v = existente.get();
            v.setTotal(total);
            v.setOcupados(0); // Reinicia ocupados
            v.setDisponibles(total);
            Vacantes actualizado = vacantesService.guardar(v);
            return ResponseEntity.ok(actualizado);
        }

        // Si no existía, se crea nueva
        Vacantes nueva = new Vacantes();
        nueva.setTotal(total);
        nueva.setOcupados(0);
        nueva.setDisponibles(total);

        Oferta oferta = new Oferta();
        oferta.setId(id);
        nueva.setOferta(oferta);

        Vacantes creado = vacantesService.guardar(nueva);
        return ResponseEntity.created(URI.create("/vacantes/" + creado.getId())).body(creado);
    }



    @Operation(summary = "Actualizar cantidad de ocupados (y recalcular disponibles)")
    @PutMapping("/{id}/ocupados")
    public ResponseEntity<Vacantes> actualizarCupos(
            @PathVariable Long id,
            @RequestParam int ocupados) {
        log.info("Actualizando vacante ID {} con ocupados={}", id, ocupados);
        Vacantes actualizada = vacantesService.updateCupos(id, ocupados);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Buscar vacantes por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Vacantes> buscarPorId(@PathVariable Long id) {
        return vacantesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar vacantes por ID de oferta")
    @GetMapping("/oferta/{ofertaId}")
    public ResponseEntity<Vacantes> buscarPorOferta(@PathVariable Long ofertaId) {
        return vacantesService.findByOfertaId(ofertaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todas las vacantes")
    @GetMapping
    public ResponseEntity<List<Vacantes>> listarTodo() {
        return ResponseEntity.ok(vacantesService.listarTodo());
    }
}
