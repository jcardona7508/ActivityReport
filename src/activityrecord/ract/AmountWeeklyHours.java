package activityrecord.ract;

import java.util.List;

public class AmountWeeklyHours {
  protected List fecha;
  
  protected List cantidad;
  
  public void setFecha(List fecha) {
    this.fecha = fecha;
  }
  
  public List getFecha() {
    return this.fecha;
  }
  
  public void setCantidad(List cantidad) {
    this.cantidad = cantidad;
  }
  
  public List getCantidad() {
    return this.cantidad;
  }
}
