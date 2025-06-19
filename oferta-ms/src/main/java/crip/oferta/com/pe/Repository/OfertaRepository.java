package crip.oferta.com.pe.Repository;

import crip.oferta.com.pe.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OfertaRepository extends JpaRepository<Oferta, Long> {
    List<Oferta> findByEstado(EstadoOferta estado);
}