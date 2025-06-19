package crip.practica.com.pe.Controller;

import crip.practica.com.pe.Entities.EstadoEvidencia;
import crip.practica.com.pe.Entities.Evidencia;
import crip.practica.com.pe.Entities.Practica;
import crip.practica.com.pe.Repository.PracticaRepository;
import crip.practica.com.pe.Services.EvidenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/practicaspp/evidencias")
@CrossOrigin(origins = "*")
public class EvidenciaController {

    private final EvidenciaService evidenciaService;
    private final PracticaRepository practicaRepository;

    public EvidenciaController(EvidenciaService evidenciaService, PracticaRepository practicaRepository) {
        this.evidenciaService = evidenciaService;
        this.practicaRepository = practicaRepository;
    }

    @PostMapping("/registrar/{idPractica}")
    public ResponseEntity<Evidencia> registrar(@PathVariable Long idPractica, @RequestBody Evidencia evidencia) {
        Practica practica = practicaRepository.findById(idPractica)
                .orElseThrow(() -> new RuntimeException("Práctica no encontrada"));
        evidencia.setPractica(practica);
        return new ResponseEntity<>(evidenciaService.saveEvidencia(evidencia), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Evidencia>> listarTodos() {
        return ResponseEntity.ok(evidenciaService.getAllEvidencias());
    }

    @GetMapping("/practica/{practicaId}")
    public ResponseEntity<List<Evidencia>> listarPorPractica(@PathVariable Long practicaId) {
        return ResponseEntity.ok(evidenciaService.getEvidenciasByPracticaId(practicaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evidencia> buscarPorId(@PathVariable Long id) {
        return evidenciaService.getEvidenciaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evidencia> actualizarEstado(@PathVariable Long id, @RequestParam EstadoEvidencia estado) {
        return ResponseEntity.ok(evidenciaService.updateEstadoEvidencia(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        evidenciaService.deleteEvidencia(id);
        return ResponseEntity.noContent().build();
    }
}
