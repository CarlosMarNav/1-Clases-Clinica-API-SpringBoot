package progresa.proyectofinal_damc.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.proyectofinal_damc.dao.MedicoDTO;
import progresa.proyectofinal_damc.dao.Mensaje;
import progresa.proyectofinal_damc.entity.Especialidad;
import progresa.proyectofinal_damc.entity.Medico;
import progresa.proyectofinal_damc.service.EspecialidadService;
import progresa.proyectofinal_damc.service.MedicoService;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadService especialidadService;

    // 1. LISTAR todos los médicos
    @GetMapping("/lista")
    public ResponseEntity<List<Medico>> listarMedicos() {
        List<Medico> list = medicoService.getAllMedicos();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 2. BUSCAR médico por ID
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (!medicoService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el médico con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }
        return ResponseEntity.ok(medicoService.findById(id));
    }

    // 3. REGISTRAR médico (CASO DE USO OBLIGATORIO ✅)
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMedico(@RequestBody MedicoDTO medicoDTO) {
        // Validación 1: Nombre obligatorio
        if (StringUtils.isBlank(medicoDTO.getNombre())) {
            return new ResponseEntity<>(
                    new Mensaje("El nombre del médico es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 2: Apellidos obligatorios
        if (StringUtils.isBlank(medicoDTO.getApellidos())) {
            return new ResponseEntity<>(
                    new Mensaje("Los apellidos del médico son obligatorios"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 3: Número colegiado obligatorio
        if (StringUtils.isBlank(medicoDTO.getNumeroColegiado())) {
            return new ResponseEntity<>(
                    new Mensaje("El número colegiado es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 4: Número colegiado no debe existir
        if (medicoService.existsByNumeroColegiado(medicoDTO.getNumeroColegiado())) {
            return new ResponseEntity<>(
                    new Mensaje("Ya existe un médico con el número colegiado: " + medicoDTO.getNumeroColegiado()),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 5: Especialidad debe existir
        if (!especialidadService.existsById(medicoDTO.getEspecialidadId())) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la especialidad con el ID: " + medicoDTO.getEspecialidadId()),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Obtener la especialidad
        Optional<Especialidad> especialidadOpt = especialidadService.findById(medicoDTO.getEspecialidadId());
        if (especialidadOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("Error al obtener la especialidad"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        // Crear médico
        Medico medico = new Medico();
        medico.setNombre(medicoDTO.getNombre());
        medico.setApellidos(medicoDTO.getApellidos());
        medico.setNumeroColegiado(medicoDTO.getNumeroColegiado());
        medico.setTelefono(medicoDTO.getTelefono());
        medico.setEmail(medicoDTO.getEmail());
        medico.setEspecialidad(especialidadOpt.get());

        medicoService.save(medico);

        return new ResponseEntity<>(
                new Mensaje("Médico registrado correctamente"),
                HttpStatus.CREATED
        );
    }

    // 4. ELIMINAR médico
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!medicoService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el médico con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }

        medicoService.deleteById(id);

        return new ResponseEntity<>(
                new Mensaje("Médico eliminado correctamente"),
                HttpStatus.OK
        );
    }

    // 5. LISTAR médicos por especialidad
    @GetMapping("/porEspecialidad/{especialidadId}")
    public ResponseEntity<?> listarPorEspecialidad(@PathVariable Long especialidadId) {
        if (!especialidadService.existsById(especialidadId)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la especialidad con el ID: " + especialidadId),
                    HttpStatus.NOT_FOUND
            );
        }

        List<Medico> medicos = medicoService.findByEspecialidadId(especialidadId);
        return new ResponseEntity<>(medicos, HttpStatus.OK);
    }
}