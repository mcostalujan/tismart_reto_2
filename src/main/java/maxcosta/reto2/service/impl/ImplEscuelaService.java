package maxcosta.reto2.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxcosta.reto2.dao.IEscuelaDao;
import maxcosta.reto2.dao.IFacultadDao;
import maxcosta.reto2.dto.EscuelaDto;
import maxcosta.reto2.exception.domain.EscuelaException;
import maxcosta.reto2.model.Escuela;
import maxcosta.reto2.model.EscuelaPopulation;
import maxcosta.reto2.model.Facultad;
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
    public Escuela guardarEscuela(EscuelaDto escuelaDto) throws NumberFormatException, EscuelaException, ParseException {
        if (validarEscuela(escuelaDto)) {
            Facultad facultadEncontrada = this.facultadDao.findByIdFacultad(Long.valueOf(escuelaDto.getIdFacultad()));
            Escuela escuelaNueva = new Escuela();
            if (escuelaDto.getIdEscuela() != null)
                escuelaNueva = this.escuelaDao.findByIdEscuela(escuelaDto.getIdEscuela());
            escuelaNueva.setCantidadAlumnos(Integer.parseInt(escuelaDto.getCantidadAlumnos()));
            escuelaNueva.setClasificacion(Integer.parseInt(escuelaDto.getClasificacion()));
            escuelaNueva.setFacultad(facultadEncontrada);
            escuelaNueva.setFechaRegistro(escuelaDto.getFechaRegistro());
            escuelaNueva.setLicenciada(escuelaDto.getLicenciada());
            escuelaNueva.setNombre(escuelaDto.getNombre());
            escuelaNueva.setRecursoFiscal(Float.parseFloat(escuelaDto.getRecursoFiscal()));
            return this.escuelaDao.save(escuelaNueva);
        }
        return null;
    }

    private boolean validarEscuela(EscuelaDto escuelaDto) throws EscuelaException {
        Escuela escuelaTemporal = new Escuela();
        return escuelaTemporal.validarCantidadAlumnos(escuelaDto.getCantidadAlumnos())
                && escuelaTemporal.validarClasificacion(escuelaDto.getClasificacion())
                && escuelaTemporal.validarFechaRegistro(escuelaDto.getFechaRegistro())
                && escuelaTemporal.validarLicenciada(escuelaDto.getLicenciada())
                && escuelaTemporal.validarNombre(escuelaDto.getNombre())
                && escuelaTemporal.validarRecursoFiscal(escuelaDto.getRecursoFiscal())
                && escuelaTemporal.validarIdentificadorFacultad(String.valueOf(escuelaDto.getIdFacultad()));
    }

    @Override
    public void eliminarEscuela(Long idEscuela) {
        this.escuelaDao.deleteById(idEscuela);
    }

    @Override
    public List<Escuela> buscarEscuelasPorFacultad(String facultad) {
        return this.escuelaDao.findAllByFacultadNombreContainingIgnoreCase(facultad);
    }

    @Override
    public List<Escuela> buscarEscuelasPorFechaRegistro(Date fechaRegistro) {
        return this.escuelaDao.findAllByFechaRegistro(fechaRegistro);
    }

    @Override
    public ByteArrayInputStream exportarListaDeEscuelasPorFechaRegistro(List<Escuela> escuelasEncontradasPorFecha,
            String fecha) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // add text to pdf file
            com.itextpdf.text.Font font = com.itextpdf.text.FontFactory.getFont(FontFactory.COURIER, 14,
                    BaseColor.BLACK);
            Paragraph paragraph = new Paragraph("Lista de escuelas registradas en " + fecha, font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(8);

            // make column titles
            Stream.of("ID", "Nombre", "Total Alumnos", "Recurso Fiscal", "¿Licenciada?", "Clasificación",
                    "Fecha Registro", "Facultad").forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        header.setBorderWidth(1);
                        header.setPhrase(new Phrase(headerTitle, headFont));
                        table.addCell(header);
                    });

            for (Escuela escuelaTemp : escuelasEncontradasPorFecha) {
                PdfPCell idCell = new PdfPCell(new Phrase(escuelaTemp.getIdEscuela().toString()));
                idCell.setPaddingLeft(1);
                idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);

                PdfPCell nombreCell = new PdfPCell(new Phrase(escuelaTemp.getNombre()));
                nombreCell.setPaddingLeft(1);
                nombreCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                nombreCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(nombreCell);

                PdfPCell cantidadAlumnosCell = new PdfPCell(
                        new Phrase(String.valueOf(escuelaTemp.getCantidadAlumnos())));
                cantidadAlumnosCell.setPaddingLeft(1);
                cantidadAlumnosCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cantidadAlumnosCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cantidadAlumnosCell);

                PdfPCell recursoFiscalCell = new PdfPCell(new Phrase(String.valueOf(escuelaTemp.getRecursoFiscal())));
                recursoFiscalCell.setPaddingLeft(1);
                recursoFiscalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                recursoFiscalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(recursoFiscalCell);

                PdfPCell licenciadaCell = new PdfPCell(new Phrase(escuelaTemp.getLicenciada().toString()));
                licenciadaCell.setPaddingLeft(1);
                licenciadaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                licenciadaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(licenciadaCell);

                PdfPCell clasificacionCell = new PdfPCell(new Phrase(String.valueOf(escuelaTemp.getClasificacion())));
                clasificacionCell.setPaddingLeft(1);
                clasificacionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                clasificacionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(clasificacionCell);

                PdfPCell fechaRegistroCell = new PdfPCell(
                        new Phrase(this.utility.convertirDateToStringWithFormat(escuelaTemp.getFechaRegistro())));
                fechaRegistroCell.setPaddingLeft(1);
                fechaRegistroCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                fechaRegistroCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(fechaRegistroCell);

                PdfPCell facultadCell = new PdfPCell(new Phrase(escuelaTemp.getFacultad().getNombre()));
                facultadCell.setPaddingLeft(1);
                facultadCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                facultadCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(facultadCell);
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream exportarBarchartDeEscuelasPorFechaRegistro(BufferedImage bufferedImage)
            throws IOException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // add text to pdf file
            com.itextpdf.text.Font font = com.itextpdf.text.FontFactory.getFont(FontFactory.COURIER, 14,
                    BaseColor.BLACK);
            Paragraph paragraph = new Paragraph("Bar Chart", font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            Image img = Image.getInstance(baos.toByteArray());
            img.setAlignment(Element.ALIGN_CENTER);
            document.add(img);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public List<EscuelaPopulation> getTopPopulationEscuelasPorFecha(Date fechaRegistro) {
        List<Escuela> listEncontrada = this.escuelaDao.findAllByFechaRegistro(fechaRegistro);
        List<EscuelaPopulation> listaAEnviar = new ArrayList<>();
        double totalAlumnos = 0;
        for (Escuela escuelaTemp : listEncontrada)
            totalAlumnos += escuelaTemp.getCantidadAlumnos();

        if (totalAlumnos > 0) {
            for (Escuela escuelaTemp : listEncontrada) {
                EscuelaPopulation muestraNueva = new EscuelaPopulation();
                muestraNueva.setNombreEscuela(escuelaTemp.getNombre());
                muestraNueva.setPorcentajeTotalAlumnos(escuelaTemp.getCantidadAlumnos() / totalAlumnos);
                listaAEnviar.add(muestraNueva);
            }
        }

        return listaAEnviar;
    }

    @Override
    public ByteArrayInputStream exportarPiechartDeEscuelasPorFechaRegistro(BufferedImage bufferedImage)
            throws IOException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // add text to pdf file
            com.itextpdf.text.Font font = com.itextpdf.text.FontFactory.getFont(FontFactory.COURIER, 14,
                    BaseColor.BLACK);
            Paragraph paragraph = new Paragraph("Pie Chart", font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            Image img = Image.getInstance(baos.toByteArray());
            img.setAlignment(Element.ALIGN_CENTER);
            document.add(img);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

}
