package activityrecord.ract;

import java.util.StringTokenizer;
import siscorp.system.DateSC;

public class Afix {
  protected String usuario;
  
  protected Integer nume;
  
  protected String actividad;
  
  protected String objeto;
  
  protected String objetoAux;
  
  protected int generateNumber(int i) {
    int resultado = 0;
    DateSC fecha = new DateSC();
    fecha.setFormat("yyyy/MM/dd");
    if (fecha != null) {
      StringTokenizer fechacompleta = new StringTokenizer(fecha.toString(), "/");
      int ano = (new Integer(fechacompleta.nextToken())).intValue();
      int mes = (new Integer(fechacompleta.nextToken())).intValue();
      int dia = (new Integer(fechacompleta.nextToken())).intValue();
      resultado = ((ano - 1900) * 10000 + mes * 100 + dia) * 1000 + i;
    } 
    return resultado;
  }
  
  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }
  
  public String getUsuario() {
    return this.usuario;
  }
  
  public void setActividad(String actividad) {
    this.actividad = actividad;
  }
  
  public String getActividad() {
    return this.actividad;
  }
  
  public void setObjeto(String obj) {
    this.objeto = obj;
  }
  
  public String getObjeto() {
    return this.objeto;
  }
  
  public void setObjetoAux(String obj) {
    this.objetoAux = obj;
  }
  
  public String getObjetoAux() {
    return this.objetoAux;
  }
  
  public Integer getNume() {
    return this.nume;
  }
  
  public void setNume(Integer integer) {
    this.nume = new Integer(generateNumber(integer.intValue()));
  }
}
