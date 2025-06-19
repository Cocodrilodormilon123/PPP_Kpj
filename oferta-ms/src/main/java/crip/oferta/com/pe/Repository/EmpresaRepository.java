package crip.oferta.com.pe.Repository;

import crip.oferta.com.pe.Entities.Empresa;
import crip.oferta.com.pe.Entities.EstadoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByEstado(EstadoEmpresa estado);
}