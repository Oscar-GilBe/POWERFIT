package VO;
import java.time.LocalDate;    
import java.time.LocalTime;    
import java.time.DayOfWeek;    


/*
 * Clase ClaseVO que representa la entidad clase de la BD y guarda la actividad a la que pertenece, la fecha y dia de la semana
 * en la que se imparte, las horas a la que comienza y finaliza, y las plazas disponibles que quedan
 */
public class ClaseVO {
    String _actividad;
    LocalDate _fecha;
    DayOfWeek _dia;
    LocalTime _inicio, _fin;
    Integer _plazas;

    public ClaseVO(String actividad, LocalDate fecha, DayOfWeek dia, LocalTime inicio, LocalTime fin, Integer plazas) {
        this._actividad = actividad;
        this._fecha = fecha;
        this._dia = dia;
        this._inicio = inicio;
        this._fin = fin;
        this._plazas = plazas;
    }

    public String getActividad(){
        return _actividad;
    }

    public void setActividad(String actividad) { 
        this._actividad = actividad;
    }

    public LocalDate getFecha() {
        return this._fecha;
    }

    public void setFecha(LocalDate fecha) {
        this._fecha = fecha;
    }

    public DayOfWeek getDia() {
        return this._dia;
    }
   
    public void setDia(DayOfWeek dia) {
        this._dia = dia;
    }
    
    public LocalTime getInicio() {
        return this._inicio;
    }

    public void setInicio(LocalTime inicio) {
        this._inicio = inicio;
    }

    public LocalTime getFin() {
        return this._fin;
    }

    public void setFin(LocalTime fin) {
        this._fin = fin;
    } 
    
    public Integer getPlazas() {
        return this._plazas;
    }

    public void setPlazas(Integer plazas) {
        this._plazas = plazas;
    }

    public static String DayOfWeekToString(DayOfWeek dia) {
        switch (dia) {
            case MONDAY:
                return "LUNES";
            case TUESDAY:
                return "MARTES";
            case WEDNESDAY:
                return "MIERCOLES";
            case THURSDAY:
                return "JUEVES";
            case FRIDAY:
                return "VIERNES";
            case SATURDAY:
                return "SABADO";
            case SUNDAY:
                return "DOMINGO";
            default:
                return "";
        }
    }

    public static DayOfWeek StringToDayOfWeek(String dia) {
        switch (dia.toUpperCase()) {
            case "LUNES":
                return DayOfWeek.MONDAY;
            case "MARTES":
                return DayOfWeek.TUESDAY;
            case "MIERCOLES":
                return DayOfWeek.WEDNESDAY;
            case "JUEVES":
                return DayOfWeek.THURSDAY;
            case "VIERNES":
                return DayOfWeek.FRIDAY;
            case "SABADO":
                return DayOfWeek.SATURDAY;
            default:    // D
                return DayOfWeek.SUNDAY;
        }
    }
}