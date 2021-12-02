package maxcosta.reto2.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import static maxcosta.reto2.constant.EscuelaConstant.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maxcosta.reto2.exception.domain.EscuelaException;

@JsonIgnoreProperties({ "hibernateLazyInitializer" })
@Table(name = "escuela")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Escuela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idEscuela;

    private String nombre;
    private int cantidadAlumnos;
    private float recursoFiscal;
    private Boolean licenciada;
    private int clasificacion;
    private Date fechaRegistro;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facultad")
    private Facultad facultad;

    public boolean validarIdentificador(String idEscuelaTest) throws EscuelaException {
        if (idEscuelaTest.length() == 0)
            throw new EscuelaException(ID_VACIO);
        try {
            Integer.parseInt(idEscuelaTest);
        } catch (NumberFormatException nfe) {
            throw new EscuelaException(ID_INVALIDO);
        }
        return true;
    }

    public boolean validarNombre(String nombreTest) throws EscuelaException {
        if (nombreTest.length() == 0)
            throw new EscuelaException(NOMBRE_INVALIDO);
        return true;
    }

    public boolean validarLicenciada(Boolean licenciadaTest) throws EscuelaException {
        if (licenciadaTest == null)
            throw new EscuelaException(LICENCIADA_INVALIDO);
        return true;
    }

    public boolean validarCantidadAlumnos(String cantidadAlumnosTest) throws EscuelaException {
        if (cantidadAlumnosTest.length() > 0 && !cantidadAlumnosTest.isEmpty()) {
            try {
                Integer.parseInt(cantidadAlumnosTest);
            } catch (NumberFormatException nfe) {
                throw new EscuelaException(CANTIDAD_ALUMNOS_INVALIDO);
            }
        } else {
            throw new EscuelaException(CANTIDAD_ALUMNOS_VACIO);
        }
        return true;
    }

    public boolean validarClasificacion(String clasificacionTest) throws EscuelaException {
        if (clasificacionTest.length() > 0 && !clasificacionTest.isEmpty()) {
            try {
                Integer.parseInt(clasificacionTest);
            } catch (NumberFormatException nfe) {
                throw new EscuelaException(CLASIFICACION_INVALIDO);
            }
        } else {
            throw new EscuelaException(CLASIFICACION_VACIO);
        }
        return true;
    }

    public boolean validarRecursoFiscal(String recursoFiscalTest) throws EscuelaException {
        if (recursoFiscalTest.length() > 0 && !recursoFiscalTest.isEmpty()) {
            try {
                Float.parseFloat(recursoFiscalTest);
            } catch (NumberFormatException nfe) {
                throw new EscuelaException(RECURSO_FISCAL_INVALIDO);
            }
        } else {
            throw new EscuelaException(RECURSO_FISCAL_VACIO);
        }
        return true;
    }

    public boolean validarFechaRegistro(Date fechaRegistroTest) throws EscuelaException {
        if (fechaRegistroTest == null)
            throw new EscuelaException(FECHA_REGISTRO_INVALIDA);
        return true;
    }

    public boolean validarIdentificadorFacultad(String idFacultadTest) throws EscuelaException {
        if (idFacultadTest.length() == 0)
            throw new EscuelaException(ID_FACULTAD_VACIO);
        try {
            Integer.parseInt(idFacultadTest);
        } catch (NumberFormatException nfe) {
            throw new EscuelaException(ID_FACULTAD_INVALIDO);
        }
        return true;
    }

}
