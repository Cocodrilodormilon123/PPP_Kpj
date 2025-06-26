package crip.persona.com.pe.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import crip.persona.com.pe.Entities.Persona;
import crip.persona.com.pe.Entities.TipoPersona;
import crip.persona.com.pe.Services.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personas")
@Tag(name = "Persona Resource", description = "CRUD de Personas")
public class PersonaController {

    private final PersonaService personaService;
    private static final Logger log = LoggerFactory.getLogger(PersonaController.class);

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> registrarConImagen(
            @RequestPart("persona") String personaJson,
            @RequestPart("file") MultipartFile archivo
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            Persona persona = mapper.readValue(personaJson, Persona.class);

            String nombreArchivo = "persona_" + persona.getDni() + "_" + archivo.getOriginalFilename();
            Path rutaDestino = Paths.get("img").resolve(nombreArchivo).toAbsolutePath();
            Files.createDirectories(rutaDestino.getParent());
            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            persona.setFoto(nombreArchivo);
            Persona nueva = personaService.guardar(persona);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("id", nueva.getId());

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            // Error lanzado desde el servicio, como "El DNI ya está registrado"
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error inesperado al registrar persona");
            return ResponseEntity.internalServerError().body(error);
        }
    }



    @Operation(summary = "Actualizar persona por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizar(@PathVariable Long id, @RequestBody Persona persona) {
        log.info("Actualizando persona con ID: {}", id);
        Persona actualizada = personaService.actualizar(id, persona);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Listar todas las personas")
    @GetMapping
    public ResponseEntity<List<Persona>> listarTodo() {
        return ResponseEntity.ok(personaService.listarTodo());
    }

    @Operation(summary = "Listar personas por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Persona>> listarPorTipo(@PathVariable TipoPersona tipo) {
        return ResponseEntity.ok(personaService.listarPorTipo(tipo));
    }

    @Operation(summary = "Buscar persona por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Persona> buscarPorId(@PathVariable Long id) {
        return personaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Buscar persona por código universitario")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Persona> buscarPorCodigo(@PathVariable String codigo) {
        return personaService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Mostrar imagen de persona")
    @GetMapping(value = "/img/{nombreImagen}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable String nombreImagen) {
        try {
            Path ruta = Paths.get("img").resolve(nombreImagen).toAbsolutePath();
            byte[] imagen = Files.readAllBytes(ruta);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
