package maxcosta.reto2.controller;

import static maxcosta.reto2.constant.EscuelaConstant.ESCUELA_ELIMINADA;
import static org.springframework.http.HttpStatus.OK;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import maxcosta.reto2.dto.EscuelaDto;
import maxcosta.reto2.exception.domain.EscuelaException;
import maxcosta.reto2.model.Escuela;
import maxcosta.reto2.model.HttpResponse;
import maxcosta.reto2.service.IEscuelaService;
import maxcosta.reto2.service.IFacultadService;
import maxcosta.reto2.utility.Utility;

@Controller
@RequestMapping("/escuelas")
public class EscuelaController {

    @Autowired
    private IEscuelaService escuelaService;

    @Autowired
    private IFacultadService facultadService;

    @Autowired
    private Utility utility;

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

    @GetMapping("/listar/fecha/{fechaRegistro}")
    public ResponseEntity<List<Escuela>> obtenerTodosLosPagosPorIntervalo(
            @PathVariable("fechaRegistro") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaRegistro) {
        List<Escuela> lista = this.escuelaService.buscarEscuelasPorFechaRegistro(fechaRegistro);
        return new ResponseEntity<>(lista, OK);
    }

    @GetMapping("/buscar/id/{id}")
    public ResponseEntity<Escuela> obtenerEscuelaPorId(@PathVariable("id") Long id) {
        Escuela escuelaEncontrada = this.escuelaService.buscarEscuelaPorId(id);
        return new ResponseEntity<>(escuelaEncontrada, OK);
    }

    @PostMapping("/guardar")
    public ResponseEntity<Escuela> guardarEscuelaPorJson(@RequestBody EscuelaDto escuelaDto)
            throws NumberFormatException, EscuelaException {

        Escuela escuelaGuardada = this.escuelaService.guardarEscuela(escuelaDto);

        return new ResponseEntity<>(escuelaGuardada, OK);
    }

    @PostMapping("/guardar/form")
    public ResponseEntity<Escuela> guardarEscuelaPorForm(
            @RequestParam(value = "idEscuela", required = false) Long idEscuela,
            @RequestParam("nombre") String nombre,
            @RequestParam("cantidadAlumnos") String cantidadAlumnos,
            @RequestParam("recursoFiscal") String recursoFiscal,
            @RequestParam(value = "licenciada", required = false) Boolean licenciada,
            @RequestParam("clasificacion") String clasificacion,
            @RequestParam(value = "fechaRegistro", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaRegistro,
            @RequestParam("idFacultad") String idFacultad) throws NumberFormatException, EscuelaException {
        EscuelaDto escuelaIntermediaria = new EscuelaDto(idEscuela, nombre, cantidadAlumnos, recursoFiscal, 
        licenciada, clasificacion, fechaRegistro, idFacultad);
        Escuela escuelaGuardada = this.escuelaService.guardarEscuela(escuelaIntermediaria);

        return new ResponseEntity<>(escuelaGuardada, OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<HttpResponse> cancelarContrato(@PathVariable("id") Long idEscuela) {
        this.escuelaService.eliminarEscuela(idEscuela);
        return response(OK, ESCUELA_ELIMINADA);
    }

    @GetMapping("/exportar/pdf/fecha/{fechaRegistro}")
    public ResponseEntity<InputStreamResource> exportarEscuelasPorFechaEnPdf(
            @PathVariable("fechaRegistro") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaRegistro) {
        String fechaRegistroConFormato = this.utility.convertirDateToStringWithFormat(fechaRegistro);
        String prefijo = this.utility.obtenerFechaActualConFormatoParaArchivos();
        List<Escuela> escuelasEncontradasPorFecha = this.escuelaService.buscarEscuelasPorFechaRegistro(fechaRegistro);
        ByteArrayInputStream bais = this.escuelaService
                .exportarListaDeEscuelasPorFechaRegistro(escuelasEncontradasPorFecha, fechaRegistroConFormato);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename = " + prefijo + "_table.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bais));

    }
    ///////////////// 7777

    @GetMapping("/exportar/barchart/alumnosPorEscuela/pdf/fecha/{fechaRegistro}")
    public ResponseEntity<InputStreamResource> buildBarChart(
            @PathVariable("fechaRegistro") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaRegistro)
            throws IOException {
        String fechaRegistroConFormato = this.utility.convertirDateToStringWithFormat(fechaRegistro);
        String prefijo = this.utility.obtenerFechaActualConFormatoParaArchivos();
        final DefaultCategoryDataset categoryDataset = buildDatasetCantidadDeAlumnosPorEscuela(fechaRegistro);
        final String title = "Total de alumnos en escuelas registradas en " + fechaRegistroConFormato;
        final String categoryAxisLabel = "Escuelas";
        final String valueAxisLabel = "Cantidad de alumnos";
        final boolean legend = true;
        final boolean tooltips = true;
        final boolean urls = true;

        final JFreeChart barChart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel,
                categoryDataset, PlotOrientation.VERTICAL, legend, tooltips, urls);
        final CategoryPlot categoryPlot = (CategoryPlot) barChart.getPlot();
        final CategoryItemRenderer categoryItemRenderer = categoryPlot.getRenderer();
        categoryItemRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        categoryItemRenderer.setDefaultItemLabelsVisible(true);

        final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER);
        categoryItemRenderer.setDefaultPositiveItemLabelPosition(position);

        final BufferedImage bufferedImage = barChart.createBufferedImage(700, 450);
        ByteArrayInputStream bais = this.escuelaService.exportarBarchartDeEscuelasPorFechaRegistro(bufferedImage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename = " + prefijo + "_barchart.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bais));
    }

    private DefaultCategoryDataset buildDatasetCantidadDeAlumnosPorEscuela(Date fechaRegistro) {
        final Comparable<String> rowKey = "Total de alumnos";
        final DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        this.escuelaService.buscarEscuelasPorFechaRegistro(fechaRegistro).forEach((escuela) -> categoryDataset
                .setValue(escuela.getCantidadAlumnos(), rowKey, escuela.getNombre()));

        return categoryDataset;
    }

    @GetMapping("/exportar/barchart/alumnosPorEscuela/image/fecha/{fechaRegistro}")
    public void buildBarChartImage(
            @PathVariable("fechaRegistro") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaRegistro,
            HttpServletResponse response)
            throws IOException {
        String fechaRegistroConFormato = this.utility.convertirDateToStringWithFormat(fechaRegistro);
        final DefaultCategoryDataset categoryDataset = buildDatasetCantidadDeAlumnosPorEscuela(fechaRegistro);
        final String title = "Total de alumnos en escuelas registradas en " + fechaRegistroConFormato;
        final String categoryAxisLabel = "Escuelas";
        final String valueAxisLabel = "Cantidad de alumnos";
        final boolean legend = true;
        final boolean tooltips = true;
        final boolean urls = true;

        final JFreeChart barChart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel,
                categoryDataset, PlotOrientation.VERTICAL, legend, tooltips, urls);
        final CategoryPlot categoryPlot = (CategoryPlot) barChart.getPlot();
        final CategoryItemRenderer categoryItemRenderer = categoryPlot.getRenderer();
        categoryItemRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        categoryItemRenderer.setDefaultItemLabelsVisible(true);

        final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER);
        categoryItemRenderer.setDefaultPositiveItemLabelPosition(position);

        writeChartAsPNGImage(barChart, 700, 450, response);
    }

    @GetMapping("/exportar/piechart/alumnosPorEscuela/pdf/fecha/{fechaRegistro}")
    public ResponseEntity<InputStreamResource> buildPieChart(
            @PathVariable("fechaRegistro") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaRegistro)
            throws IOException {
        String fechaRegistroConFormato = this.utility.convertirDateToStringWithFormat(fechaRegistro);
        String prefijo = this.utility.obtenerFechaActualConFormatoParaArchivos();
        final PieDataset pieDataset = buildTopIDEIndexPieDataset(fechaRegistro);
        final String title = "Top Cantidad de Alumnos de escuelas registradas en " + fechaRegistroConFormato;
        final boolean legend = true;
        final boolean tooltips = true;

        final PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0} = {2}");

        final JFreeChart pieChart3D = ChartFactory.createPieChart3D(title, pieDataset, legend, tooltips,
                Locale.getDefault());
        final PiePlot3D piePlot3D = (PiePlot3D) pieChart3D.getPlot();
        piePlot3D.setLabelGenerator(labelGenerator);

        final BufferedImage bufferedImage = pieChart3D.createBufferedImage(700, 450);
        ByteArrayInputStream bais = this.escuelaService.exportarPiechartDeEscuelasPorFechaRegistro(bufferedImage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename = " + prefijo + "_piechart.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bais));
    }

    @GetMapping("/exportar/piechart/alumnosPorEscuela/image/fecha/{fechaRegistro}")
    public void buildPieChartImage(
            @PathVariable("fechaRegistro") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaRegistro,
            HttpServletResponse response)
            throws IOException {
        String fechaRegistroConFormato = this.utility.convertirDateToStringWithFormat(fechaRegistro);
        final PieDataset pieDataset = buildTopIDEIndexPieDataset(fechaRegistro);
        final String title = "Top Cantidad de Alumnos de escuelas registradas en " + fechaRegistroConFormato;
        final boolean legend = true;
        final boolean tooltips = true;

        final PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0} = {2}");

        final JFreeChart pieChart3D = ChartFactory.createPieChart3D(title, pieDataset, legend, tooltips,
                Locale.getDefault());
        final PiePlot3D piePlot3D = (PiePlot3D) pieChart3D.getPlot();
        piePlot3D.setLabelGenerator(labelGenerator);

        writeChartAsPNGImage(pieChart3D, 750, 450, response);
    }

    private PieDataset buildTopIDEIndexPieDataset(Date fechaRegistro) {
        final DefaultPieDataset pieDataset = new DefaultPieDataset();
        this.escuelaService.getTopPopulationEscuelasPorFecha(fechaRegistro)
                .forEach((escuela) -> pieDataset.setValue(escuela.getNombreEscuela(),
                        escuela.getPorcentajeTotalAlumnos()));

        return pieDataset;
    }

    private void writeChartAsPNGImage(final JFreeChart chart, final int width, final int height,
            HttpServletResponse response) throws IOException {
        final BufferedImage bufferedImage = chart.createBufferedImage(width, height);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        ChartUtils.writeBufferedImageAsPNG(response.getOutputStream(), bufferedImage);
    }
    //////////////// 7777

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
                httpStatus);
    }

}
