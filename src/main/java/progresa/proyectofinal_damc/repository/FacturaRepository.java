package progresa.proyectofinal_damc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progresa.proyectofinal_damc.entity.EstadoPago;
import progresa.proyectofinal_damc.entity.Factura;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    // Verificar si existe una factura por número
    boolean existsByNumeroFactura(String numeroFactura);

    // Buscar factura por número
    Optional<Factura> findByNumeroFactura(String numeroFactura);

    // Buscar factura por cita ID
    Optional<Factura> findByCitaId(Long citaId);

    // Buscar facturas por estado de pago
    List<Factura> findByEstadoPago(EstadoPago estadoPago);

    // Verificar si una cita ya tiene factura
    boolean existsByCitaId(Long citaId);
}