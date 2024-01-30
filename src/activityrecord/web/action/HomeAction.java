package activityrecord.web.action;

import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.PeriodType;
import com.gausssoft.GausssoftException;
import common.user.User;
import common.util.ResourceString;
import common.web.action.GenericAction;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;

public class HomeAction extends GenericAction {
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    EventResponse response = new EventResponse();
    ApplicationEvent nextEvent = null;
    Session session = event.getRequest().getSession();
    User user = (User)session.getAttribute("USER");
    ResourceString i18nTexts = (ResourceString)session.getAttribute("RESOURCESTRING");
    if (user == null) {
      response.setResultCode(2);
      response.setAttribute("page", "/jsp/login.jsp");
      if (i18nTexts != null) {
        String message = i18nTexts.getString("inicio");
        response.setAttribute("message", message);
      } else {
        response.setAttribute("message", "You must initiate a session.");
      } 
      return response;
    } 
    ActivityReportResource resource = (ActivityReportResource)session.getAttribute("RESOURCE");
    if (resource == null)
      if (user.hasAccess("PRACT")) {
        nextEvent = new ApplicationEvent("promptResource", event.getRequest(), 
            event.getApplicationContext());
        response.setNextEvent(nextEvent);
      } else {
        nextEvent = new ApplicationEvent("getResource", event.getRequest(), 
            event.getApplicationContext());
        response.setNextEvent(nextEvent);
      }  
    if (resource != null) {
      PeriodType fview = resource.getView();
      boolean ok = false;
      if (fview == PeriodType.DAY) {
        session.setAttribute("lastSmallUnit", "&lt; " + i18nTexts.getString("navigate.PrevDay"));
        session.setAttribute("lastMediumUnit", "&lt;&lt; " + i18nTexts.getString("navigate.PrevWeek"));
        session.setAttribute("lastBigUnit", "&lt;&lt;&lt; " + i18nTexts.getString("navigate.PrevMonth"));
        session.setAttribute("nextSmallUnit", String.valueOf(i18nTexts.getString("navigate.NextDay")) + " &gt;");
        session.setAttribute("nextMediumUnit", String.valueOf(i18nTexts.getString("navigate.NextWeek")) + " &gt;&gt;");
        session.setAttribute("nextBigUnit", String.valueOf(i18nTexts.getString("navigate.NextMonth")) + " &gt;&gt;&gt;");
        ok = true;
      } else {
        session.setAttribute("lastMediumUnit", "&lt;&lt; " + i18nTexts.getString("navigate.PrevYear"));
        session.setAttribute("nextMediumUnit", String.valueOf(i18nTexts.getString("navigate.NextYear")) + " &gt;&gt;");
        session.setAttribute("lastBigUnit", "");
        session.setAttribute("nextBigUnit", "");
      } 
      if (fview == PeriodType.MONTH) {
        session.setAttribute("lastSmallUnit", "&lt; " + i18nTexts.getString("navigate.PrevMonth"));
        session.setAttribute("nextSmallUnit", String.valueOf(i18nTexts.getString("navigate.NextMonth")) + " &gt;");
        ok = true;
      } 
      if (fview == PeriodType.QUARTER) {
        session.setAttribute("lastSmallUnit", "&lt; " + i18nTexts.getString("navigate.PrevQuarter"));
        session.setAttribute("nextSmallUnit", String.valueOf(i18nTexts.getString("navigate.NextQuarter")) + " &gt;");
        ok = true;
      } 
      if (fview == PeriodType.SEMESTER) {
        session.setAttribute("lastSmallUnit", "&lt; " + i18nTexts.getString("navigate.PrevSemester"));
        session.setAttribute("nextSmallUnit", String.valueOf(i18nTexts.getString("navigate.NextSemester")) + " &gt;");
        ok = true;
      } 
      if (fview == PeriodType.YEAR) {
        session.setAttribute("lastMediumUnit", "");
        session.setAttribute("nextMediumUnit", "");
        session.setAttribute("lastSmallUnit", "&lt; " + i18nTexts.getString("navigate.PrevYear"));
        session.setAttribute("nextSmallUnit", String.valueOf(i18nTexts.getString("navigate.NextYear")) + " &gt;");
        ok = true;
      } 
      if (!ok) {
        response.setAttribute("message", "Problems with Period type.");
        return response;
      } 
      response.setResultCode(0);
    } 
    return response;
  }
}
