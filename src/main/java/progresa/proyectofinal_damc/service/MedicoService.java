package progresa.proyectofinal_damc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.proyectofinal_damc.entity.Medico;
import progresa.proyectofinal_damc.repository.MedicoRepository;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    // Listar todos los médicos
    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll();
    }

    // Verificar si existe por ID
    public boolean existsById(Long id) {
        return medicoRepository.existsById(id);
    }

    // Buscar por ID
    public Optional<Medico> findById(Long id) {
        return medicoRepository.findById(id);
    }

    // Verificar si existe por número colegiado
    public boolean existsByNumeroColegiado(String numeroColegiado) {
        return medicoRepository.existsByNumeroColegiado(numeroColegiado);
    }


    // Buscar médicos por especialidad
    public List<Medico> findByEspecialidadId(Long especialidadId) {
        return medicoRepository.findByEspecialidadId(especialidadId);
    }

    // Crear o actualizar médico
    public void save(Medico medico) {
        medicoRepository.save(medico);
    }

    // Eliminar por ID
    public void deleteById(Long id) {
        medicoRepository.deleteById(id);
    }
}
