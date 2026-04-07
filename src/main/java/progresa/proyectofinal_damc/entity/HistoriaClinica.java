package progresa.proyectofinal_damc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "historia_clinica")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_historia", nullable = false, unique = true)
    private String numeroHistoria;

    @Column(name = "grupo_sanguineo")
    private String grupoSanguineo;

    @Column(name = "alergias")
    private String alergias;

    @Column(name = "enfermedades_cronicas")
    private String enfermedadesCronicas;

    @Column(name = "observaciones")
    private String observaciones;

    // RELACIÓN 1-1 con Paciente (Una historia -> Un paciente)
    @OneToOne
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    @JsonBackReference // Evita recursión infinita en JSON
    private Paciente paciente;

    // RELACIÓN 1-N con Cita (Una historia registra muchas citas)
    @OneToMany(mappedBy = "historiaClinica")
    private List<Cita> citas = new ArrayList<>();
}