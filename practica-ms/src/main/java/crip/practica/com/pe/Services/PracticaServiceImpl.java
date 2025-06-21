package crip.practica.com.pe.Services;

import crip.practica.com.pe.Clients.OfertaPostulacionClient;
import crip.practica.com.pe.Entities.EstadoPractica;
import crip.practica.com.pe.Entities.Practica;
import crip.practica.com.pe.Repository.PracticaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PracticaServiceImpl implements PracticaService {

    private final PracticaRepository practicaRepository;
    private final OfertaPostulacionClient ofertaPostulacionClient;

    public PracticaServiceImpl(PracticaRepository practicaRepository, OfertaPostulacionClient ofertaPostulacionClient) {
        this.practicaRepository = practicaRepository;
        this.ofertaPostulacionClient = ofertaPostulacionClient;
    }

    @Override
    public Practica savePractica(Practica practica) {
        // Validación: debe tener id de persona
        if (practica.getIdPersona() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe especificar el ID de la persona.");
        }

        // Validación: debe tener id de postulación
        if (practica.getIdPostulacion() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe especificar el ID de la postulación.");
        }

        // Estado por defecto
        if (practica.getEstado() == null) {
            practica.setEstado(EstadoPractica.EN_PROCESO);
        }

        // Fecha de inicio automática si no se define
        if (practica.getFechaInicio() == null) {
            practica.setFechaInicio(LocalDate.now());
        }

        return practicaRepository.save(practica);
    }

    @Override
    public Practica updatePractica(Long id, EstadoPractica estado) {
        return practicaRepository.findById(id)
                .map(existingPractica -> {
                    existingPractica.setEstado(estado);

                    if (estado == EstadoPractica.FINALIZADA && existingPractica.getFechaFin() == null) {
                        existingPractica.setFechaFin(LocalDate.now());
                    }

                    if (estado == EstadoPractica.EN_PROCESO && existingPractica.getFechaInicio() == null) {
                        existingPractica.setFechaInicio(LocalDate.now());
                    }

                    if (estado == EstadoPractica.EN_PROCESO || estado == EstadoPractica.FINALIZADA) {
                        return practicaRepository.save(existingPractica);
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Estado no permitido.");
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Práctica no encontrada con ID: " + id));
    }

    @Override
    public List<Practica> getAllPracticas() {
        return practicaRepository.findAll();
    }

    @Override
    public List<Practica> getPracticasByEstado(EstadoPractica estado) {
        return practicaRepository.findByEstado(estado);
    }

    @Override
    public List<Practica> getPracticasByPostulacionId(Long postulacionId) {
        return practicaRepository.findByIdPostulacion(postulacionId);
    }

    @Override
    public List<Practica> getPracticasByPersonaId(Long personaId) {
        return practicaRepository.findByIdPersona(personaId);
    }

    @Override
    public Optional<Practica> getPracticaById(Long id) {
        return practicaRepository.findById(id);
    }

    @Override
    public Practica verificarYCrearPractica(Long idPostulacion) {
        String estadoPostulacion = ofertaPostulacionClient.obtenerEstadoPostulacion(idPostulacion);
        String estadoDocumento = ofertaPostulacionClient.obtenerEstadoDocumento(idPostulacion);

        if ("ACEPTADA".equals(estadoPostulacion) && "ACEPTADO".equals(estadoDocumento)) {
            boolean yaExiste = practicaRepository.existsByIdPostulacion(idPostulacion);
            if (yaExiste) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Ya existe una práctica para esta postulación.");
            }

            Practica practica = new Practica();
            practica.setIdPostulacion(idPostulacion);
            practica.setEstado(EstadoPractica.EN_PROCESO);
            practica.setFechaInicio(LocalDate.now());

            return practicaRepository.save(practica);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se cumplen los requisitos para generar la práctica. " +
                            "(Postulación: " + estadoPostulacion + ", Documento: " + estadoDocumento + ")");
        }
    }

    @Override
    public void deletePractica(Long id) {
        practicaRepository.findById(id)
                .ifPresentOrElse(practicaRepository::delete,
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Práctica no encontrada con ID: " + id);
                        });
    }

}
