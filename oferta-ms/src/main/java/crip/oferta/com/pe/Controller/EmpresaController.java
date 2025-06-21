package crip.oferta.com.pe.Controller;

import crip.oferta.com.pe.Entities.Empresa;
import crip.oferta.com.pe.Entities.EstadoEmpresa;
import crip.oferta.com.pe.Services.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresa Resource", description = "CRUD de Empresas y validación de propuestas")
public class EmpresaController {

    private final EmpresaService empresaService;
    private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @Operation(summary = "Registrar una nueva empresa")
    @PostMapping
    public ResponseEntity<Empresa> registrar(@RequestBody Empresa empresa) {
        log.info("Registrando empresa: {} por persona ID: {}", empresa.getNombre(), empresa.getIdPersona());
        Empresa nueva = empresaService.guardar(empresa);
        return ResponseEntity.created(URI.create("/empresas/" + nueva.getId())).body(nueva);
    }

    @Operation(summary = "Actualizar estado de una empresa (por ID)")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Empresa> updateEstado(
            @PathVariable Long id,
            @RequestParam EstadoEmpresa estado) {
        log.info("Cambiando estado de empresa ID {} a {}", id, estado);
        Empresa actualizada = empresaService.updateEstado(id, estado);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Listar todas las empresas")
    @GetMapping
    public ResponseEntity<List<Empresa>> listarTodas() {
        log.info("Listando todas las empresas");
        return ResponseEntity.ok(empresaService.listarTodo());
    }

    @Operation(summary = "Listar empresas por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Empresa>> listarPorEstado(@PathVariable EstadoEmpresa estado) {
        log.info("Listando empresas con estado = {}", estado);
        return ResponseEntity.ok(empresaService.listarPorEstado(estado));
    }

    @Operation(summary = "Buscar una empresa por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {
        log.info("Buscando empresa con ID: {}", id);
        return empresaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}