package crip.practica.com.pe.Repository;

import crip.practica.com.pe.Entities.EstadoPractica;
import crip.practica.com.pe.Entities.Practica;
import crip.practica.com.pe.models.DetallePracticaDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticaRepository extends JpaRepository<Practica, Long> {
    List<Practica> findByEstado(EstadoPractica estado);
    List<Practica> findByIdPostulacion(Long idPostulacion);
    List<Practica> findByIdPersona(Long idPersona);
    boolean existsByIdPostulacion(Long idPostulacion);

}