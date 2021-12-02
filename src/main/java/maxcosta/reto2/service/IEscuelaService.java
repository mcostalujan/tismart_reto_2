package maxcosta.reto2.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import maxcosta.reto2.model.Escuela;
import maxcosta.reto2.model.EscuelaPopulation;

public interface IEscuelaService {

    public Escuela buscarEscuelaPorId(Long idEscuela);

    public List<Escuela> buscarEscuelasPorFacultad(String facultad);

    public List<Escuela> buscarTodasLasEscuelas();

    public Escuela guardarEscuela(Escuela escuela);

    public void eliminarEscuela(Long idEscuela);

    public List<Escuela> buscarEscuelasPorFechaRegistro(Date fechaRegistro);

    public List<EscuelaPopulation> getTopPopulationEscuelasPorFecha(Date fechaRegistro);

    public ByteArrayInputStream exportarListaDeEscuelasPorFechaRegistro(List<Escuela> escuelasEncontradasPorFecha);

    public ByteArrayInputStream exportarBarchartDeEscuelasPorFechaRegistro(BufferedImage bufferedImage) throws IOException;

    public ByteArrayInputStream exportarPiechartDeEscuelasPorFechaRegistro(BufferedImage bufferedImage) throws IOException;

}
