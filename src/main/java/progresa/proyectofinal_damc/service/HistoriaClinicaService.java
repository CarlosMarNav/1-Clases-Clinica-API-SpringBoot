package progresa.proyectofinal_damc.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.proyectofinal_damc.entity.HistoriaClinica;
import progresa.proyectofinal_damc.repository.HistoriaClinicaRepository;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistoriaClinicaService {

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;



    // Verificar si existe por ID
    public boolean existsById(Long id) {
        return historiaClinicaRepository.existsById(id);
    }

    // Buscar por ID
    public Optional<HistoriaClinica> findById(Long id) {
        return historiaClinicaRepository.findById(id);
    }

    // Verificar si existe por número de historia
    public boolean existsByNumeroHistoria(String numeroHistoria) {
        return historiaClinicaRepository.existsByNumeroHistoria(numeroHistoria);
    }


    // Buscar por paciente ID
    public Optional<HistoriaClinica> findByPacienteId(Long pacienteId) {
        return historiaClinicaRepository.findByPacienteId(pacienteId);
    }

    // Verificar si un paciente ya tiene historia clínica
    public boolean existsByPacienteId(Long pacienteId) {
        return historiaClinicaRepository.existsByPacienteId(pacienteId);
    }

    // Crear o actualizar historia clínica
    public void save(HistoriaClinica historiaClinica) {
        historiaClinicaRepository.save(historiaClinica);
    }

    // Eliminar por ID
    public void deleteById(Long id) {
        historiaClinicaRepository.deleteById(id);
    }
}