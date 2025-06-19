package crip.persona.com.pe.Repository;

import crip.persona.com.pe.Entities.Persona;
import crip.persona.com.pe.Entities.TipoPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    List<Persona> findByTipoPersona(TipoPersona tipoPersona);
    Optional<Persona> findByCodigo(String codigo);
}