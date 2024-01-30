package com.gausssoft.service;

import activity.User;
import activityrecord.data.GraphDataTO;
import activityrecord.ract.ActivityChoiceTO;
import activityrecord.ract.ActivityRequestTO;
import activityrecord.ract.DetailChoiceTO;
import activityrecord.ract.MessageUserTO;
import activityrecord.ract.ObjectChoiceTO;
import com.gausssoft.GausssoftException;
import com.gausssoft.client.ActivityTO;
import com.gausssoft.client.WeekDetailTO;
import com.gausssoft.service.Service;
import com.gausssoft.services.IServiceFacade;
import com.gausssoft.store.exception.ApplicationException;
import com.gausssoft.system.GaussSoftSettings;
import com.gausssoft.system.ISettings;
import common.web.service.LDAPAuthenticationService;
import java.io.File;
import java.util.List;
import java.util.Properties;
import siscorp.framework.application.ResourceManager;
import siscorp.framework.application.web.WebResourceManager;

public class SessionFacade implements IServiceFacade {
  public static final String NAME = "SessionFacade";
  
  private String id = null;
  
  private common.user.User currentUser;
  
  private final Service cldService = Service.getInstance();
  
  private LDAPAuthenticationService authenticationService;
  
  public SessionFacade() throws ApplicationException {
    ISettings settings = GaussSoftSettings.getInstance();
    try {
      if (!settings.isSection("ActivityRecordWeb"))
        throw new RuntimeException("ActivityRecordWeb is not installed  or properly configured"); 
      String workingPath = settings.getSection("ActivityRecordWeb").getString("WorkingPath", "UNINSTALLED");
      this.authenticationService = new LDAPAuthenticationService((ResourceManager)new WebResourceManager(new File(workingPath)));
    } catch (Exception e) {
      throw new ApplicationException("Database Manager error");
    } 
  }
  
  public LDAPAuthenticationService getAuthenticationService() {
    return this.authenticationService;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public boolean close() {
    return false;
  }
  
  public Properties getEmailProperties() throws ApplicationException {
    return this.cldService.getEmailProperties();
  }
  
  public boolean sendEmail(String emailTo, String subject, String message, String cc) throws ApplicationException {
    Properties emailProperties = getEmailProperties();
    emailProperties.setProperty("mail.user", "noreply@gausssoft.com");
    emailProperties.setProperty("mail.from", "noreply@gausssoft.com");
    emailProperties.setProperty("mail.password", "R@d1@lV1ewer");
    emailProperties.setProperty("mail.to", emailTo);
    emailProperties.setProperty("mail.subject", subject);
    emailProperties.setProperty("mail.body", message);
    if (cc != null && cc.length() != 0) {
      emailProperties.setProperty("mail.cc", cc);
    } else {
      emailProperties.remove("mail.cc");
    } 
    return this.cldService.sendEmail(emailProperties);
  }
  
  public common.user.User login(String userCode, String password, String newPass) throws ApplicationException, Exception {
	  common.user.User user = this.cldService.login(userCode, password);
    if (user != null && !user.getCheckSum().equals("-2"))
      if (newPass != null) {
        user.setPassword(newPass);
        this.cldService.updateUser(user);
      }  
    this.currentUser = user;
    return user;
  }
  
  public boolean validateSessionUser(String code) {
    boolean resp = false;
    if (this.currentUser != null && this.currentUser.getLoginName().equals(code)) {
      resp = true;
    } else {
      System.out.println("Session user error: " + code + " has an invalid session");
    } 
    return resp;
  }
  
  public int updateUser(User user) throws ApplicationException {
    return updateUser(user);
  }
  
  public static void main(String[] args) {
    try {
      com.gausssoft.service.SessionFacade x = new com.gausssoft.service.SessionFacade();
      x.login("jcardona", "jcardona", "xxx");
    } catch (ApplicationException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public List<ActivityChoiceTO> getResources() throws ApplicationException, GausssoftException {
    return this.cldService.getResources();
  }
  
  public WeekDetailTO getResourceActivities(String usuario, String date) throws ApplicationException, GausssoftException {
    return this.cldService.getResourceActivities(usuario, date);
  }
  
  public List<ActivityChoiceTO> getResActivities(String resourcecode) throws ApplicationException, GausssoftException {
    return this.cldService.getResActivities(resourcecode);
  }
  
  public List<ObjectChoiceTO> getActivityObjects(String resourcecode, String activityCode) throws ApplicationException, GausssoftException {
    return this.cldService.getActivityObjects(resourcecode, activityCode);
  }
  
  public List<DetailChoiceTO> getObjectDetail(String resourcecode, String activityCode, String objectCode) throws ApplicationException, GausssoftException {
    return this.cldService.getObjectDetail(resourcecode, activityCode, objectCode);
  }
  
  public void doUpdateRact(ActivityRequestTO activityRequestTO) throws GausssoftException {
    this.cldService.doUpdateRact(activityRequestTO);
  }
  
  public GraphDataTO getImage(String resourcecode, String date, String screenwidth, String screenheight) throws ApplicationException, GausssoftException {
    return this.cldService.getImage(resourcecode, date, screenwidth, screenheight);
  }
  
  public String getPeriodTime(String usuario) throws GausssoftException {
    return this.cldService.getResourceByCode(usuario).getView().toString();
  }
  
  public List<ActivityTO> getSummary(String resourceCode, String date) throws GausssoftException {
    return this.cldService.getSummary(resourceCode, date);
  }
  
  public void updateRactParameters() throws ApplicationException {
    this.cldService.updateRactParameters();
  }
  
  public List<MessageUserTO> getMessageList(String licenseCode) throws GausssoftException {
    return this.cldService.getMessageList(licenseCode);
  }
}
