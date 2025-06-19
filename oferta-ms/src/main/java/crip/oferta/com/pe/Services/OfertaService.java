package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Entities.EstadoOferta;
import crip.oferta.com.pe.Entities.Oferta;

import java.util.List;
import java.util.Optional;

public interface OfertaService {
    Oferta guardar(Oferta oferta);
    Oferta updateEstado(Long id, EstadoOferta estado);
    List<Oferta> listarTodo();
    List<Oferta> listarPorEstado(EstadoOferta estado);
    Optional<Oferta> findById(Long id);
    Oferta actualizarOferta(Long id, Oferta nueva);
}