package examentwo.com.report_exm.services;

import examentwo.com.report_exm.models.Oferta;
import examentwo.com.report_exm.repositories.ResultadosRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private ResultadosRepository resultadosRepository;

    public ReportServiceImpl(ResultadosRepository resultadosRepository) {
        this.resultadosRepository = resultadosRepository;
    }


    @Override
    public List<Oferta> getOferta() {
        return resultadosRepository.getOferta();
    }

    @Override
    public Optional<Oferta> getOfertaById(Long id) {
        return resultadosRepository.getById(id);
    }
}
