package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Clients.PracticaClient;
import crip.oferta.com.pe.Entities.EstadoDocumento;
import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Entities.Postulacion;
import crip.oferta.com.pe.Repository.DocumentoPostulacionRepository;
import crip.oferta.com.pe.Repository.PostulacionRepository;
import crip.oferta.com.pe.Services.PostulacionService;
import crip.oferta.com.pe.models.EstadoPractica;
import crip.oferta.com.pe.models.Practica;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostulacionServiceImpl implements PostulacionService {

    private final PostulacionRepository postulacionRepository;
    private final PracticaClient practicaClient;
    private final DocumentoPostulacionRepository documentoPostulacionRepository;

    public PostulacionServiceImpl(PostulacionRepository postulacionRepository,
                                  PracticaClient practicaClient,
                                  DocumentoPostulacionRepository documentoPostulacionRepository) {
        this.postulacionRepository = postulacionRepository;
        this.practicaClient = practicaClient;
        this.documentoPostulacionRepository = documentoPostulacionRepository;
    }

    @Override
    public Postulacion savePostulacion(Postulacion postulacion) {
        postulacion.setEstado(EstadoPostulacion.PENDIENTE);

        if (postulacion.getComentario() == null || postulacion.getComentario().isBlank()) {
            postulacion.setComentario("En espera");
        }

        if (postulacion.getFechaPostulacion() == null) {
            postulacion.setFechaPostulacion(LocalDate.now());
        }

        return postulacionRepository.save(postulacion);
    }

    @Override
    public Postulacion updatePostulacion(Long id, EstadoPostulacion nuevoEstado, String comentario) {
        return postulacionRepository.findById(id)
                .map(postulacion -> {
                    postulacion.setEstado(nuevoEstado);

                    switch (nuevoEstado) {
                        case PENDIENTE -> postulacion.setComentario("En espera");

                        case EN_REVISION -> postulacion.setComentario(
                                (comentario == null || comentario.isBlank()) ?
                                        "Tus documentos están en revisión." : comentario);

                        case RECHAZADA -> {
                            postulacion.setComentario(
                                    (comentario == null || comentario.isBlank()) ?
                                            "Tu postulación ha sido rechazada. Vuelve a intentarlo." : comentario);

                            documentoPostulacionRepository.findByIdPostulacion(postulacion.getId())
                                    .ifPresent(doc -> {
                                        doc.setEstado(EstadoDocumento.RECHAZADO);
                                        documentoPostulacionRepository.save(doc);
                                    });
                        }

                        case ACEPTADA -> {
                            postulacion.setComentario("Documentación validada exitosamente. ¡Bienvenido a la práctica!");

                            documentoPostulacionRepository.findByIdPostulacion(postulacion.getId())
                                    .ifPresent(doc -> {
                                        doc.setEstado(EstadoDocumento.ACEPTADO);
                                        documentoPostulacionRepository.save(doc);
                                    });

                            Practica nuevaPractica = new Practica();
                            nuevaPractica.setIdPersona(postulacion.getIdPersona());
                            nuevaPractica.setIdPostulacion(postulacion.getId());
                            nuevaPractica.setEstado(EstadoPractica.EN_PROCESO);
                            nuevaPractica.setFechaInicio(LocalDate.now());

                            practicaClient.registrar(nuevaPractica);
                        }
                    }

                    return postulacionRepository.save(postulacion);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Postulación no encontrada con ID: " + id));
    }

    @Override
    public List<Postulacion> getAllPostulaciones() {
        return postulacionRepository.findAll();
    }

    @Override
    public List<Postulacion> getPostulacionesByEstado(EstadoPostulacion estado) {
        return postulacionRepository.findByEstado(estado);
    }

    @Override
    public List<Postulacion> getPostulacionesByIdPersona(Long idPersona) {
        return postulacionRepository.findByIdPersona(idPersona);
    }

    @Override
    public List<Postulacion> getPostulacionesByOfertaId(Long ofertaId) {
        return postulacionRepository.findByOfertaId(ofertaId);
    }

    @Override
    public Optional<Postulacion> getPostulacionById(Long id) {
        return postulacionRepository.findById(id);
    }

    @Override
    public void deletePostulacion(Long id) {
        postulacionRepository.findById(id)
                .ifPresentOrElse(postulacionRepository::delete,
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Postulación no encontrada con ID: " + id);
                        });
    }

    @Override
    public List<Postulacion> listarPorIdPersona(Long idPersona) {
        return postulacionRepository.findByIdPersona(idPersona);
    }
}

