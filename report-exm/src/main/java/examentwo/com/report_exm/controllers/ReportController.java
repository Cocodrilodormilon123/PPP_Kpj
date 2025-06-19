package examentwo.com.report_exm.controllers;

import examentwo.com.report_exm.models.Oferta;
import examentwo.com.report_exm.services.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "report")
public class ReportController {

    private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/ofertas")
    public ResponseEntity<List<Oferta>> getOfertas() {
        List<Oferta> ofertas = reportService.getOferta();
        return ResponseEntity.ok(ofertas);
    }

    @GetMapping("/{id}")
    public Optional<Oferta> getById(@PathVariable Long id) {
        return reportService.getOfertaById(id);
    }
}
