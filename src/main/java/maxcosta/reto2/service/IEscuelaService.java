package maxcosta.reto2.service;

import java.util.List;

import maxcosta.reto2.model.Escuela;

public interface IEscuelaService {

    public Escuela buscarEscuelaPorId(Long idEscuela);
    public List<Escuela> buscarEscuelasPorFacultad(String facultad);
    public List<Escuela> buscarTodasLasEscuelas();
    public Escuela guardarEscuela(Escuela escuela);
    public void eliminarEscuela(Long idEscuela);
    
}
