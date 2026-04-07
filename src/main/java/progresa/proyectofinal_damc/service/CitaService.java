package progresa.proyectofinal_damc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.proyectofinal_damc.entity.Cita;
import progresa.proyectofinal_damc.entity.EstadoCita;
import progresa.proyectofinal_damc.repository.CitaRepository;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    // Listar todas las citas
    public List<Cita> getAllCitas() {
        return citaRepository.findAll();
    }

    // Verificar si existe por ID
    public boolean existsById(Long id) {
        return citaRepository.existsById(id);
    }

    // Buscar por ID
    public Optional<Cita> findById(Long id) {
        return citaRepository.findById(id);
    }

    // Buscar citas de un paciente
    public List<Cita> findByPacienteId(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    // Buscar citas de un médico
    public List<Cita> findByMedicoId(Long medicoId) {
        return citaRepository.findByMedicoId(medicoId);
    }

    // Buscar citas por estado
    public List<Cita> findByEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado);
    }


    // Buscar citas de un médico en una fecha
    public List<Cita> findByMedicoIdAndFecha(Long medicoId, String fecha) {
        return citaRepository.findByMedicoIdAndFecha(medicoId, fecha);
    }

    // Verificar si existe cita en fecha/hora para un médico (evitar duplicados)
    public boolean existsByMedicoIdAndFechaAndHora(Long medicoId, String fecha, String hora) {
        return citaRepository.existsByMedicoIdAndFechaAndHora(medicoId, fecha, hora);
    }

    // Crear o actualizar cita
    public void save(Cita cita) {
        citaRepository.save(cita);
    }

    // Eliminar por ID
    public void deleteById(Long id) {
        citaRepository.deleteById(id);
    }
}