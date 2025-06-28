package crip.practica.com.pe.Services;

import crip.practica.com.pe.Entities.EstadoEvidencia;
import crip.practica.com.pe.Entities.Evidencia;
import crip.practica.com.pe.Entities.Practica;
import crip.practica.com.pe.Repository.EvidenciaRepository;
import crip.practica.com.pe.Repository.PracticaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EvidenciaServiceImpl implements EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;
    private final PracticaRepository practicaRepository;

    public EvidenciaServiceImpl(EvidenciaRepository evidenciaRepository, PracticaRepository practicaRepository) {
        this.evidenciaRepository = evidenciaRepository;
        this.practicaRepository = practicaRepository;
    }

    @Override
    public Evidencia saveEvidencia(Evidencia evidencia) {
        evidencia.setFechaSubida(LocalDate.now());
        if (evidencia.getEstado() == null) {
            evidencia.setEstado(EstadoEvidencia.PENDIENTE);
        }
        return evidenciaRepository.save(evidencia);
    }

    @Override
    public Evidencia updateEstadoEvidencia(Long id, EstadoEvidencia estado) {
        return evidenciaRepository.findById(id)
                .map(e -> {
                    e.setEstado(estado);
                    return evidenciaRepository.save(e);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evidencia no encontrada"));
    }

    @Override
    public List<Evidencia> getAllEvidencias() {
        return evidenciaRepository.findAll();
    }

    @Override
    public List<Evidencia> getEvidenciasByPracticaId(Long practicaId) {
        return evidenciaRepository.findByPracticaId(practicaId);
    }

    @Override
    public Optional<Evidencia> getEvidenciaById(Long id) {
        return evidenciaRepository.findById(id);
    }

    @Override
    public void deleteEvidencia(Long id) {
        evidenciaRepository.findById(id)
                .ifPresentOrElse(evidenciaRepository::delete, () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evidencia no encontrada");
                });
    }
    public List<Evidencia> getEvidenciasByPersonaId(Long idPersona) {
        List<Practica> practicas = practicaRepository.findByIdPersona(idPersona);
        List<Long> idsPracticas = practicas.stream()
                .map(Practica::getId)
                .toList();
        return evidenciaRepository.findAllByPracticaIdIn(idsPracticas);
    }
}