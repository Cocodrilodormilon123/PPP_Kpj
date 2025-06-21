package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Entities.DocumentoPostulacion;

import java.util.Optional;

public interface DocumentoPostulacionService {
    DocumentoPostulacion guardar(DocumentoPostulacion doc);
    Optional<DocumentoPostulacion> obtenerPorIdPostulacion(Long idPostulacion);
    DocumentoPostulacion actualizarEstado(Long idPostulacion, String nuevoEstado);
}
