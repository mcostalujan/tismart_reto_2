package maxcosta.reto2.controller;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import maxcosta.reto2.model.Facultad;
import maxcosta.reto2.service.IFacultadService;

@Controller
@RequestMapping("/facultades")
public class FacultadController {
    
    @Autowired
    private IFacultadService facultadService;

    @GetMapping("/listar")
    public ResponseEntity<List<Facultad>> obtenerTodasLasFacultades() {
        List<Facultad> lista = this.facultadService.buscarTodasLasFacultades();
        return new ResponseEntity<>(lista, OK);
    }

    @PostMapping("/guardar")
    public ResponseEntity<Facultad> guardarFacultadPorJson(@RequestBody Facultad facultad) {
        Facultad facultadGuardada = this.facultadService.guardarFacultad(facultad);
        return new ResponseEntity<>(facultadGuardada, OK);
    }

}
