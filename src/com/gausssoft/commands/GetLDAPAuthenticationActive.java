package com.gausssoft.commands;

import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.client.MessageTO;
import com.gausssoft.system.GaussSoftSettings;
import com.gausssoft.system.SettingsException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class GetLDAPAuthenticationActive implements ICommand {
  private String name;
  
  public GetLDAPAuthenticationActive() {
    setName("GetLDAPAuthenticationActive");
  }
  
  public IResponseContext execute(IRequestContext request) {
    ResponseContext responseContext = new ResponseContext();
    MessageTO message = new MessageTO();
    try {
      String workingPath = GaussSoftSettings.getInstance().getSection("ActivityRecordWeb").getString("WorkingPath", "");
      String footer = GaussSoftSettings.getInstance().getSection("ActivityRecordWeb").getString("activityrecord.Footer", "");
      String remember = GaussSoftSettings.getInstance().getSection("ActivityRecordWeb").getString("activityrecord.RememberUser", "");
      Properties properties = new Properties();
      properties.load(new FileInputStream(new File(workingPath, "Authentication.properties")));
      responseContext.setValue("{\"ldap\":\"" + properties.getProperty("active") + "\",\"footer\":\"" + footer + "\",\"remember\":\"" + remember + "\"}");
      responseContext.setResultCode(ResultCode.SUCCESS);
    } catch (SettingsException|java.io.IOException e) {
      responseContext.setValue("message", message.client = e.getMessage());
      responseContext.setExceptionMessage(e.getMessage());
      responseContext.setResultCode(ResultCode.EXCEPTION);
    } 
    return (IResponseContext)responseContext;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void dispose() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
