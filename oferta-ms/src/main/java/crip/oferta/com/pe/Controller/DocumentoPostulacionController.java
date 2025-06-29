package crip.oferta.com.pe.Controller;

import crip.oferta.com.pe.Entities.DocumentoPostulacion;
import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Repository.PostulacionRepository;
import crip.oferta.com.pe.Services.DocumentoPostulacionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.ClassPathResource;
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

    @Operation(summary = "Subir archivo firmado")
    @PostMapping("/{idPostulacion}/archivo")
    public ResponseEntity<?> subirArchivo(@PathVariable Long idPostulacion, @RequestParam("file") MultipartFile file) {
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

    @Operation(summary = "Descargar plantilla solo si la postulación está en estado EN_REVISION")
    @GetMapping("/descargar-plantilla/{idPostulacion}")
    public ResponseEntity<?> descargarPlantilla(@PathVariable Long idPostulacion) {
        return postulacionRepository.findById(idPostulacion).map(postulacion -> {
            if (postulacion.getEstado() != EstadoPostulacion.EN_REVISION) {
                return ResponseEntity.status(403)
                        .body("La plantilla solo está disponible si la postulación está en EN_REVISION");
            }

            try {
                ClassPathResource resource = new ClassPathResource("static/plantillas/formato_postulacion.pdf");

                if (!resource.exists()) {
                    return ResponseEntity.internalServerError().body("Archivo no encontrado");
                }

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=formato_postulacion.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource);

            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Error al acceder al archivo");
            }

        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ver archivo subido")
    @GetMapping("/archivo/{nombre}")
    public ResponseEntity<Resource> verArchivo(@PathVariable("nombre") String nombre) {
        try {
            Path archivoPath = Paths.get("archivos-subidos").resolve(nombre).toAbsolutePath();
            Resource recurso = new UrlResource(archivoPath.toUri());

            if (!recurso.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(archivoPath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                    .body(recurso);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Aceptar documento y generar práctica")
    @PutMapping("/aceptar/{idPostulacion}")
    public ResponseEntity<DocumentoPostulacion> aceptarDocumento(@PathVariable Long idPostulacion) {
        DocumentoPostulacion actualizado = service.actualizarEstado(idPostulacion, "ACEPTADO");
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Rechazar documento con comentario obligatorio")
    @PutMapping("/rechazar/{idPostulacion}")
    public ResponseEntity<?> rechazarDocumento(
            @PathVariable Long idPostulacion,
            @RequestParam String comentario) {

        if (comentario == null || comentario.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El comentario es obligatorio para rechazar el documento.");
        }

        DocumentoPostulacion actualizado = service.actualizarEstadoConComentario(idPostulacion, "RECHAZADO", comentario);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Estado actual del documento")
    @GetMapping("/estado-documento/{idPostulacion}")
    public ResponseEntity<String> obtenerEstadoDocumento(@PathVariable Long idPostulacion) {
        return service.obtenerPorIdPostulacion(idPostulacion)
                .map(doc -> ResponseEntity.ok(doc.getEstado().name()))
                .orElse(ResponseEntity.notFound().build());
    }
}
