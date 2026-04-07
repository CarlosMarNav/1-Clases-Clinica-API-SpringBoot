package progresa.proyectofinal_damc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progresa.proyectofinal_damc.entity.EstadoPago;
import progresa.proyectofinal_damc.entity.Factura;
import progresa.proyectofinal_damc.repository.FacturaRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    // Listar todas las facturas
    public List<Factura> getAllFacturas() {
        return facturaRepository.findAll();
    }

    // Verificar si existe por ID
    public boolean existsById(Long id) {
        return facturaRepository.existsById(id);
    }

    // Buscar por ID
    public Optional<Factura> findById(Long id) {
        return facturaRepository.findById(id);
    }

    // Verificar si existe por número de factura
    public boolean existsByNumeroFactura(String numeroFactura) {
        return facturaRepository.existsByNumeroFactura(numeroFactura);
    }

    // Buscar por número de factura
    public Optional<Factura> findByNumeroFactura(String numeroFactura) {
        return facturaRepository.findByNumeroFactura(numeroFactura);
    }

    // Buscar factura por cita ID
    public Optional<Factura> findByCitaId(Long citaId) {
        return facturaRepository.findByCitaId(citaId);
    }

    // Buscar facturas por estado de pago
    public List<Factura> findByEstadoPago(EstadoPago estadoPago) {
        return facturaRepository.findByEstadoPago(estadoPago);
    }

    // Verificar si una cita ya tiene factura
    public boolean existsByCitaId(Long citaId) {
        return facturaRepository.existsByCitaId(citaId);
    }

    // Crear o actualizar factura
    public void save(Factura factura) {
        facturaRepository.save(factura);
    }

    // Eliminar por ID
    public void deleteById(Long id) {
        facturaRepository.deleteById(id);
    }
}