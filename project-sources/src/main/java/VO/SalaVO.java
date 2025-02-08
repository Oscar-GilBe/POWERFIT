package VO;
/*
 * Clase SalaVO que representa la entidad sala de la BD y guarda el numero de la sala
 */
public class SalaVO {
    Integer _numero;

    public SalaVO(Integer numero) {
        this._numero = numero;
    }
    
    public Integer getNumero(){
        return _numero;
    }

    public void setNumero(Integer numero) { 
        this._numero = numero;
    }
}