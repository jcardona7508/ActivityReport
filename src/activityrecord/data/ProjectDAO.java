package activityrecord.data;

import activityrecord.data.ProyectActivityDAO;
import java.io.Serializable;
import java.util.ArrayList;

public class ProjectDAO implements Serializable {
  public String proyecto;
  
  public String nombreProyecto;
  
  public ArrayList<ProyectActivityDAO> actividades = new ArrayList<>();
  
  public void addActivity(ProyectActivityDAO activity) {
    this.actividades.add(activity);
  }
  
  public ProyectActivityDAO getActivity(String activityCode) {
    for (ProyectActivityDAO acti : this.actividades) {
      if (acti.actividad.equals(activityCode))
        return acti; 
    } 
    return null;
  }
  
  public void updateActivitiesValues(activityrecord.data.ProjectDAO proyect) {
    for (ProyectActivityDAO acti : this.actividades) {
      ProyectActivityDAO newProyectActivity = proyect.getActivity(acti.actividad);
      if (newProyectActivity != null) {
        acti.costo = newProyectActivity.costo;
        acti.qtty = newProyectActivity.qtty;
        acti.contratado = newProyectActivity.contratado;
      } 
    } 
  }
}
