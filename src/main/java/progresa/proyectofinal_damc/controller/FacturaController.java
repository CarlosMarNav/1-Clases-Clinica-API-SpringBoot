package progresa.proyectofinal_damc.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progresa.proyectofinal_damc.dao.FacturaDTO;
import progresa.proyectofinal_damc.dao.Mensaje;
import progresa.proyectofinal_damc.entity.Cita;
import progresa.proyectofinal_damc.entity.EstadoCita;
import progresa.proyectofinal_damc.entity.EstadoPago;
import progresa.proyectofinal_damc.entity.Factura;
import progresa.proyectofinal_damc.service.CitaService;
import progresa.proyectofinal_damc.service.FacturaService;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private CitaService citaService;

    // 1. LISTAR todas las facturas
    @GetMapping("/lista")
    public ResponseEntity<List<Factura>> listarFacturas() {
        List<Factura> list = facturaService.getAllFacturas();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 2. BUSCAR factura por ID
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (!facturaService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la factura con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }
        return ResponseEntity.ok(facturaService.findById(id));
    }

    // 3. GENERAR FACTURA (CASO DE USO OBLIGATORIO ✅)
    @PostMapping("/generar")
    public ResponseEntity<?> generarFactura(@RequestBody FacturaDTO facturaDTO) {

        // ========== VALIDACIONES ==========

        // Validación 1: Número de factura obligatorio
        if (StringUtils.isBlank(facturaDTO.getNumeroFactura())) {
            return new ResponseEntity<>(
                    new Mensaje("El número de factura es obligatorio"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 2: Número de factura no debe existir
        if (facturaService.existsByNumeroFactura(facturaDTO.getNumeroFactura())) {
            return new ResponseEntity<>(
                    new Mensaje("Ya existe una factura con el número: " + facturaDTO.getNumeroFactura()),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 3: Monto obligatorio y debe ser mayor a 0
        if (facturaDTO.getMonto() == null || facturaDTO.getMonto() <= 0) {
            return new ResponseEntity<>(
                    new Mensaje("El monto debe ser mayor a 0"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 4: La cita debe existir
        if (!citaService.existsById(facturaDTO.getCitaId())) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la cita con el ID: " + facturaDTO.getCitaId()),
                    HttpStatus.NOT_FOUND
            );
        }

        // Validación 5: La cita NO debe tener factura (Relación 1-1)
        if (facturaService.existsByCitaId(facturaDTO.getCitaId())) {
            return new ResponseEntity<>(
                    new Mensaje("La cita ya tiene una factura generada"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validación 6: La cita debe estar COMPLETADA
        Optional<Cita> citaOpt = citaService.findById(facturaDTO.getCitaId());
        if (citaOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("Error al obtener la cita"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        Cita cita = citaOpt.get();

        if (cita.getEstado() != EstadoCita.COMPLETADA) {
            return new ResponseEntity<>(
                    new Mensaje("Solo se pueden facturar citas completadas"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // ========== CREAR FACTURA ==========

        Factura factura = new Factura();
        factura.setNumeroFactura(facturaDTO.getNumeroFactura());
        factura.setFechaEmision(facturaDTO.getFechaEmision());
        factura.setMonto(facturaDTO.getMonto());
        factura.setMetodoPago(facturaDTO.getMetodoPago());
        factura.setEstadoPago(EstadoPago.PENDIENTE); // Estado inicial
        factura.setCita(cita);

        facturaService.save(factura);

        return new ResponseEntity<>(
                new Mensaje("Factura generada correctamente"),
                HttpStatus.CREATED
        );
    }

    // 4. MARCAR FACTURA COMO PAGADA
    @PutMapping("/marcarPagada/{id}")
    public ResponseEntity<?> marcarComoPagada(@PathVariable Long id) {

        // Validación 1: La factura debe existir
        if (!facturaService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la factura con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }

        // Obtener la factura
        Optional<Factura> facturaOpt = facturaService.findById(id);
        if (facturaOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("Error al obtener la factura"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        Factura factura = facturaOpt.get();

        // Validación 2: La factura NO debe estar ya pagada
        if (factura.getEstadoPago() == EstadoPago.PAGADA) {
            return new ResponseEntity<>(
                    new Mensaje("La factura ya está marcada como pagada"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Cambiar estado a PAGADA
        factura.setEstadoPago(EstadoPago.PAGADA);
        facturaService.save(factura);

        return new ResponseEntity<>(
                new Mensaje("Factura marcada como pagada correctamente"),
                HttpStatus.OK
        );
    }

    // 5. BUSCAR factura por cita ID
    @GetMapping("/porCita/{citaId}")
    public ResponseEntity<?> buscarPorCitaId(@PathVariable Long citaId) {
        if (!citaService.existsById(citaId)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la cita con el ID: " + citaId),
                    HttpStatus.NOT_FOUND
            );
        }

        Optional<Factura> facturaOpt = facturaService.findByCitaId(citaId);

        if (facturaOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("La cita aún no tiene factura generada"),
                    HttpStatus.NOT_FOUND
            );
        }

        return ResponseEntity.ok(facturaOpt.get());
    }

    // 6. LISTAR facturas por estado de pago
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> listarPorEstadoPago(@PathVariable String estado) {
        EstadoPago estadoPago;

        try {
            estadoPago = EstadoPago.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new Mensaje("Estado no válido. Use: PENDIENTE o PAGADA"),
                    HttpStatus.BAD_REQUEST
            );
        }

        List<Factura> facturas = facturaService.findByEstadoPago(estadoPago);
        return new ResponseEntity<>(facturas, HttpStatus.OK);
    }

    // 7. ELIMINAR factura
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!facturaService.existsById(id)) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la factura con el ID: " + id),
                    HttpStatus.NOT_FOUND
            );
        }

        facturaService.deleteById(id);

        return new ResponseEntity<>(
                new Mensaje("Factura eliminada correctamente"),
                HttpStatus.OK
        );
    }

    @GetMapping("/buscarPorNumero/{numeroFactura}")
    public ResponseEntity<?> buscarPorNumeroFactura(@PathVariable String numeroFactura) {
        Optional<Factura> factura = facturaService.findByNumeroFactura(numeroFactura);

        if (factura.isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("No existe la factura con el número: " + numeroFactura),
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(factura.get(), HttpStatus.OK);
    }
}