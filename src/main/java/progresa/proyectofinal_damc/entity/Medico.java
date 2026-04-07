package progresa.proyectofinal_damc.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medico")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "numero_colegiado", nullable = false, unique = true)
    private String numeroColegiado;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email", unique = true)
    private String email;

    // RELACIÓN N-1 con Especialidad (Muchos médicos -> Una especialidad)
    @ManyToOne
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    // RELACIÓN 1-N con Cita (Un médico -> Muchas citas)
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Cita> citas = new ArrayList<>();
}