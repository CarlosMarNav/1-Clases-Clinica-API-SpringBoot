package progresa.proyectofinal_damc.dao;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicoDTO {

    private String nombre;
    private String apellidos;
    private String numeroColegiado;
    private String telefono;
    private String email;
    private Long especialidadId; // Solo enviamos el ID de la especialidad, no toda la entidad
}