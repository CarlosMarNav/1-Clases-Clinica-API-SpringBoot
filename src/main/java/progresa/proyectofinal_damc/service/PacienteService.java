package progresa.proyectofinal_damc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.proyectofinal_damc.entity.Paciente;
import progresa.proyectofinal_damc.repository.PacienteRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    // Listar todos los pacientes
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    // Verificar si existe por ID
    public boolean existsById(Long id) {
        return pacienteRepository.existsById(id);
    }

    // Buscar por ID
    public Optional<Paciente> findById(Long id) {
        return pacienteRepository.findById(id);
    }

    // Verificar si existe por DNI
    public boolean existsByDni(String dni) {
        return pacienteRepository.existsByDni(dni);
    }

    // Buscar por DNI
    public Optional<Paciente> findByDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    // Crear o actualizar paciente
    public void save(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    // Eliminar por ID
    public void deleteById(Long id) {
        pacienteRepository.deleteById(id);
    }
}