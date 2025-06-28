package crip.practica.com.pe.Controller;

import crip.practica.com.pe.Entities.EstadoEvidencia;
import crip.practica.com.pe.Entities.Evidencia;
import crip.practica.com.pe.Entities.Practica;
import crip.practica.com.pe.Repository.PracticaRepository;
import crip.practica.com.pe.Services.EvidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/practicaspp/evidencias")
public class EvidenciaController {

    private final EvidenciaService evidenciaService;
    private final PracticaRepository practicaRepository;

    public EvidenciaController(EvidenciaService evidenciaService, PracticaRepository practicaRepository) {
        this.evidenciaService = evidenciaService;
        this.practicaRepository = practicaRepository;
    }

    @PostMapping("/registrar/{idPractica}")
    public ResponseEntity<Evidencia> registrar(
            @PathVariable Long idPractica,
            @RequestParam("semana") int semana,
            @RequestPart("archivo") MultipartFile archivo) {

        Practica practica = practicaRepository.findById(idPractica)
                .orElseThrow(() -> new RuntimeException("Práctica no encontrada"));

        try {
            // Crear carpeta si no existe
            String carpetaUploads = "uploads/";
            java.io.File folder = new java.io.File(carpetaUploads);
            if (!folder.exists()) folder.mkdirs();

            // Generar nombre único
            String nombre = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
            String ruta = carpetaUploads + nombre;

            // Guardar archivo físicamente
            java.nio.file.Files.copy(archivo.getInputStream(), java.nio.file.Paths.get(ruta));

            // Crear entidad evidencia
            Evidencia evidencia = new Evidencia();
            evidencia.setPractica(practica);
            evidencia.setSemana(semana);
            evidencia.setNombreArchivo(nombre);
            evidencia.setUrlArchivo(ruta);
            evidencia.setFechaSubida(LocalDate.now());
            evidencia.setEstado(EstadoEvidencia.PENDIENTE);

            return new ResponseEntity<>(evidenciaService.saveEvidencia(evidencia), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @GetMapping("/descargar/{nombreArchivo}")
    public ResponseEntity<Resource> descargar(@PathVariable String nombreArchivo) throws IOException {
        Path path = Paths.get("uploads/" + nombreArchivo);
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF) // MIME tipo correcto
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Listar evidencias por ID de persona (administrador)")
    @GetMapping("/persona/{idPersona}")
    public ResponseEntity<List<Evidencia>> listarPorPersona(@PathVariable Long idPersona) {
        return ResponseEntity.ok(evidenciaService.getEvidenciasByPersonaId(idPersona));
    }
}
