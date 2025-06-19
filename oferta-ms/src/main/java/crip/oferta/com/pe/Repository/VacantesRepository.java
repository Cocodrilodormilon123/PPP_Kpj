package crip.oferta.com.pe.Repository;

import crip.oferta.com.pe.Entities.Vacantes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VacantesRepository extends JpaRepository<Vacantes, Long> {
    Optional<Vacantes> findByOfertaId(Long ofertaId);
}