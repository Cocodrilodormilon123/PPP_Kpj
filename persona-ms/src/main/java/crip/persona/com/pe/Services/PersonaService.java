package crip.persona.com.pe.Services;

import crip.persona.com.pe.Entities.Persona;
import crip.persona.com.pe.Entities.TipoPersona;

import java.util.List;
import java.util.Optional;

public interface PersonaService {
    Persona guardar(Persona persona);
    Persona actualizar(Long id, Persona persona);
    List<Persona> listarTodo();
    List<Persona> listarPorTipo(TipoPersona tipoPersona);
    Optional<Persona> findById(Long id);
    Optional<Persona> findByCodigo(String codigo);
}