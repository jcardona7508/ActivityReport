package activityrecord.web.action;

import activityrecord.ract.Activity;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.WeekDetail;
import com.gausssoft.GausssoftException;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.system.DateSC;

public class LoadActivitiesAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    String hora = null, cantidad = null;
    Session session = event.getRequest().getSession();
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    WeekDetail weekDetail = (WeekDetail)session.getAttribute("ACTIVITIES");
    String dateFormatString = (String)session.getAttribute("DATEFORMAT");
    if (dateFormatString == null)
      dateFormatString = "yyyy/MM/dd"; 
    DateSC currentTime = new DateSC();
    currentTime.setFormat("yyyy/MM/dd hh:mm:ss");
    if (resource == null)
      return response; 
    try {
      String[] fecha = event.getRequest().getParameterValues("currentdate");
      String[] activ = event.getRequest().getParameterValues("idactividad");
      String[] obj = event.getRequest().getParameterValues("idobjeto");
      String[] objAux = event.getRequest().getParameterValues("idobjetoaux");
      String[] fixed = event.getRequest().getParameterValues("fixed");
      if (activ == null || 
        obj == null || 
        objAux == null || 
        fixed == null) {
        response.setResultCode(0);
      } else {
        for (int i = 0; i < fecha.length; i++) {
          String[] horas = event.getRequest()
            .getParameterValues("horas" + fecha[i]);
          String[] cantidades = event.getRequest()
            .getParameterValues("cano" + fecha[i]);
          String[] coment = event.getRequest()
            .getParameterValues("comentario" + fecha[i]);
          DateSC currentDate = new DateSC(fecha[i], dateFormatString);
          for (int j = 0; j < activ.length; j++) {
            hora = null;
            hora = (horas[j].trim().length() != 0) ? horas[j] : "0";
            cantidad = (cantidades[j].trim().length() != 0) ? cantidades[j] : hora;
            String key = String.valueOf(activ[j]) + obj[j] + objAux[j];
            Activity registro = null;
            if (weekDetail.getDailyDetail(currentDate).contains(key))
              registro = (Activity)weekDetail.getDailyDetail(currentDate).getActivity(key).clone(); 
            if (registro != null) {
              DateSC date = new DateSC(fecha[i], "yyyy/MM/dd");
              registro.setAcur(resource.getCode());
              registro.setFech(date.toString());
              registro.generateNumber(j);
              registro.setAcua(activ[j]);
              registro.setCana(hora);
              registro.setAcuo(obj[j]);
              registro.setCano(cantidad);
              registro.setAcu4(objAux[j]);
              registro.setCome(coment[j]);
              registro.setStam(currentTime.toString());
              weekDetail.getWeekSummary().getActivity(registro).setFixed(fixed[j].equals("T"));
              registro.setFixed(fixed[j].equals("T"));
              weekDetail.getDailyDetail(currentDate).set(registro);
            } 
          } 
        } 
        response.setResultCode(0);
      } 
    } catch (Exception e) {
      response.setResultCode(200);
      response.setAttribute("message", "Error indeterminado ver log");
    } 
    return response;
  }
}
