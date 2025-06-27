package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Clients.PracticaClient;
import crip.oferta.com.pe.Entities.DocumentoPostulacion;
import crip.oferta.com.pe.Entities.EstadoDocumento;
import crip.oferta.com.pe.Entities.EstadoPostulacion;
import crip.oferta.com.pe.Entities.Postulacion;
import crip.oferta.com.pe.models.Practica;
import crip.oferta.com.pe.models.EstadoPractica;
import crip.oferta.com.pe.Repository.DocumentoPostulacionRepository;
import crip.oferta.com.pe.Repository.PostulacionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class DocumentoPostulacionServiceImpl implements DocumentoPostulacionService {

    private static final Logger log = LoggerFactory.getLogger(DocumentoPostulacionServiceImpl.class);

    private final DocumentoPostulacionRepository documentoPostulacionRepository;
    private final PostulacionRepository postulacionRepository;
    private final PracticaClient practicaClient;

    public DocumentoPostulacionServiceImpl(DocumentoPostulacionRepository documentoPostulacionRepository,
                                           PostulacionRepository postulacionRepository,
                                           PracticaClient practicaClient) {
        this.documentoPostulacionRepository = documentoPostulacionRepository;
        this.postulacionRepository = postulacionRepository;
        this.practicaClient = practicaClient;
    }

    @Override
    public DocumentoPostulacion guardar(DocumentoPostulacion nuevoDoc) {
        if (nuevoDoc == null || nuevoDoc.getIdPostulacion() == null) {
            throw new IllegalArgumentException("El documento o el ID de la postulación no pueden ser nulos.");
        }

        Long idPostulacion = nuevoDoc.getIdPostulacion();
        Optional<DocumentoPostulacion> existenteOpt = documentoPostulacionRepository.findByIdPostulacion(idPostulacion);

        if (existenteOpt.isPresent()) {
            DocumentoPostulacion existente = existenteOpt.get();
            existente.setRutaArchivo(nuevoDoc.getRutaArchivo());
            existente.setEstado(EstadoDocumento.PENDIENTE);
            existente.setFechaSubida(nuevoDoc.getFechaSubida());
            return documentoPostulacionRepository.save(existente);
        } else {
            nuevoDoc.setEstado(EstadoDocumento.PENDIENTE);
            return documentoPostulacionRepository.save(nuevoDoc);
        }
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
        DocumentoPostulacion actualizado = documentoPostulacionRepository.save(doc);

        log.info("📄 Estado del documento cambiado a {} para ID Postulacion {}", nuevoEstado, idPostulacion);

        Optional<Postulacion> postulacionOpt = postulacionRepository.findById(idPostulacion);

        if (postulacionOpt.isPresent()) {
            Postulacion postulacion = postulacionOpt.get();

            if (EstadoDocumento.ACEPTADO.name().equals(nuevoEstado)) {
                // ✅ Generar práctica
                Practica practica = new Practica();
                practica.setIdPersona(postulacion.getIdPersona());
                practica.setIdPostulacion(postulacion.getId());
                practica.setEstado(EstadoPractica.EN_PROCESO);

                try {
                    practicaClient.registrar(practica);
                    log.info("✅ Práctica generada automáticamente para persona {}", postulacion.getIdPersona());

                    postulacion.setEstado(EstadoPostulacion.ACEPTADA);
                    postulacion.setComentario("Documento aprobado.");
                    postulacionRepository.save(postulacion);

                } catch (Exception e) {
                    log.error("❌ Error al crear práctica automáticamente: {}", e.getMessage(), e);
                }

            } else if (EstadoDocumento.RECHAZADO.name().equals(nuevoEstado)) {
                postulacion.setEstado(EstadoPostulacion.EN_REVISION);
                postulacion.setComentario("Documento rechazado. Revisar formato o contenido.");
                postulacionRepository.save(postulacion);
            }

        } else {
            log.warn("No se encontró la postulación con ID {}", idPostulacion);
        }

        return actualizado;
    }
    @Override
    public DocumentoPostulacion actualizarEstadoConComentario(Long idPostulacion, String nuevoEstado, String comentario) {
        DocumentoPostulacion doc = documentoPostulacionRepository.findByIdPostulacion(idPostulacion)
                .orElseThrow(() -> new RuntimeException("No existe documento para esta postulación"));

        doc.setEstado(EstadoDocumento.valueOf(nuevoEstado));
        documentoPostulacionRepository.save(doc);

        Optional<Postulacion> postulacionOpt = postulacionRepository.findById(idPostulacion);

        if (postulacionOpt.isPresent()) {
            Postulacion postulacion = postulacionOpt.get();

            if (EstadoDocumento.RECHAZADO.name().equals(nuevoEstado)) {
                postulacion.setEstado(EstadoPostulacion.EN_REVISION);
                postulacion.setComentario(comentario);
                postulacionRepository.save(postulacion);
                log.info("📤 Documento RECHAZADO. Comentario guardado.");
            }

        } else {
            log.warn("⚠️ No se encontró la postulación con ID {}", idPostulacion);
        }

        return doc;
    }
}
