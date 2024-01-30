package com.gausssoft.commands;

import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.client.MessageTO;
import com.gausssoft.client.UserTO;
import com.gausssoft.service.SessionFacade;
import com.gausssoft.services.SessionFactory;
import com.gausssoft.system.GaussSoftSettings;
import common.user.User;
import java.util.ArrayList;

public class UserLogin implements ICommand {
  private String name;
  
  public void dispose() {}
  
  public UserLogin() {
    this.name = "UserLogin";
  }
  
  public IResponseContext execute(IRequestContext context) {
    SessionFacade session = null;
    ResponseContext responseContext = new ResponseContext();
    String autoLoginValue = "";
    Boolean autoLogin = Boolean.valueOf(false);
    String[] parameters = { "usercode", "password" };
    MessageTO message = new MessageTO();
    try {
      String sessionId = context.getSessionID();
      session = (SessionFacade)SessionFactory.getInstance().getSession(
          "SessionFacade", sessionId);
      session.updateRactParameters();
      responseContext.setResultCode(ResultCode.FAIL);
      responseContext.setValue("message", message.client = "Missing userlogin or password parameter");
      if (!checkParameters(context, parameters))
        return (IResponseContext)responseContext; 
      String userLogin = (String)context.getParameter("usercode");
      String password = (String)context.getParameter("password");
      String newpass = (String)context.getParameter("newpassword");
      autoLoginValue = (String)context.getParameter("AUTOLOGIN");
      if (autoLoginValue != null && autoLoginValue.equals("T"))
        autoLogin = Boolean.valueOf(true); 
      if (autoLogin.booleanValue())
        userLogin = context.getParameter("USERNAME").toString().toUpperCase(); 
      common.user.User user = session.login(userLogin, password, (newpass != null && newpass.length() != 0) ? newpass : null);
      if (user == null) {
        responseContext.setValue("message", message.client = "Error, user not exist or wrong password");
        return (IResponseContext)responseContext;
      } 
      UserTO userTO = new UserTO();
      userTO.name = user.getName();
      userTO.profile = user.getProfile().getName();
      userTO.usercode = user.getLoginName();
      userTO.accessList = (user.getProfile() != null) ? new ArrayList(user.getProfile().getAccessList().keySet()) : null;
      responseContext.setValue(userTO);
      if (user.getCheckSum() == null) {
        responseContext.setValue("message", message.client = "corruptedsystem");
        return (IResponseContext)responseContext;
      } 
      if (user.isLocked()) {
        responseContext.setValue("message", message.client = "userlocked");
        return (IResponseContext)responseContext;
      } 
      if (session.getAuthenticationService().isActive()) {
        if (session.getAuthenticationService().testAuthentication(userLogin, password)) {
          System.out.println("LDAP OK");
          responseContext.setResultCode(ResultCode.SUCCESS);
        } else {
          System.out.println("Falló LDAP");
          responseContext.setValue("message", message.client = "authldaperror");
        } 
        return (IResponseContext)responseContext;
      } 
      if (user.getProfile() != null && !user.hasAccess("RACT")) {
        responseContext.setValue("message", message.client = "noaccess");
        return (IResponseContext)responseContext;
      } 
      if (user.getCheckSum().equals("-2")) {
        System.out.println("Password incorrecto");
        responseContext.setValue("message", message.client = "SEC002");
        return (IResponseContext)responseContext;
      } 
      if (newpass == null && !user.checkPassword(password) && !autoLogin.booleanValue() && !session.getAuthenticationService().isActive()) {
        System.out.println("Password incorrecto");
        responseContext.setValue("message", message.client = "SEC002");
        return (IResponseContext)responseContext;
      } 
      if (!user.verifyCheckSum()) {
        responseContext.setValue("message", message.client = "corruptedsystem");
        return (IResponseContext)responseContext;
      } 
      if (user.ifChangePassword() && !session.getAuthenticationService().isActive()) {
        System.out.println("Falló cambio de clave");
        responseContext.setValue("message", message.client = "changepassword");
        return (IResponseContext)responseContext;
      } 
      if (user != null && !user.getName().equals("-2")) {
        responseContext.setResultCode(ResultCode.SUCCESS);
        String defaultLogo = GaussSoftSettings.getInstance().getSection("ActivityRecordWeb").getString("activityrecord.DefaultLogo", "true");
        userTO.password = defaultLogo;
        if (newpass != null && newpass.length() != 0)
          responseContext.setValue("message", "SECNEWPASS"); 
      } else {
        System.out.println("Usuario:" + user);
        responseContext.setValue("message", message.client = "Error, user not exist or wrong password");
        responseContext.setResultCode(ResultCode.FAIL);
      } 
    } catch (Exception e) {
      responseContext.setValue("message", message.client = e.getMessage());
      responseContext.setExceptionMessage(e.getMessage());
      responseContext.setResultCode(ResultCode.EXCEPTION);
    } 
    return (IResponseContext)responseContext;
  }
  
  private boolean checkParameters(IRequestContext context, String[] parameters) {
    boolean result = true;
    for (int i = 0; i < parameters.length; i++)
      result = (context.containsParameterName(parameters[i]) && result); 
    return result;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}
