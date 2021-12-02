package maxcosta.reto2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxcosta.reto2.dao.IFacultadDao;
import maxcosta.reto2.model.Facultad;
import maxcosta.reto2.service.IFacultadService;

@Service
public class ImplFacultadService implements IFacultadService{

    @Autowired
    private IFacultadDao facultadDao;

    @Override
    public Facultad buscarFacultadPorId(Long idFacultad) {
        return this.facultadDao.findByIdFacultad(idFacultad);
    }

    @Override
    public List<Facultad> buscarTodasLasFacultades() {
        return this.facultadDao.findAll();
    }

    @Override
    public Facultad guardarFacultad(Facultad facultad) {
        return this.facultadDao.save(facultad);
    }
    
}
