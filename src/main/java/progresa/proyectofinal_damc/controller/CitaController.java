package progresa.proyectofinal_damc.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.proyectofinal_damc.dao.CitaActualizarDTO;
import progresa.proyectofinal_damc.dao.CitaDTO;
import progresa.proyectofinal_damc.dao.Mensaje;
import progresa.proyectofinal_damc.entity.*;
import progresa.proyectofinal_damc.service.CitaService;
import progresa.proyectofinal_damc.service.HistoriaClinicaService;
import progresa.proyectofinal_damc.service.MedicoService;
import progresa.proyectofinal_damc.service.PacienteService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private HistoriaClinicaService historiaClinicaService;

    // 1. LISTAR todas las citas
    @GetMapping("/lista")
    public ResponseEntity<List<Cita>> listarCitas() {
        List<Cita> list = citaService.getAllCitas();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 2. BUSCAR cita por ID
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (!citaService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la cita con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }
        return ResponseEntity.ok(citaService.findById(id));
    }

    // 3. AGENDAR CITA (CASO DE USO OBLIGATORIO ✅)
    @PostMapping("/agendar")
    public ResponseEntity<?> agendarCita(@RequestBody CitaDTO citaDTO) {

        // ========== VALIDACIONES ==========

        // Validación 1: Fecha obligatoria
        if (StringUtils.isBlank(citaDTO.getFecha())) {
            return new ResponseEntity<>(
                    new Mensaje("La fecha es obligatoria"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 2: Hora obligatoria
        if (StringUtils.isBlank(citaDTO.getHora())) {
            return new ResponseEntity<>(
                    new Mensaje("La hora es obligatoria"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 3: Motivo obligatorio
        if (StringUtils.isBlank(citaDTO.getMotivo())) {
            return new ResponseEntity<>(
                    new Mensaje("El motivo de la cita es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 4: Paciente debe existir
        if (!pacienteService.existsById(citaDTO.getPacienteId())) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el paciente con el ID: " + citaDTO.getPacienteId()),
                    HttpStatus.NOT_FOUND
            );
        }

        // Validación 5: Médico debe existir
        if (!medicoService.existsById(citaDTO.getMedicoId())) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el médico con el ID: " + citaDTO.getMedicoId()),
                    HttpStatus.NOT_FOUND
            );
        }

        // Validación 6: Paciente debe tener historia clínica
        if (!historiaClinicaService.existsByPacienteId(citaDTO.getPacienteId())) {
            return new ResponseEntity<>(
                    new Mensaje("El paciente debe tener una historia clínica antes de agendar una cita"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 7: El médico NO debe tener otra cita a la misma hora
        if (citaService.existsByMedicoIdAndFechaAndHora(
                citaDTO.getMedicoId(),
                citaDTO.getFecha(),
                citaDTO.getHora())) {
            return new ResponseEntity<>(
                    new Mensaje("El médico ya tiene una cita agendada en esa fecha y hora"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // ========== OBTENER ENTIDADES ==========

        Optional<Paciente> pacienteOpt = pacienteService.findById(citaDTO.getPacienteId());
        Optional<Medico> medicoOpt = medicoService.findById(citaDTO.getMedicoId());
        Optional<HistoriaClinica> historiaOpt = historiaClinicaService.findByPacienteId(citaDTO.getPacienteId());

        if (pacienteOpt.isEmpty() || medicoOpt.isEmpty() || historiaOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("Error al obtener los datos necesarios para agendar la cita"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        // ========== CREAR CITA ==========

        Cita cita = new Cita();
        cita.setFecha(citaDTO.getFecha());
        cita.setHora(citaDTO.getHora());
        cita.setMotivo(citaDTO.getMotivo());
        cita.setPaciente(pacienteOpt.get());
        cita.setMedico(medicoOpt.get());
        cita.setHistoriaClinica(historiaOpt.get());
        cita.setEstado(EstadoCita.PENDIENTE); // Estado inicial

        citaService.save(cita);

        return new ResponseEntity<>(
                new Mensaje("Cita agendada correctamente"),
                HttpStatus.CREATED
        );
    }

    // 4. CANCELAR CITA (CASO DE USO OBLIGATORIO ✅)
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarCita(@PathVariable Long id) {

        // Validación 1: La cita debe existir
        if (!citaService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la cita con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }

        // Obtener la cita
        Optional<Cita> citaOpt = citaService.findById(id);
        if (citaOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("Error al obtener la cita"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        Cita cita = citaOpt.get();

        // Validación 2: La cita NO debe estar ya cancelada
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            return new ResponseEntity<>(
                    new Mensaje("La cita ya está cancelada"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 3: La cita NO debe estar completada
        if (cita.getEstado() == EstadoCita.COMPLETADA) {
            return new ResponseEntity<>(
                    new Mensaje("No se puede cancelar una cita que ya fue completada"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Cambiar estado a CANCELADA
        cita.setEstado(EstadoCita.CANCELADA);
        citaService.save(cita);

        return new ResponseEntity<>(
                new Mensaje("Cita cancelada correctamente"),
                HttpStatus.OK
        );
    }

    // 5. REGISTRAR DIAGNÓSTICO (CASO DE USO OBLIGATORIO ✅)
    @PutMapping("/registrarDiagnostico/{id}")
    public ResponseEntity<?> registrarDiagnostico(
            @PathVariable Long id,
            @RequestBody CitaActualizarDTO citaActualizarDTO) {

        // Validación 1: La cita debe existir
        if (!citaService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la cita con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }

        // Validación 2: Diagnóstico obligatorio
        if (StringUtils.isBlank(citaActualizarDTO.getDiagnostico())) {
            return new ResponseEntity<>(
                    new Mensaje("El diagnóstico es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Obtener la cita
        Optional<Cita> citaOpt = citaService.findById(id);
        if (citaOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("Error al obtener la cita"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        Cita cita = citaOpt.get();

        // Validación 3: La cita debe estar PENDIENTE
        if (cita.getEstado() != EstadoCita.PENDIENTE) {
            return new ResponseEntity<>(
                    new Mensaje("Solo se puede registrar diagnóstico en citas pendientes"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Actualizar diagnóstico y tratamiento
        cita.setDiagnostico(citaActualizarDTO.getDiagnostico());
        cita.setTratamiento(citaActualizarDTO.getTratamiento());
        cita.setEstado(EstadoCita.COMPLETADA); // Cambiar estado a completada

        citaService.save(cita);

        return new ResponseEntity<>(
                new Mensaje("Diagnóstico registrado correctamente"),
                HttpStatus.OK
        );
    }

    // 6. LISTAR citas de un paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<?> listarCitasPorPaciente(@PathVariable Long pacienteId) {
        if (!pacienteService.existsById(pacienteId)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el paciente con el ID: " + pacienteId),
                    HttpStatus.NOT_FOUND
            );
        }

        List<Cita> citas = citaService.findByPacienteId(pacienteId);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    // 7. LISTAR citas de un médico
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<?> listarCitasPorMedico(@PathVariable Long medicoId) {
        if (!medicoService.existsById(medicoId)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el médico con el ID: " + medicoId),
                    HttpStatus.NOT_FOUND
            );
        }

        List<Cita> citas = citaService.findByMedicoId(medicoId);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    // 8. LISTAR citas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> listarCitasPorEstado(@PathVariable String estado) {
        EstadoCita estadoCita;

        try {
            estadoCita = EstadoCita.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new Mensaje("Estado no válido. Use: PENDIENTE, COMPLETADA o CANCELADA"),
                    HttpStatus.BAD_REQUEST
            );
        }

        List<Cita> citas = citaService.findByEstado(estadoCita);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    // 9. LISTAR citas de un médico en una fecha específica
    @GetMapping("/medico/{medicoId}/fecha/{fecha}")
    public ResponseEntity<?> listarCitasPorMedicoYFecha(
            @PathVariable Long medicoId,
            @PathVariable String fecha) {

        if (!medicoService.existsById(medicoId)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe el médico con el ID: " + medicoId),
                    HttpStatus.NOT_FOUND
            );
        }

        List<Cita> citas = citaService.findByMedicoIdAndFecha(medicoId, fecha);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminarCita(@PathVariable Long id) {
        // Validación: La cita debe existir
        if (!citaService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la cita con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }

        // Eliminar la cita
        citaService.deleteById(id);

        return new ResponseEntity<>(
                new Mensaje("Cita eliminada correctamente"),
                HttpStatus.OK
        );
    }
}