package crip.practica.com.pe.Services;

import crip.practica.com.pe.Entities.EstadoPractica;
import crip.practica.com.pe.Entities.Practica;

import java.util.List;
import java.util.Optional;

public interface PracticaService {
    Practica savePractica(Practica practica);
    Practica updatePractica(Long id, EstadoPractica estado);
    List<Practica> getAllPracticas();
    List<Practica> getPracticasByEstado(EstadoPractica estado);
    List<Practica> getPracticasByPostulacionId(Long postulacionId);
    List<Practica> getPracticasByPersonaId(Long personaId);
    Optional<Practica> getPracticaById(Long id);
    Practica verificarYCrearPractica(Long idPostulacion);
    void deletePractica(Long id);
}