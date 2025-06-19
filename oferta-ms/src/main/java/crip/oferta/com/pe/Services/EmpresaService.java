package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Entities.Empresa;
import crip.oferta.com.pe.Entities.EstadoEmpresa;

import java.util.List;
import java.util.Optional;

public interface EmpresaService {
    Empresa guardar(Empresa empresa);
    Empresa updateEstado(Long id, EstadoEmpresa estado);
    List<Empresa> listarTodo();
    List<Empresa> listarPorEstado(EstadoEmpresa estado);
    Optional<Empresa> findById(Long id);
}