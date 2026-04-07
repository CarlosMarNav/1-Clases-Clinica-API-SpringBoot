package progresa.proyectofinal_damc.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progresa.proyectofinal_damc.entity.HistoriaClinica;

import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {

    // Verificar si existe una historia clínica por número
    boolean existsByNumeroHistoria(String numeroHistoria);

    // Buscar historia clínica por número
    Optional<HistoriaClinica> findByNumeroHistoria(String numeroHistoria);

    // Buscar historia clínica por paciente ID
    Optional<HistoriaClinica> findByPacienteId(Long pacienteId);

    // Verificar si un paciente ya tiene historia clínica
    boolean existsByPacienteId(Long pacienteId);
}
