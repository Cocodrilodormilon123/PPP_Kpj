package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Entities.Oferta;
import crip.oferta.com.pe.Entities.Vacantes;
import crip.oferta.com.pe.Repository.VacantesRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VacantesServiceImpl implements VacantesService {

    private final VacantesRepository vacantesRepository;

    public VacantesServiceImpl(VacantesRepository vacantesRepository) {
        this.vacantesRepository = vacantesRepository;
    }

    @Override
    public Vacantes guardar(Vacantes vacantes) {
        if (vacantes.getOcupados() > vacantes.getTotal()) {
            throw new IllegalArgumentException("Los cupos ocupados no pueden superar el total.");
        }

        vacantes.setDisponibles(vacantes.getTotal() - vacantes.getOcupados());

        System.out.println("🔁 Guardando vacantes ID=" + vacantes.getId() + " | Ocupados=" + vacantes.getOcupados() + " | Total=" + vacantes.getTotal());

        return vacantesRepository.save(vacantes);
    }

    @Override
    public Vacantes updateCupos(Long id, int ocupados) {
        return vacantesRepository.findById(id).map(v -> {
            if (ocupados > v.getTotal()) {
                throw new IllegalArgumentException("No se pueden ocupar más cupos que el total disponible.");
            }
            v.setOcupados(ocupados);
            v.setDisponibles(v.getTotal() - ocupados);
            return vacantesRepository.save(v);
        }).orElseThrow(() -> new RuntimeException("Vacantes no encontrada con ID: " + id));
    }

    @Override
    public Optional<Vacantes> findById(Long id) {
        return vacantesRepository.findById(id);
    }

    @Override
    public Optional<Vacantes> findByOfertaId(Long ofertaId) {
        return vacantesRepository.findByOfertaId(ofertaId);
    }

    @Override
    public List<Vacantes> listarTodo() {
        return vacantesRepository.findAll();
    }

    @Override
    public Vacantes crearVacia(Long idOferta) {
        Vacantes v = new Vacantes();
        v.setTotal(0);
        v.setOcupados(0);
        v.setDisponibles(0);

        Oferta oferta = new Oferta();
        oferta.setId(idOferta);
        v.setOferta(oferta);

        return vacantesRepository.save(v);
    }
}
