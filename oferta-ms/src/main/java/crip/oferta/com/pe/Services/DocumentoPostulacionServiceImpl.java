package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Entities.DocumentoPostulacion;
import crip.oferta.com.pe.Entities.EstadoDocumento;
import crip.oferta.com.pe.Repository.DocumentoPostulacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class DocumentoPostulacionServiceImpl implements DocumentoPostulacionService {

    private final DocumentoPostulacionRepository documentoPostulacionRepository;

    public DocumentoPostulacionServiceImpl(DocumentoPostulacionRepository documentoPostulacionRepository) {
        this.documentoPostulacionRepository = documentoPostulacionRepository;
    }

    @Override
    public DocumentoPostulacion guardar(DocumentoPostulacion doc) {
        return documentoPostulacionRepository.save(doc);
    }

    @Override
    public Optional<DocumentoPostulacion> obtenerPorIdPostulacion(Long idPostulacion) {
        return documentoPostulacionRepository.findByIdPostulacion(idPostulacion);
    }

    @Override
    public DocumentoPostulacion actualizarEstado(Long idPostulacion, String nuevoEstado) {
        DocumentoPostulacion doc = documentoPostulacionRepository.findByIdPostulacion(idPostulacion)
                .orElseThrow(() -> new RuntimeException("No existe documento para esta postulación"));

        doc.setEstado(EstadoDocumento.valueOf(nuevoEstado));
        return documentoPostulacionRepository.save(doc);
    }
}
