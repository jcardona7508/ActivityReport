package activityrecord.engine.action;

import activityrecord.data.RactDAO;
import activityrecord.engine.event.UpdateRactEvent;
import activityrecord.ract.Activity;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.Afix;
import com.gausssoft.GausssoftException;
import common.engine.GenericComponentManager;
import common.engine.action.DefaultHandler;
import common.sql.SQLManager;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscorp.dbmanagement.DatabaseSC;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.system.DateSC;

public class UpdateRactEventHandler extends DefaultHandler {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    UpdateRactEvent ractEvent = (UpdateRactEvent)event;
    EventResponse response = new EventResponse();
    switch (ractEvent.getEventCode()) {
      case 0:
        doUpdateRact(event, response);
        return response;
    } 
    response.setResultCode(100);
    response.setAttribute("message", "Evento Desconocido");
    return response;
  }
  
  protected void doUpdateRact(ApplicationEvent event, EventResponse response) {
    String hora = null, cantidad = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.doUpdateRact");
    Afix afix = new Afix();
    DateSC currentTime = new DateSC();
    currentTime.setFormat("yyyy/MM/dd hh:mm:ss");
    Session session = event.getRequest().getSession();
    String doubleValues = (session.getAttribute("RACT_PARAMETERS") != null && ((Map)session.getAttribute("RACT_PARAMETERS")).get("DOBL") != null) ? ((Map)session.getAttribute("RACT_PARAMETERS")).get("DOBL").toString() : "F";
    logger.log(Level.INFO, "**** > doUpdateRact: " + RactDAO.getCurrentDateAndTime());
    long startTime = System.nanoTime();
    try {
      ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
      GenericComponentManager manager = 
        (GenericComponentManager)event.getApplicationContext()
        .getAtribute(
          "siscorp.framework.application.ComponentManager");
      SQLManager sqlManager = 
        (SQLManager)manager.getComponent(
          "activityrecord.sqleditor", 
          "activityrecord");
      if (manager == null)
        throw new Exception("Component Manager = NULL "); 
      DatabaseSC db = manager.getDatabase("activityrecord");
      String[] fecha = event.getRequest().getParameterValues("currentdate");
      String[] activ = event.getRequest().getParameterValues("idactividad");
      String[] obj = event.getRequest().getParameterValues("idobjeto");
      String[] objAux = event.getRequest().getParameterValues("idobjetoaux");
      String[] fixed = event.getRequest().getParameterValues("fixed");
      String[] cust1 = event.getRequest().getParameterValues("cust1");
      String[] cust2 = event.getRequest().getParameterValues("cust2");
      String[] cust3 = event.getRequest().getParameterValues("cust3");
      String[] cust4 = event.getRequest().getParameterValues("cust4");
      String[] cust5 = event.getRequest().getParameterValues("cust5");
      if (activ == null || obj == null || objAux == null || fixed == null) {
        response.setResultCode(0);
      } else {
        for (int i = 0; i < fecha.length; i++) {
          logger.log(Level.FINEST, " Fecha " + fecha[i]);
          String[] horas = event.getRequest().getParameterValues("horas" + fecha[i]);
          String[] cantidades = event.getRequest().getParameterValues("cano" + fecha[i]);
          String[] coment = event.getRequest().getParameterValues("comentario" + fecha[i]);
          ArrayList<Activity> registroLst = new ArrayList<>();
          boolean delete = false;
          for (int j = 0; j < activ.length; j++) {
            hora = null;
            hora = (horas[j].trim().length() != 0) ? horas[j] : "0";
            logger.log(Level.FINEST, "  valor de i  " + i);
            logger.log(Level.FINEST, "  valor de j  " + j);
            logger.log(Level.FINEST, "  hora/porcentaje " + hora);
            if (doubleValues.equalsIgnoreCase("T")) {
              cantidad = (cantidades[j].trim().length() != 0) ? cantidades[j] : "0";
            } else {
              cantidad = hora;
            } 
            logger.log(Level.FINEST, "  cantidad " + cantidad);
            Activity registro = new Activity();
            DateSC date = new DateSC(fecha[i], "yyyy/MM/dd");
            logger.log(Level.FINEST, "  fecha convertida  " + date.toString());
            registro.setAcur(resource.getResourceCode());
            logger.log(Level.FINEST, "  resourceCode " + cantidad);
            registro.setFech(date.toString());
            registro.generateNumber(j);
            logger.log(Level.FINEST, "  nume " + registro.getNume());
            registro.setAcur_acur(resource.getCode());
            registro.setAcur_usua(resource.getUserName());
            registro.setAcua(activ[j]);
            logger.log(Level.FINEST, "  actividad " + activ[j]);
            registro.setCana(hora);
            registro.setAcuo(obj[j]);
            logger.log(Level.FINEST, "  objeto  " + obj[j]);
            registro.setCano(cantidad);
            registro.setAcu4(objAux[j]);
            logger.log(Level.FINEST, "  objetoaux " + objAux[j]);
            registro.setCome(coment[j]);
            logger.log(Level.FINEST, "  coment " + coment[j]);
            registro.setStam(currentTime.toString());
            if (cust1 != null)
              registro.setCustomFieldValue(0, cust1[j]); 
            if (cust2 != null)
              registro.setCustomFieldValue(1, cust2[j]); 
            if (cust3 != null)
              registro.setCustomFieldValue(2, cust3[j]); 
            if (cust4 != null)
              registro.setCustomFieldValue(3, cust4[j]); 
            if (cust5 != null)
              registro.setCustomFieldValue(4, cust5[j]); 
            Activity tmp = RactDAO.getActivityBy(registro, date, resource.getResourceCode(), sqlManager);
            if (tmp == null) {
              if (registro.getCana().floatValue() > 0.0F || registro.getCano().floatValue() > 0.0F) {
                String newNume = RactDAO.getNextNume(date, resource.getResourceCode(), sqlManager);
                if (newNume != null)
                  registro.setNume(newNume); 
                RactDAO.saveRact(registro, sqlManager);
              } 
            } else {
              if (tmp.getCome() == null)
                tmp.setCome(""); 
              if (registro.getCana().floatValue() != tmp.getCana().floatValue() || registro.getCano().floatValue() != tmp.getCano().floatValue() || (registro.getCome() != null && !registro.getCome().equals(tmp.getCome())) || !tmp.getCustomFieldValues().equals(registro.getCustomFieldValues())) {
                RactDAO.deleteRact(registro, date, resource.getResourceCode(), sqlManager);
                logger.log(Level.INFO, ">Borrando registro: " + registro.getActividad() + " " + registro.getObjeto() + " " + registro.getDetail() + " " + tmp.getNume());
                if (registro.getCana().floatValue() > 0.0F || registro.getCano().floatValue() > 0.0F) {
                  registro.setNume(tmp.getNume());
                  RactDAO.saveRact(registro, sqlManager);
                  logger.log(Level.INFO, "<Registro Agregado: " + registro.getCana().floatValue() + " " + registro.getCano().floatValue() + registro.getCome() + " " + registro.getCustomFieldValues());
                } 
              } 
            } 
          } 
        } 
        RactDAO.deleteAfix(resource.getResourceCode(), sqlManager);
        for (int k = 0; k < activ.length; k++) {
          if (fixed[k].equals("T") && !activ[k].equals("-1")) {
            afix.setUsuario(resource.getResourceCode());
            afix.setNume(new Integer(k));
            afix.setActividad(!activ[k].equals("-1") ? activ[k] : "");
            afix.setObjeto(!obj[k].equals("-1") ? obj[k] : null);
            afix.setObjetoAux(!objAux[k].equals("-1") ? objAux[k] : null);
            RactDAO.saveAfix(afix, sqlManager);
          } 
        } 
        event.getRequest().setAttribute("BD", db);
        response.setResultCode(0);
      } 
    } catch (Exception exception) {
      logger.log(Level.SEVERE, "doUpdateRact Exception ", exception);
      response.setAttribute("message", 
          "UpdateRactEventHandler Error procesando RetrieveUser: " + exception);
      response.setAttribute("exception", exception);
      response.setResultCode(200);
    } 
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    System.out.println("doUpdateRact DURATION:" + (duration / 1000000000L));
    System.out.println("*****");
    logger.log(Level.INFO, "**** < doUpdateRact: " + RactDAO.getCurrentDateAndTime());
  }
  
  public String[] SplitHoraCantidad(String hora) {
    String[] resp = null;
    if (hora.indexOf(" ") != -1)
      resp = hora.split("\\s"); 
    return resp;
  }
}
