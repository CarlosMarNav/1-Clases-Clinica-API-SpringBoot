package progresa.proyectofinal_damc.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.proyectofinal_damc.dao.HistoriaClinicaDTO;
import progresa.proyectofinal_damc.dao.Mensaje;
import progresa.proyectofinal_damc.dao.PacienteDTO;
import progresa.proyectofinal_damc.entity.HistoriaClinica;
import progresa.proyectofinal_damc.entity.Paciente;
import progresa.proyectofinal_damc.service.HistoriaClinicaService;
import progresa.proyectofinal_damc.service.PacienteService;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private HistoriaClinicaService historiaClinicaService;

    // 1. LISTAR todos los pacientes
    @GetMapping("/lista")
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> list = pacienteService.getAllPacientes();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 2. BUSCAR paciente por ID
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (!pacienteService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el paciente con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }
        return ResponseEntity.ok(pacienteService.findById(id));
    }

    // 3. REGISTRAR paciente (CASO DE USO OBLIGATORIO ✅)
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPaciente(@RequestBody PacienteDTO pacienteDTO) {
        // Validación 1: Nombre obligatorio
        if (StringUtils.isBlank(pacienteDTO.getNombre())) {
            return new ResponseEntity<>(
                    new Mensaje("El nombre del paciente es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 2: Apellidos obligatorios
        if (StringUtils.isBlank(pacienteDTO.getApellidos())) {
            return new ResponseEntity<>(
                    new Mensaje("Los apellidos del paciente son obligatorios"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 3: DNI obligatorio
        if (StringUtils.isBlank(pacienteDTO.getDni())) {
            return new ResponseEntity<>(
                    new Mensaje("El DNI del paciente es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 4: DNI no debe existir
        if (pacienteService.existsByDni(pacienteDTO.getDni())) {
            return new ResponseEntity<>(
                    new Mensaje("Ya existe un paciente con el DNI: " + pacienteDTO.getDni()),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Crear paciente
        Paciente paciente = new Paciente();
        paciente.setNombre(pacienteDTO.getNombre());
        paciente.setApellidos(pacienteDTO.getApellidos());
        paciente.setDni(pacienteDTO.getDni());
        paciente.setFechaNacimiento(pacienteDTO.getFechaNacimiento());
        paciente.setTelefono(pacienteDTO.getTelefono());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setDireccion(pacienteDTO.getDireccion());

        pacienteService.save(paciente);

        return new ResponseEntity<>(
                new Mensaje("Paciente registrado correctamente"),
                HttpStatus.CREATED
        );
    }

    // 4. CREAR historia clínica para un paciente (RELACIÓN 1-1)
    @PostMapping("/{pacienteId}/crearHistoriaClinica")
    public ResponseEntity<?> crearHistoriaClinica(
            @PathVariable Long pacienteId,
            @RequestBody HistoriaClinicaDTO historiaClinicaDTO) {

        // Validación 1: Paciente debe existir
        if (!pacienteService.existsById(pacienteId)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el paciente con el ID: " + pacienteId),
                    HttpStatus.NOT_FOUND
            );
        }

        // Validación 2: Paciente NO debe tener historia clínica (Relación 1-1)
        if (historiaClinicaService.existsByPacienteId(pacienteId)) {
            return new ResponseEntity<>(
                    new Mensaje("El paciente ya tiene una historia clínica asignada"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 3: Número de historia obligatorio
        if (StringUtils.isBlank(historiaClinicaDTO.getNumeroHistoria())) {
            return new ResponseEntity<>(
                    new Mensaje("El número de historia clínica es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 4: Número de historia no debe existir
        if (historiaClinicaService.existsByNumeroHistoria(historiaClinicaDTO.getNumeroHistoria())) {
            return new ResponseEntity<>(
                    new Mensaje("Ya existe una historia clínica con el número: " + historiaClinicaDTO.getNumeroHistoria()),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Obtener paciente
        Optional<Paciente> pacienteOpt = pacienteService.findById(pacienteId);
        if (pacienteOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("Error al obtener el paciente"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        // Crear historia clínica
        HistoriaClinica historiaClinica = new HistoriaClinica();
        historiaClinica.setNumeroHistoria(historiaClinicaDTO.getNumeroHistoria());
        historiaClinica.setGrupoSanguineo(historiaClinicaDTO.getGrupoSanguineo());
        historiaClinica.setAlergias(historiaClinicaDTO.getAlergias());
        historiaClinica.setEnfermedadesCronicas(historiaClinicaDTO.getEnfermedadesCronicas());
        historiaClinica.setObservaciones(historiaClinicaDTO.getObservaciones());
        historiaClinica.setPaciente(pacienteOpt.get());

        historiaClinicaService.save(historiaClinica);

        return new ResponseEntity<>(
                new Mensaje("Historia clínica creada correctamente"),
                HttpStatus.CREATED
        );
    }

    // 5. ELIMINAR paciente
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!pacienteService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el paciente con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }

        pacienteService.deleteById(id);

        return new ResponseEntity<>(
                new Mensaje("Paciente eliminado correctamente"),
                HttpStatus.OK
        );
    }


    @GetMapping("/buscarPorDni/{dni}")
    public ResponseEntity<?> buscarPorDni(@PathVariable String dni) {
        Optional<Paciente> paciente = pacienteService.findByDni(dni);

        if (paciente.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el paciente con el DNI: " + dni),
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(paciente.get(), HttpStatus.OK);
    }
}