package maxcosta.reto2.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import maxcosta.reto2.dto.EscuelaDto;
import maxcosta.reto2.model.Escuela;
import maxcosta.reto2.model.Facultad;
import maxcosta.reto2.service.IEscuelaService;
import maxcosta.reto2.service.IFacultadService;

@Controller
@RequestMapping("/escuelas")
public class EscuelaController {

    @Autowired
    private IEscuelaService escuelaService;
    
    @Autowired
    private IFacultadService facultadService;
    
    @GetMapping("/listar")
    public ResponseEntity<List<Escuela>> obtenerTodasLasEscuelas() {
        List<Escuela> lista = this.escuelaService.buscarTodasLasEscuelas();
        return new ResponseEntity<>(lista, OK);
    }

    @GetMapping("/listar/facultad/{escuela}")
    public ResponseEntity<List<Escuela>> obtenerTodosLosPagosPorIntervalo(@PathVariable("escuela") String escuela) {
        List<Escuela> lista = this.escuelaService.buscarEscuelasPorFacultad(escuela);
        return new ResponseEntity<>(lista, OK);
    }

    @PostMapping("/guardar")
    public ResponseEntity<Escuela> guardarEscuelaPorJson(@RequestBody EscuelaDto escuelaDto) {
        Escuela escuelaNueva = new Escuela();
        Escuela escuelaGuardada = null;
        Facultad facultadEncontrada = this.facultadService.buscarFacultadPorId(escuelaDto.getIdFacultad());
        if (facultadEncontrada != null) {
            if(escuelaDto.getIdEscuela()!= null) escuelaNueva.setIdEscuela(escuelaDto.getIdEscuela());
            escuelaNueva.setCantidadAlumnos(escuelaDto.getCantidadAlumnos());
            escuelaNueva.setClasificacion(escuelaDto.getClasificacion());
            escuelaNueva.setFechaRegistro(escuelaDto.getFechaRegistro());
            escuelaNueva.setLicenciada(escuelaDto.getLicenciada());
            escuelaNueva.setNombre(escuelaDto.getNombre());
            escuelaNueva.setRecursoFiscal(escuelaDto.getRecursoFiscal());
            escuelaNueva.setFacultad(facultadEncontrada);
            escuelaGuardada = this.escuelaService.guardarEscuela(escuelaNueva);
        }
        
        return new ResponseEntity<>(escuelaGuardada, OK);
    }




}
