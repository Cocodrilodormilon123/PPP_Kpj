package crip.oferta.com.pe.Services;

import crip.oferta.com.pe.Entities.Empresa;
import crip.oferta.com.pe.Entities.EstadoEmpresa;
import crip.oferta.com.pe.Repository.EmpresaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public Empresa guardar(Empresa empresa) {
        String ruc = empresa.getRuc();

        // Validar longitud y formato del RUC
        if (!ruc.matches("^(10|20)\\d{9}$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El RUC debe empezar con 10 o 20 y tener 11 dígitos numéricos."
            );
        }

        // Verificar si el RUC ya está registrado
        if (empresaRepository.existsByRuc(ruc)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Este RUC ya está registrado."
            );
        }

        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa updateEstado(Long id, EstadoEmpresa estado) {
        return empresaRepository.findById(id)
                .map(empresa -> {
                    empresa.setEstado(estado);
                    return empresaRepository.save(empresa);
                })
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con ID: " + id));
    }

    @Override
    public List<Empresa> listarTodo() {
        return empresaRepository.findAll();
    }

    @Override
    public List<Empresa> listarPorEstado(EstadoEmpresa estado) {
        return empresaRepository.findByEstado(estado);
    }

    @Override
    public Optional<Empresa> findById(Long id) {
        return empresaRepository.findById(id);
    }
}