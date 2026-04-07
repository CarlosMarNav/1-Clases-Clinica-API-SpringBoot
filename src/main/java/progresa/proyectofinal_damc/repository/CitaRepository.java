package progresa.proyectofinal_damc.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progresa.proyectofinal_damc.entity.Cita;
import progresa.proyectofinal_damc.entity.EstadoCita;


import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Buscar todas las citas de un paciente
    List<Cita> findByPacienteId(Long pacienteId);

    // Buscar todas las citas de un médico
    List<Cita> findByMedicoId(Long medicoId);

    // Buscar citas por estado
    List<Cita> findByEstado(EstadoCita estado);

    // Buscar citas de un médico en una fecha específica
    List<Cita> findByMedicoIdAndFecha(Long medicoId, String fecha);

    // Verificar si existe una cita en una fecha y hora específica para un médico
    boolean existsByMedicoIdAndFechaAndHora(Long medicoId, String fecha, String hora);
}