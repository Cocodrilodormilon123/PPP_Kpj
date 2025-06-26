package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Entities.Postulacion;

import java.util.List;
import java.util.Optional;

public interface PostulacionService {
    Postulacion savePostulacion(Postulacion postulacion);
    Postulacion updatePostulacion(Long id, EstadoPostulacion estado, String comentario);
    List<Postulacion> getAllPostulaciones();
    List<Postulacion> getPostulacionesByEstado(EstadoPostulacion estado);
    List<Postulacion> getPostulacionesByIdPersona(Long idPersona);
    List<Postulacion> getPostulacionesByOfertaId(Long ofertaId);
    Optional<Postulacion> getPostulacionById(Long id);
    void deletePostulacion(Long id);
    List<Postulacion> listarPorIdPersona(Long idPersona);
}