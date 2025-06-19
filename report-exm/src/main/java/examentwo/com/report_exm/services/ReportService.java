package examentwo.com.report_exm.services;

import examentwo.com.report_exm.models.Oferta;

import java.util.List;
import java.util.Optional;

public interface ReportService {
    List<Oferta> getOferta();
    Optional<Oferta> getOfertaById(Long id);
}
