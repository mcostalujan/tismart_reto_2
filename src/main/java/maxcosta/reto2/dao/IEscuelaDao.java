package maxcosta.reto2.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import maxcosta.reto2.model.Escuela;

public interface IEscuelaDao extends JpaRepository<Escuela, Long>{

    public Escuela findByIdEscuela(Long idEscuela);

    public List<Escuela> findAllByFacultadNombreContainingIgnoreCase(String facultad);

    public List<Escuela> findAllByFechaRegistro(Date fechaRegistro);
    
}
