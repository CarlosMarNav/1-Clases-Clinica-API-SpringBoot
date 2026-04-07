package progresa.proyectofinal_damc.dao;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriaClinicaDTO {

    private String numeroHistoria;
    private String grupoSanguineo;
    private String alergias;
    private String enfermedadesCronicas;
    private String observaciones;
    private Long pacienteId; // ID del paciente al que pertenece
}