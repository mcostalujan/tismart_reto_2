package maxcosta.reto2.utility;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

@Service
public final class Utility {

    private Utility() {
    }

    public static final String TIME_ZONE = "America/Lima";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public Date convertStringToDateWithTimeZone(String fecha) throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat(DATE_FORMAT);
        isoFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return isoFormat.parse(fecha);
    }

    public String convertirDateToStringWithFormat(Date fecha) {

        Format formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(fecha);
    }

    public String obtenerFechaActualConFormatoParaArchivos() {
        Format formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        return formatter.format(new Date());
    }

    public Date arreglarZonaHorariaFecha(Date fecha) throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat(DATE_FORMAT);
        isoFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String strDate = dateFormat.format(fecha);
        return isoFormat.parse(strDate);
    }

    public Date obtenerFechaActual() throws ParseException {
        return this.arreglarZonaHorariaFecha(java.sql.Timestamp.valueOf(LocalDateTime.now()));
    }

    public static String randomAlphaNumeric(int count){
        String carateres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456";
        StringBuilder builder = new StringBuilder();
        while(count-- != 0){
            int character = (int) (Math.random() * carateres.length());
            builder.append(carateres.charAt(character));
        }
        return builder.toString();
    }
}
