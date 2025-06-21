package crip.oferta.com.pe.Controller;

import crip.oferta.com.pe.Entities.DocumentoPostulacion;
import crip.oferta.com.pe.Entities.EstadoDocumento;
import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Entities.Postulacion;
import crip.oferta.com.pe.Repository.PostulacionRepository;
import crip.oferta.com.pe.Services.DocumentoPostulacionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/documento-postulacion")
public class DocumentoPostulacionController {

    private final DocumentoPostulacionService service;
    private final PostulacionRepository postulacionRepository;

    public DocumentoPostulacionController(DocumentoPostulacionService service,
                                          PostulacionRepository postulacionRepository) {
        this.service = service;
        this.postulacionRepository = postulacionRepository;
    }

    @Operation(summary = "Subir archivo firmado (imagen o documento)")
    @PostMapping("/{idPostulacion}/archivo")
    public ResponseEntity<?> subirArchivo(
            @PathVariable Long idPostulacion,
            @RequestParam("file") MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType == null || !(contentType.equals("application/pdf")
                    || contentType.startsWith("image/")
                    || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
                return ResponseEntity.badRequest().body("Formato de archivo no permitido");
            }

            String extension = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".")))
                    .orElse("");

            String nombreUnico = "doc_" + System.currentTimeMillis() + extension;
            Path ruta = Paths.get("archivos-subidos/" + nombreUnico);
            Files.createDirectories(ruta.getParent());
            Files.write(ruta, file.getBytes());

            DocumentoPostulacion doc = new DocumentoPostulacion();
            doc.setIdPostulacion(idPostulacion);
            doc.setRutaArchivo(ruta.toString());
            doc.setEstado(EstadoDocumento.PENDIENTE);

            DocumentoPostulacion creado = service.guardar(doc);
            return ResponseEntity.ok(creado);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al guardar archivo: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener documento por ID de postulación")
    @GetMapping("/{idPostulacion}")
    public ResponseEntity<?> obtenerPorIdPostulacion(@PathVariable Long idPostulacion) {
        return service.obtenerPorIdPostulacion(idPostulacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar estado del documento de postulación")
    @PutMapping("/{idPostulacion}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long idPostulacion,
            @RequestParam String nuevoEstado) {
        DocumentoPostulacion actualizado = service.actualizarEstado(idPostulacion, nuevoEstado);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Descargar plantilla solo si la postulación está en estado EN_REVISION")
    @GetMapping("/descargar-plantilla/{idPostulacion}")
    public ResponseEntity<?> descargarPlantilla(@PathVariable Long idPostulacion) throws IOException {
        return postulacionRepository.findById(idPostulacion).map(postulacion -> {
            if (postulacion.getEstado() != EstadoPostulacion.EN_REVISION) {
                return ResponseEntity.status(403)
                        .body("La plantilla solo está disponible si la postulación está en EN_REVISION");
            }

            try {
                Path path = Paths.get("src/main/resources/static/plantillas/formato_postulacion.pdf");
                Resource resource = new UrlResource(path.toUri());

                if (!resource.exists()) {
                    return ResponseEntity.internalServerError()
                            .body("Archivo no encontrado en el servidor");
                }

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=formato_postulacion.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource);

            } catch (IOException e) {
                return ResponseEntity.internalServerError()
                        .body("Error al acceder al archivo: " + e.getMessage());
            }

        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener estado del documento por ID de postulación")
    @GetMapping("/estado-documento/{idPostulacion}")
    public ResponseEntity<String> obtenerEstadoDocumento(@PathVariable Long idPostulacion) {
        return service.obtenerPorIdPostulacion(idPostulacion)
                .map(doc -> ResponseEntity.ok(doc.getEstado().name()))
                .orElse(ResponseEntity.notFound().build());
    }
}
