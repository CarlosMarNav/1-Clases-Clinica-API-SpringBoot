package progresa.proyectofinal_damc.controller;



import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.proyectofinal_damc.dao.EspecialidadDTO;
import progresa.proyectofinal_damc.dao.Mensaje;
import progresa.proyectofinal_damc.entity.Especialidad;
import progresa.proyectofinal_damc.service.EspecialidadService;


import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    // 1. LISTAR todas las especialidades
    @GetMapping("/lista")
    public ResponseEntity<List<Especialidad>> listarEspecialidades() {
        List<Especialidad> list = especialidadService.getAllEspecialidades();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 2. BUSCAR especialidad por ID
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (!especialidadService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la especialidad con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }
        return ResponseEntity.ok(especialidadService.findById(id));
    }

    // 3. CREAR especialidad
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody EspecialidadDTO especialidadDTO) {
        // Validación: Nombre obligatorio
        if (StringUtils.isBlank(especialidadDTO.getNombre())) {
            return new ResponseEntity<>(
                    new Mensaje("El nombre de la especialidad es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación: Especialidad no debe existir
        if (especialidadService.existsByNombre(especialidadDTO.getNombre())) {
            return new ResponseEntity<>(
                    new Mensaje("La especialidad '" + especialidadDTO.getNombre() + "' ya existe"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Crear especialidad
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(especialidadDTO.getNombre());
        especialidad.setDescripcion(especialidadDTO.getDescripcion());

        especialidadService.save(especialidad);

        return new ResponseEntity<>(
                new Mensaje("Especialidad creada correctamente"),
                HttpStatus.CREATED
        );
    }

    // 4. ELIMINAR especialidad
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!especialidadService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la especialidad con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }

        especialidadService.deleteById(id);

        return new ResponseEntity<>(
                new Mensaje("Especialidad eliminada correctamente"),
                HttpStatus.OK
        );
    }
}