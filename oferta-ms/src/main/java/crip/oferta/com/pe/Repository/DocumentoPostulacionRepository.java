package crip.oferta.com.pe.Repository;

import crip.oferta.com.pe.Entities.DocumentoPostulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentoPostulacionRepository extends JpaRepository<DocumentoPostulacion, Long> {
    Optional<DocumentoPostulacion> findByIdPostulacion(Long idPostulacion);

}
