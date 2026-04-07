package progresa.proyectofinal_damc.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progresa.proyectofinal_damc.entity.Medico;


import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // Verificar si existe un médico por número colegiado
    boolean existsByNumeroColegiado(String numeroColegiado);

    // Buscar médico por número colegiado
    Optional<Medico> findByNumeroColegiado(String numeroColegiado);

    // Buscar médico por email
    Optional<Medico> findByEmail(String email);

    // Buscar todos los médicos de una especialidad
    List<Medico> findByEspecialidadId(Long especialidadId);
}
