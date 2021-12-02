package maxcosta.reto2.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import maxcosta.reto2.model.Facultad;

public interface IFacultadDao extends JpaRepository<Facultad, Long> {
    

    public Facultad findByIdFacultad(Long idFacultad);

}
