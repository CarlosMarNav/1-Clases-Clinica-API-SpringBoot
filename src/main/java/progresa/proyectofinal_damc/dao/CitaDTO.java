package progresa.proyectofinal_damc.dao;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {

    private String fecha;
    private String hora;
    private String motivo;
    private Long pacienteId;
    private Long medicoId;
}