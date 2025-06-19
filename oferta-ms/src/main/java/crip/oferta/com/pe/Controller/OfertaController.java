package crip.oferta.com.pe.Controller;

import crip.oferta.com.pe.Entities.EstadoOferta;
import crip.oferta.com.pe.Entities.Oferta;
import crip.oferta.com.pe.Services.OfertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ofertas")
@Tag(name = "Oferta Resource", description = "CRUD de Ofertas de prácticas")
public class OfertaController {

    private final OfertaService ofertaService;
    private static final Logger log = LoggerFactory.getLogger(OfertaController.class);

    public OfertaController(OfertaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    @Operation(summary = "Registrar nueva oferta")
    @PostMapping
    public ResponseEntity<Oferta> registrar(@RequestBody Oferta oferta) {
        log.info("Registrando nueva oferta: {}", oferta.getTitulo());
        Oferta nueva = ofertaService.guardar(oferta);
        return ResponseEntity.created(URI.create("/ofertas/" + nueva.getId())).body(nueva);
    }

    @Operation(summary = "Actualizar estado de una oferta por ID")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Oferta> updateEstado(
            @PathVariable Long id,
            @RequestParam EstadoOferta estado) {
        log.info("Actualizando estado de oferta ID {} a {}", id, estado);
        Oferta actualizada = ofertaService.updateEstado(id, estado);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Actualizar toda la información de una oferta por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Oferta> actualizarOferta(@PathVariable Long id, @RequestBody Oferta ofertaActualizada) {
        log.info("Actualizando oferta ID {} con nuevos datos", id);
        Oferta actualizada = ofertaService.actualizarOferta(id, ofertaActualizada);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Listar todas las ofertas")
    @GetMapping
    public ResponseEntity<List<Oferta>> listarTodo() {
        return ResponseEntity.ok(ofertaService.listarTodo());
    }

    @Operation(summary = "Listar ofertas por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Oferta>> listarPorEstado(@PathVariable EstadoOferta estado) {
        return ResponseEntity.ok(ofertaService.listarPorEstado(estado));
    }

    @Operation(summary = "Buscar oferta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Oferta> buscarPorId(@PathVariable Long id) {
        return ofertaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
