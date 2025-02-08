package VO;

import java.time.LocalDate;
import java.time.LocalTime;   

/*
 * Clase ReservaVO que representa la entidad reserva de la BD, que guarda el cliente que realiza la reserva, 
 * y la clase reservada, dada por la actividad a la que pertenece, la fecha en la que se imparte y la hora a
 * la que comienza
 */
public class ReservaVO {
    String _cliente;
    String _actividad;
    LocalDate _fecha;
    LocalTime _hora;

    public ReservaVO(String cliente, String actividad, LocalDate fecha, LocalTime hora) {
        this._cliente = cliente;
        this._actividad = actividad;
        this._fecha = fecha;
        this._hora = hora;
    }
    
    public String getCliente(){
        return _cliente;
    }

    public void setCliente(String cliente) { 
        this._cliente = cliente;
    }

    public String getActividad(){
        return _actividad;
    }

    public void setActividad(String actividad) { 
        this._actividad = actividad;
    }

    public LocalDate getFecha(){
        return _fecha;
    }

    public void setFecha(LocalDate fecha) { 
        this._fecha = fecha;
    }

    public LocalTime getHora(){
        return _hora;
    }

    public void setHora(LocalTime hora) { 
        this._hora = hora;
    }
}