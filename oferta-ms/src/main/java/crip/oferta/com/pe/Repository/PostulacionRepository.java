package crip.oferta.com.pe.Repository;

import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Entities.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
    List<Postulacion> findByEstado(EstadoPostulacion estado);
    List<Postulacion> findByIdPersona(Long idPersona);
    List<Postulacion> findByOfertaId(Long ofertaId);
}