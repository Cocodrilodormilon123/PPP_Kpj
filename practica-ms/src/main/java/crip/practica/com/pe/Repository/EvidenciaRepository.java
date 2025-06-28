package crip.practica.com.pe.Repository;

import crip.practica.com.pe.Entities.EstadoEvidencia;
import crip.practica.com.pe.Entities.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenciaRepository extends JpaRepository<Evidencia, Long> {
    List<Evidencia> findByEstado(EstadoEvidencia estado);
    List<Evidencia> findByPracticaId(Long practicaId);
    List<Evidencia> findAllByPracticaIdIn(List<Long> practicaIds);
}