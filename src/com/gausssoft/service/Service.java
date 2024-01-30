package com.gausssoft.service;

import activityrecord.data.GraphDataTO;
import activityrecord.ract.ActivityChoiceTO;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.ActivityRequestTO;
import activityrecord.ract.DetailChoiceTO;
import activityrecord.ract.MessageUserTO;
import activityrecord.ract.ObjectChoiceTO;
import com.gausssoft.GausssoftException;
import com.gausssoft.client.ActivityTO;
import com.gausssoft.client.WeekDetailTO;
import com.gausssoft.configuration.mail.SendEmail;
import com.gausssoft.services.IApplicationService;
import com.gausssoft.services.IServiceFacade;
import com.gausssoft.services.ServiceException;
import com.gausssoft.store.IStoreTransaction;
import com.gausssoft.store.Store;
import com.gausssoft.store.exception.ApplicationException;
import common.user.User;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Service implements IApplicationService {
  private static com.gausssoft.service.Service instance = null;
  
  private static final String NAME = "ApplicationService";
  
  private String id;
  
  private Store clStore;
  
  Service() throws ApplicationException 
  {
  	this.clStore = Store.getInstance();    
  }
  
  public static com.gausssoft.service.Service getInstance() throws ApplicationException {
    if (instance == null)
      instance = new com.gausssoft.service.Service(); 
    return instance;
  }
  
  public String getServiceName() {
    return "ApplicationService";
  }
  
  public String getServiceId() {
    return this.id;
  }
  
  public void openService() throws ServiceException {}
  
  public void closeService() throws ServiceException {}
  
  public IServiceFacade getSession(String id) {
    return null;
  }
  
  public IStoreTransaction createTransaction() {
    return null;
  }
  
  public Properties getEmailProperties() throws ApplicationException {
    return this.clStore.getEmailProperties();
  }
  
  public boolean sendEmail(Properties properties) throws ApplicationException {
    SendEmail email = new SendEmail();
    String respCode = email.sendSimpleMail(properties);
    return respCode.equals("EMA001");
  }
  
  public User login(String userCode, String password) throws ApplicationException {
    User user = this.clStore.getUser(userCode);
    if (user != null) {
      if (user.isLocked())
        return user; 
      if (!user.checkPassword(password))
        user.setCheckSum("-2"); 
    } 
    return user;
  }
  
  public int updateUser(User user) throws ApplicationException {
    return this.clStore.updateUser(user);
  }
  
  public List<ActivityChoiceTO> getResources() throws ApplicationException, GausssoftException {
    return this.clStore.getResources();
  }
  
  public String getCurrentDate() {
    SimpleDateFormat formatter = new SimpleDateFormat(this.clStore.getDbDateFormat());
    return formatter.format(new Date());
  }
  
  public String formatDate(String dateOrig) throws ApplicationException {
    try {
      DateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
      DateFormat targetFormat = new SimpleDateFormat(this.clStore.getDbDateFormat());
      Date date = originalFormat.parse(dateOrig);
      return targetFormat.format(date);
    } catch (ParseException e) {
      e.printStackTrace();
      throw new ApplicationException("1", null, new String[] { "formatDate" });
    } 
  }
  
  public WeekDetailTO getResourceActivities(String usuario, String date) throws ApplicationException, GausssoftException {
    return this.clStore.getResourceActivities(formatDate(date), formatDate(date), usuario);
  }
  
  public List<ActivityChoiceTO> getResActivities(String resourcecode) throws ApplicationException, GausssoftException {
    return this.clStore.getResActivities(resourcecode);
  }
  
  public List<ObjectChoiceTO> getActivityObjects(String resourcecode, String activityCode) throws ApplicationException, GausssoftException {
    return this.clStore.getActivityObjects(resourcecode, activityCode);
  }
  
  public List<DetailChoiceTO> getObjectDetail(String resourcecode, String activityCode, String objectCode) throws ApplicationException, GausssoftException {
    return this.clStore.getObjectDetail(resourcecode, activityCode, objectCode);
  }
  
  public void doUpdateRact(ActivityRequestTO activityRequestTO) throws GausssoftException {
    this.clStore.doUpdateRact(activityRequestTO);
  }
  
  public GraphDataTO getImage(String resourcecode, String date, String screenwidth, String screenheight) throws ApplicationException, GausssoftException {
    return this.clStore.getImage(resourcecode, date, screenwidth, screenheight);
  }
  
  public ActivityReportResource getResourceByCode(String usuario) throws GausssoftException {
    return this.clStore.getResourceByCode(usuario);
  }
  
  public List<ActivityTO> getSummary(String resourceCode, String date) throws GausssoftException {
    return this.clStore.getSummary(resourceCode, date);
  }
  
  public void updateRactParameters() throws ApplicationException {
    this.clStore.updateRactParameters();
  }
  
  public List<MessageUserTO> getMessageList(String licenseCode) throws GausssoftException {
    return this.clStore.getMessageList(licenseCode);
  }
}
