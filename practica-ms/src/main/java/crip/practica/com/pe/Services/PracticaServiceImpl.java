package crip.practica.com.pe.Services;

import crip.practica.com.pe.Clients.OfertaPostulacionClient;
import crip.practica.com.pe.Entities.EstadoPractica;
import crip.practica.com.pe.Entities.Practica;
import crip.practica.com.pe.Repository.PracticaRepository;
import crip.practica.com.pe.models.DetallePracticaDTO;
import crip.practica.com.pe.models.Postulacion;
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
        if (practica.getIdPersona() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe especificar el ID de la persona.");
        }

        if (practica.getIdPostulacion() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe especificar el ID de la postulación.");
        }

        if (practica.getEstado() == null) {
            practica.setEstado(EstadoPractica.EN_PROCESO);
        }

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
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no permitido.");
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Práctica no encontrada con ID: " + id));
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
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una práctica para esta postulación.");
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

    @Override
    public DetallePracticaDTO obtenerDetalleCompleto(Long idPractica) {
        Practica practica = practicaRepository.findById(idPractica)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Práctica no encontrada"));

        Postulacion postulacion = ofertaPostulacionClient.obtenerPostulacionPorId(practica.getIdPostulacion());

        DetallePracticaDTO dto = new DetallePracticaDTO();
        dto.setIdPractica(practica.getId());
        dto.setFechaInicio(practica.getFechaInicio());
        dto.setFechaFin(practica.getFechaFin());
        dto.setEstado(practica.getEstado());

        if (postulacion.getOferta() != null) {
            dto.setTituloOferta(postulacion.getOferta().getTitulo());
            dto.setModalidad(postulacion.getOferta().getModalidad());
            if (postulacion.getOferta().getEmpresa() != null) {
                dto.setEmpresa(postulacion.getOferta().getEmpresa().getNombre());
            }
        }

        return dto;
    }
    @Override
    public boolean existePracticaActiva(Long idPersona) {
        return practicaRepository.existsByIdPersonaAndEstado(idPersona, EstadoPractica.EN_PROCESO);
    }
    @Override
    public DetallePracticaDTO obtenerDetallePorEstudiante(Long idPersona) {
        Practica practica = practicaRepository
                .findByIdPersonaAndEstado(idPersona, EstadoPractica.EN_PROCESO)
                .orElseThrow(() -> new RuntimeException("No se encontró una práctica EN_PROCESO para el estudiante"));

        Postulacion postulacion = ofertaPostulacionClient.obtenerPostulacionPorId(practica.getIdPostulacion());

        DetallePracticaDTO dto = new DetallePracticaDTO();
        dto.setIdPractica(practica.getId());
        dto.setFechaInicio(practica.getFechaInicio());
        dto.setFechaFin(practica.getFechaFin());
        dto.setEstado(practica.getEstado());
        dto.setTituloOferta(postulacion.getOferta().getTitulo());
        dto.setEmpresa(postulacion.getOferta().getEmpresa().getNombre());
        dto.setModalidad(postulacion.getOferta().getModalidad());

        return dto;
    }
}