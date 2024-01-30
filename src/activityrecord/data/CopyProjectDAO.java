package activityrecord.data;

import activityrecord.data.ProyectActivityDAO;
import java.io.Serializable;
import java.util.ArrayList;

public class CopyProjectDAO implements Serializable {
  public ArrayList<ProyectActivityDAO> actividades = new ArrayList<>();
  
  public ArrayList<String> proyectosDestino = new ArrayList<>();
  
  public String proyecto;
}
