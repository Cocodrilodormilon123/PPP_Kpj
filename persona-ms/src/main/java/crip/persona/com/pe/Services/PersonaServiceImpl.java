package crip.persona.com.pe.Services;

import crip.persona.com.pe.Entities.Persona;
import crip.persona.com.pe.Entities.TipoPersona;
import crip.persona.com.pe.Repository.PersonaRepository;
import crip.persona.com.pe.dto.RegisterAuthRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final RestTemplate restTemplate;

    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Persona guardar(Persona persona) {
        Persona guardada = personaRepository.save(persona);

        if (guardada.getCodigo() != null && guardada.getDni() != null && guardada.getTipoPersona() != null) {
            RegisterAuthRequest authRequest = new RegisterAuthRequest();
            authRequest.setUsername(guardada.getCodigo());
            authRequest.setPassword(guardada.getDni());
            authRequest.setRole(guardada.getTipoPersona().toString());

            try {
                restTemplate.postForObject(
                        "http://localhost:3030/auth-server/auth/register",
                        authRequest,
                        Void.class
                );
            } catch (Exception e) {
                System.err.println("Error al registrar en auth-server: " + e.getMessage());
            }
        }

        return guardada;
    }

    @Override
    public Persona actualizar(Long id, Persona persona) {
        return personaRepository.findById(id).map(actual -> {

            if (persona.getDni() != null) actual.setDni(persona.getDni());
            if (persona.getEstado() != null) actual.setEstado(persona.getEstado());
            if (persona.getFechaRegistro() != null) actual.setFechaRegistro(persona.getFechaRegistro());
            if (persona.getNombre() != null) actual.setNombre(persona.getNombre());
            if (persona.getApellido() != null) actual.setApellido(persona.getApellido());
            if (persona.getTelefono() != null) actual.setTelefono(persona.getTelefono());
            if (persona.getCodigo() != null) actual.setCodigo(persona.getCodigo());
            if (persona.getEp() != null) actual.setEp(persona.getEp());
            if (persona.getTipoPersona() != null) actual.setTipoPersona(persona.getTipoPersona());

            return personaRepository.save(actual);

        }).orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));
    }

    @Override
    public List<Persona> listarTodo() {
        return personaRepository.findAll();
    }

    @Override
    public List<Persona> listarPorTipo(TipoPersona tipoPersona) {
        return personaRepository.findByTipoPersona(tipoPersona);
    }

    @Override
    public Optional<Persona> findById(Long id) {
        return personaRepository.findById(id);
    }
    @Override
    public Optional<Persona> findByCodigo(String codigo) {
        return personaRepository.findByCodigo(codigo);
    }
}