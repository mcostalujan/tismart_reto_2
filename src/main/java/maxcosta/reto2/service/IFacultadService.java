package maxcosta.reto2.service;

import java.util.List;

import maxcosta.reto2.model.Facultad;

public interface IFacultadService {

    public Facultad buscarFacultadPorId(Long idFacultad);
    public List<Facultad> buscarTodasLasFacultades();
    public Facultad guardarFacultad(Facultad facultad);
    
}
