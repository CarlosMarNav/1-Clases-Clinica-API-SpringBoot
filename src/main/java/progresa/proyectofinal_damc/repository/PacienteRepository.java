package progresa.proyectofinal_damc.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progresa.proyectofinal_damc.entity.Paciente;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Verificar si existe un paciente por DNI
    boolean existsByDni(String dni);

    // Buscar paciente por DNI
    Optional<Paciente> findByDni(String dni);

    // Buscar paciente por email
    Optional<Paciente> findByEmail(String email);
}