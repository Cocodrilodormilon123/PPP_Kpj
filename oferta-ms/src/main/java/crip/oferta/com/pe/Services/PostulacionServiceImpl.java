package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Entities.Postulacion;
import crip.oferta.com.pe.Repository.PostulacionRepository;

import crip.oferta.com.pe.models.Practica;
import crip.oferta.com.pe.Clients.PracticaClient;
import crip.oferta.com.pe.models.EstadoPractica;
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

    public PostulacionServiceImpl(PostulacionRepository postulacionRepository,
                                  PracticaClient practicaClient) {
        this.postulacionRepository = postulacionRepository;
        this.practicaClient = practicaClient;
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
                    EstadoPostulacion estadoAnterior = postulacion.getEstado();

                    switch (nuevoEstado) {
                        case PENDIENTE -> {
                            postulacion.setComentario("En espera");
                        }
                        case EN_REVISION -> {
                            postulacion.setComentario(
                                    (comentario == null || comentario.isBlank())
                                            ? "Tus documentos están en revisión."
                                            : comentario
                            );


                        }
                        case RECHAZADA -> {
                            postulacion.setComentario(
                                    (comentario == null || comentario.isBlank())
                                            ? "Tu postulación ha finalizado. Vuelve a intentarlo en otra convocatoria."
                                            : comentario
                            );
                        }
                    }

                    postulacion.setEstado(nuevoEstado);
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
