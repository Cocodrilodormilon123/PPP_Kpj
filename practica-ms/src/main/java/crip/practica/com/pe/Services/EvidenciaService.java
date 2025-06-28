package crip.practica.com.pe.Services;


import crip.practica.com.pe.Entities.EstadoEvidencia;
import crip.practica.com.pe.Entities.Evidencia;
import java.util.List;
import java.util.Optional;

public interface EvidenciaService {
    Evidencia saveEvidencia(Evidencia evidencia);
    Evidencia updateEstadoEvidencia(Long id, EstadoEvidencia estado);
    List<Evidencia> getAllEvidencias();
    List<Evidencia> getEvidenciasByPracticaId(Long practicaId);
    Optional<Evidencia> getEvidenciaById(Long id);
    void deleteEvidencia(Long id);
    List<Evidencia> getEvidenciasByPersonaId(Long idPersona);
}
