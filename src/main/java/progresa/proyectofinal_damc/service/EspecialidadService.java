package progresa.proyectofinal_damc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.proyectofinal_damc.entity.Especialidad;
import progresa.proyectofinal_damc.repository.EspecialidadRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    // Listar todas las especialidades
    public List<Especialidad> getAllEspecialidades() {
        return especialidadRepository.findAll();
    }

    // Verificar si existe por ID
    public boolean existsById(Long id) {
        return especialidadRepository.existsById(id);
    }

    // Buscar por ID
    public Optional<Especialidad> findById(Long id) {
        return especialidadRepository.findById(id);
    }

    // Verificar si existe por nombre
    public boolean existsByNombre(String nombre) {
        return especialidadRepository.existsByNombre(nombre);
    }


    // Crear o actualizar especialidad
    public void save(Especialidad especialidad) {
        especialidadRepository.save(especialidad);
    }

    // Eliminar por ID
    public void deleteById(Long id) {
        especialidadRepository.deleteById(id);
    }
}
