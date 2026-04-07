package progresa.proyectofinal_damc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "factura")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura", nullable = false, unique = true)
    private String numeroFactura;

    @Column(name = "fecha_emision", nullable = false)
    private String fechaEmision;

    @Column(name = "monto", nullable = false)
    private Double monto;

    @Column(name = "estado_pago", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;

    @Column(name = "metodo_pago")
    private String metodoPago;

    // RELACIÓN 1-1 con Cita (Una factura -> Una cita)
    @OneToOne
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    @JsonBackReference
    private Cita cita;
}