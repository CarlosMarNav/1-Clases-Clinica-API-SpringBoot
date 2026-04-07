package progresa.proyectofinal_damc.dao;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacturaDTO {

    private String numeroFactura;
    private String fechaEmision;
    private Double monto;
    private String metodoPago;
    private Long citaId; // ID de la cita que generó la factura
}