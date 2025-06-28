package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Entities.EstadoOferta;
import crip.oferta.com.pe.Entities.Oferta;
import crip.oferta.com.pe.Repository.OfertaRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OfertaServiceImpl implements OfertaService {

    private final OfertaRepository ofertaRepository;
    private final VacantesService vacantesService;

    public OfertaServiceImpl(OfertaRepository ofertaRepository, VacantesService vacantesService) {
        this.ofertaRepository = ofertaRepository;
        this.vacantesService = vacantesService;
    }

    @Override
    public Oferta guardar(Oferta oferta) {
        if (oferta.getFechaInicio() == null) {
            oferta.setFechaInicio(LocalDate.now());
        }

        if (oferta.getFechaFin() != null && oferta.getFechaFin().isBefore(oferta.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha de inicio.");
        }

        Oferta nueva = ofertaRepository.save(oferta);

        // crea vacantes con 0 cupos
        vacantesService.crearVacia(nueva.getId());

        return nueva;
    }

    @Override
    public Oferta updateEstado(Long id, EstadoOferta estado) {
        return ofertaRepository.findById(id)
                .map(oferta -> {
                    oferta.setEstado(estado);
                    return ofertaRepository.save(oferta);
                })
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada con ID: " + id));
    }

    @Override
    public List<Oferta> listarTodo() {
        List<Oferta> ofertas = ofertaRepository.findAll();

        for (Oferta oferta : ofertas) {
            if (oferta.getEstado() == EstadoOferta.ACTIVA &&
                    oferta.getFechaFin() != null &&
                    oferta.getFechaFin().isBefore(LocalDate.now())) {

                oferta.setEstado(EstadoOferta.FINALIZADA);
                ofertaRepository.save(oferta);
            }
        }

        return ofertas;
    }

    @Override
    public List<Oferta> listarPorEstado(EstadoOferta estado) {
        List<Oferta> ofertas = ofertaRepository.findByEstado(estado);

        for (Oferta oferta : ofertas) {
            if (oferta.getEstado() == EstadoOferta.ACTIVA &&
                    oferta.getFechaFin() != null &&
                    oferta.getFechaFin().isBefore(LocalDate.now())) {
                oferta.setEstado(EstadoOferta.FINALIZADA);
                ofertaRepository.save(oferta);
            }
        }

        return ofertaRepository.findByEstado(estado);
    }
    @Override
    public Optional<Oferta> findById(Long id) {
        return ofertaRepository.findById(id);
    }
    @Override
    public Oferta actualizarOferta(Long id, Oferta nueva) {
        return ofertaRepository.findById(id).map(existente -> {
            existente.setTitulo(nueva.getTitulo());
            existente.setDescripcion(nueva.getDescripcion());
            existente.setModalidad(nueva.getModalidad());
            existente.setEstado(nueva.getEstado());
            existente.setFechaFin(nueva.getFechaFin());
            existente.setEmpresa(nueva.getEmpresa());

            if (nueva.getFechaInicio() != null) {
                existente.setFechaInicio(nueva.getFechaInicio());
            }

            return ofertaRepository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Oferta no encontrada con ID: " + id));
    }



}
