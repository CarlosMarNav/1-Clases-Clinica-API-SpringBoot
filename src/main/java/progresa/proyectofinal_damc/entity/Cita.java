package progresa.proyectofinal_damc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cita")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Column(name = "hora", nullable = false)
    private String hora;

    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @Column(name = "diagnostico", length = 2000)
    private String diagnostico;

    @Column(name = "tratamiento", length = 2000)
    private String tratamiento;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    // RELACIÓN N-1 con Paciente (Muchas citas -> Un paciente)
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // RELACIÓN N-1 con Médico (Muchas citas -> Un médico)
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    // RELACIÓN N-1 con HistoriaClínica (Muchas citas -> Una historia)
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "historia_clinica_id", nullable = false)
    private HistoriaClinica historiaClinica;

    // RELACIÓN 1-1 con Factura (Una cita -> Una factura)
    @JsonIgnore
    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL)
    private Factura factura;
}