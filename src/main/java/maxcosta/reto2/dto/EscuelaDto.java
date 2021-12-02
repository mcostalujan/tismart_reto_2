package maxcosta.reto2.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EscuelaDto {
    private Long idEscuela;
    private String nombre;
    private String cantidadAlumnos;
    private String recursoFiscal;
    private Boolean licenciada;
    private String clasificacion;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fechaRegistro;
    private String idFacultad;
}
