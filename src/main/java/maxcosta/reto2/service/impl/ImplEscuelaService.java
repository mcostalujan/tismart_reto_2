package maxcosta.reto2.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxcosta.reto2.dao.IEscuelaDao;
import maxcosta.reto2.dao.IFacultadDao;
import maxcosta.reto2.model.Escuela;
import maxcosta.reto2.service.IEscuelaService;
import maxcosta.reto2.utility.Utility;

@Service
public class ImplEscuelaService implements IEscuelaService {

    @Autowired
    private IEscuelaDao escuelaDao;

    @Autowired
    private IFacultadDao facultadDao;

    @Autowired
    private Utility utility;

    @Override
    public Escuela buscarEscuelaPorId(Long idEscuela) {
        return this.escuelaDao.findByIdEscuela(idEscuela);
    }

    @Override
    public List<Escuela> buscarTodasLasEscuelas() {
        return this.escuelaDao.findAll();
    }

    @Override
    public Escuela guardarEscuela(Escuela escuela) {
        return this.escuelaDao.save(escuela);
    }

    @Override
    public void eliminarEscuela(Long idEscuela) {
        this.escuelaDao.deleteById(idEscuela);
    }

    @Override
    public List<Escuela> buscarEscuelasPorFacultad(String facultad) {
        return this.escuelaDao.findAllByFacultadNombreContainingIgnoreCase(facultad);
    }
}
