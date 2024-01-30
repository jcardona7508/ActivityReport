package activityrecord.engine.action;

import activityrecord.data.RactDAO;
import com.gausssoft.GausssoftException;
import com.gausssoft.configuration.ConfigurationException;
import com.gausssoft.configuration.SettingTO;
import com.gausssoft.configuration.service.ConfigurationSessionFacade;
import com.gausssoft.crypto.DESCipher;
import com.gausssoft.data.file.store.FileStore;
import com.gausssoft.util.Utilities;
import common.data.UserDAO;
import common.engine.GenericComponentManager;
import common.engine.action.DefaultHandler;
import common.engine.event.UserEvent;
import common.user.User;
import common.util.ResourceString;
import common.web.service.LDAPAuthenticationService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import siscorp.dbmanagement.DatabaseManagementExceptionSC;
import siscorp.dbmanagement.DatabaseSC;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.ResourceManager;
import siscorp.framework.application.web.WebResourceManager;

public class UserEventHandler extends DefaultHandler {
  LDAPAuthenticationService authService;
  
  ConfigurationSessionFacade configurationFacade;
  
  public EventResponse execute(ApplicationEvent event) throws GausssoftException {
    UserEvent userEvent = (UserEvent)event;
    EventResponse response = new EventResponse();
    this.authService = new LDAPAuthenticationService(event.getApplicationContext().getResourceManager());
    this.configurationFacade = new ConfigurationSessionFacade();
    switch (userEvent.getEventCode()) {
      case 0:
        doRetrieveUser(event, response);
        return response;
      case 3:
        doLoginConfiguration(event, response);
        return response;
      case 4:
        doRetrieveConfiguration(event, response);
        return response;
      case 5:
        doUpdateConfiguration(event, response);
        return response;
      case 6:
        doTestAuthConfiguration(event, response);
        return response;
      case 7:
        doInstallApplication(event, response);
        return response;
      case 8:
        doTestDB(event, response);
        return response;
      case 9:
        doUnInstallApplication(event, response);
        return response;
    } 
    response.setResultCode(100);
    response.setAttribute("message", "Evento Desconocido");
    return response;
  }
  
  private HashMap<String, String> retrievePropertiesFromEvent(ApplicationEvent event) {
    String escapedHTML;
    DESCipher desCipher = new DESCipher();
    String passwd = event.getRequest().getParameter("Password");
    String appPath = event.getRequest().getParameter("applicationPath");
    String applicationName = event.getApplicationContext().getInitParameter("ApplicationName");
    File file = new File(appPath);
    appPath = event.getRequest().getParameter("applicationPath");
    HashMap<String, String> props = new HashMap<>();
    props.put("applicationPath", file.getAbsolutePath());
    props.put("Alias", "activityrecord");
    props.put("activityrecord.Driver", event.getRequest().getParameter("Driver"));
    props.put("activityrecord.DSN", event.getRequest().getParameter("DSN"));
    props.put("activityrecord.User", event.getRequest().getParameter("User"));
    props.put("activityrecord.Password", !desCipher.isEncrypted(passwd) ? desCipher.encrypt(passwd) : passwd);
    props.put("activityrecord.Connections", "10");
    props.put("activityrecord.UpperCase", (event.getRequest().getParameter("upperCase") == null) ? "false" : "true");
    props.put("activityrecord.RememberUser", (event.getRequest().getParameter("rememberUser") == null) ? "false" : "true");
    props.put("activityrecord.DefaultLogo", (event.getRequest().getParameter("defaultLogo") == null) ? "false" : "true");
    props.put("activityrecord.ShowActivityCode", (event.getRequest().getParameter("showActivityCode") == null) ? "false" : "true");
    props.put("LogLevel", (event.getRequest().getParameter("logLevel") == null) ? "ALL" : "DEBUG");
    if (event.getRequest().getParameter("pie") == null || event.getRequest().getParameter("pie") == " ") {
      escapedHTML = "www.gausssoft.com";
    } else {
      escapedHTML = StringEscapeUtils.escapeHtml4(event.getRequest().getParameter("pie"));
    } 
    props.put("activityrecord.Footer", escapedHTML);
    props.put("activityrecord.PoolConnections", "false");
    props.put("appName", applicationName);
    String workingPath = String.valueOf(file.getAbsolutePath()) + File.separator + "ActivityRecordWeb";
    String tmpPath = String.valueOf(workingPath) + File.separator + "logs";
    props.put("TemporaryUserDirectory", tmpPath);
    props.put("TemporaryUserDirectory", "/activityreportdoc/temporal/ract");
    props.put("StandardOutput", String.valueOf(tmpPath) + File.separator + "ActivityRecordOutput.log");
    props.put("LogFileName", String.valueOf(tmpPath) + File.separator + "ActivityRecord.log");
    props.put("WorkingPath", workingPath);
    props.put("SourceDatabaseList", "activityrecord");
    props.put("NameSpace", "activityrecord");
    props.put("SqlManager", "common.sql.XMLSqlManager");
    props.put("SqlManagerDAO", "common.sql.XmlDAO");
    props.put("XmlFileSql", String.valueOf(workingPath) + File.separator + "XmlFileSql.xml");
    props.put("adminPassword", (event.getRequest().getParameter("adminPassword") == null) ? "ActivityReport" : event.getRequest().getParameter("adminPassword"));
    return props;
  }
  
  private void doInstallApplication(ApplicationEvent event, EventResponse response) throws GausssoftException {
    String message = "Error instalando aplicación ";
    boolean resp = false;
    try {
      response.setResultCode(100);
      String appPath = event.getRequest().getParameter("applicationPath");
      String applicationName = event.getApplicationContext().getInitParameter("ApplicationName");
      File file = new File(appPath);
      if (file.exists()) {
        HashMap<String, String> props = retrievePropertiesFromEvent(event);
        String workingPath = String.valueOf(file.getAbsolutePath()) + File.separator + "ActivityRecordWeb";
        if (!this.configurationFacade.isInstalled(applicationName)) {
          if (resp = this.configurationFacade.installApplication(props, false)) {
            message = "Applicación instalada exitosamente en: " + appPath + ". Reiniciar la aplicación en el servidor para que los cambios tengan efecto.";
            InputStream zipInstallerResources = Utilities.getResourceAsStream("InstallerResources.zip");
            resp = (resp && FileStore.unZipFile(zipInstallerResources, "ActivityRecordWeb", appPath));
            event.getApplicationContext().setResourceManager((ResourceManager)new WebResourceManager(new File(workingPath)));
          } else {
            message = "Error instalando app en: " + appPath;
          } 
        } else if (resp = this.configurationFacade.updateSettings(applicationName, props)) {
          message = "Información actualizada exitosamente";
        } else {
          message = "Error actualizando información";
        } 
        file = new File(String.valueOf(workingPath) + File.separator + "XmlFileSql.xml");
        if (file.exists()) {
          file.delete();
          String xmlFileSql = props.get("activityrecord.Driver");
          if (xmlFileSql.toLowerCase().contains("oracle")) {
            FileStore.copyFile(String.valueOf(workingPath) + File.separator + "XmlFileSqlORACLE.xml", file.getAbsolutePath());
          } else if (xmlFileSql.toLowerCase().contains("mysql")) {
            FileStore.copyFile(String.valueOf(workingPath) + File.separator + "XmlFileSqlMysql.xml", file.getAbsolutePath());
          } else {
            FileStore.copyFile(String.valueOf(workingPath) + File.separator + "XmlFileSqlSQLSERVER.xml", file.getAbsolutePath());
          } 
        } 
      } else {
        message = String.valueOf(message) + ": Directorio " + appPath + " inexistente";
      } 
      if (resp) {
        SettingTO settingTO = this.configurationFacade.getSettings(applicationName);
        response.setResultCode(0);
        response.setAttribute("appSettings", settingTO);
      } else {
        message = "Error instalando app en: " + appPath + ", verifique que el directorio de instalación exista y tenga permisos de escritura";
      } 
      response.setAttribute("message", message);
    } catch (ConfigurationException e) {
      e.printStackTrace();
      throw new GausssoftException("Error doInstallApplication", e);
    } catch (IOException e) {
      e.printStackTrace();
      throw new GausssoftException("Error copying XmlFileSql.xml", e);
    } 
  }
  
  private void doUnInstallApplication(ApplicationEvent event, EventResponse response) throws GausssoftException {
    String message = "Error desinstalando aplicación ";
    try {
      response.setResultCode(100);
      String appPath = event.getRequest().getParameter("applicationPath");
      String applicationName = event.getApplicationContext().getInitParameter("ApplicationName");
      File file = new File(appPath);
      if (file.exists()) {
        String workingPath = String.valueOf(file.getAbsolutePath()) + File.separator + "ActivityRecordWeb";
        if (!this.configurationFacade.uninstallApplication(applicationName)) {
          message = "Error desinstalando app en: " + appPath;
        } else {
          message = "Aplicación desinstalada exitosamente.";
          FileUtils.deleteDirectory(new File(workingPath));
        } 
      } 
      response.setAttribute("message", message);
    } catch (ConfigurationException e) {
      e.printStackTrace();
      throw new GausssoftException("Error doUnInstallApplication", e);
    } catch (IOException e) {
      e.printStackTrace();
      throw new GausssoftException("Error doUnInstallApplication", e);
    } 
  }
  
  private void doTestDB(ApplicationEvent event, EventResponse response) throws GausssoftException {
    String message = "Error de conexión con la BD";
    String applicationName = event.getApplicationContext().getInitParameter("ApplicationName");
    response.setResultCode(100);
    HashMap<String, String> props = retrievePropertiesFromEvent(event);
    SettingTO settingTO = new SettingTO(props, applicationName);
    response.setAttribute("appSettings", settingTO);
    response.setAttribute("message", message);
    response.setAttribute("authProperties", this.authService.getProperties());
    try {
      DatabaseSC db = new DatabaseSC(settingTO.geSettingProperties(), "activityrecord");
      db.open();
      db.getConnection();
      message = "Autenticación exitosa con la BD";
      response.setAttribute("authProperties", this.authService.getProperties());
      response.setAttribute("message", message);
      response.setResultCode(0);
    } catch (DatabaseManagementExceptionSC e) {
      e.printStackTrace();
      message = String.valueOf(message) + " " + e.getCause();
      response.setAttribute("message", message);
    } 
  }
  
  private void doTestAuthConfiguration(ApplicationEvent event, EventResponse response) throws GausssoftException {
    String message, user = event.getRequest().getParameter("user");
    String pass = event.getRequest().getParameter("pass");
    response.setResultCode(0);
    if (this.authService.testAuthentication(user, pass)) {
      message = "Autenticación exitosa LDAP";
    } else {
      message = "Error autenticando usuario " + user;
    } 
    response.setAttribute("clickldap", "click");
    event.getRequest().setAttribute("clickldap", "click");
    event.getRequest().setAttribute("message", message);
  }
  
  private void doUpdateConfiguration(ApplicationEvent event, EventResponse response) throws GausssoftException {
    Properties properties = new Properties();
    properties.setProperty("active", (event.getRequest().getParameter("active") == null) ? "false" : "true");
    properties.setProperty("server", (event.getRequest().getParameter("ipServer") == null) ? "localhost" : event.getRequest().getParameter("ipServer"));
    properties.setProperty("port", (event.getRequest().getParameter("port") == null) ? "389" : event.getRequest().getParameter("port"));
    properties.setProperty("domain", (event.getRequest().getParameter("domain") == null) ? "localhost.com" : event.getRequest().getParameter("domain"));
    try {
      this.authService.storeProperties(properties);
      response.setResultCode(0);
      response.setAttribute("authProperties", properties);
      response.setAttribute("message", "Información actualizada");
      event.getRequest().setAttribute("clickldap", "click");
      event.getRequest().setAttribute("message", "Información actualizada exitosamente");
    } catch (GausssoftException e) {
      response.setAttribute("message", "Error actualizando información");
      response.setResultCode(200);
      e.printStackTrace();
      throw new GausssoftException("Error reading Authentication.properties", e);
    } 
  }
  
  protected void doRetrieveConfiguration(ApplicationEvent event, EventResponse response) throws GausssoftException {
    response.setResultCode(100);
    try {
      String applicationName = event.getApplicationContext().getInitParameter("ApplicationName");
      SettingTO settings = new SettingTO();
      if (this.configurationFacade.isInstalled(applicationName)) {
        settings = this.configurationFacade.getSettings(applicationName);
        response.setAttribute("appSettings", settings);
      } 
      System.out.println("configLogin");
      Properties properties = this.authService.getProperties();
      response.setResultCode(0);
      response.setAttribute("authProperties", properties);
      response.setAttribute("clickldap", event.getRequest().getAttribute("clickldap"));
    } catch (ConfigurationException e) {
      e.printStackTrace();
      throw new GausssoftException("Error doRetrieveConfiguration", e);
    } 
  }
  
  protected void doLoginConfiguration(ApplicationEvent event, EventResponse response) throws GausssoftException {
    ResourceString resourceString = (ResourceString)event.getRequest().getSession().getAttribute(
        "RESOURCESTRING");
    response.setResultCode(100);
    System.out.println("configLogin");
    String password = event.getRequest().getParameter("password");
    try {
      String applicationName = event.getApplicationContext().getInitParameter("ApplicationName");
      if (this.configurationFacade.logIn(applicationName, password)) {
        response.setResultCode(0);
      } else {
        response.setAttribute("message", (resourceString != null) ? resourceString.getString("wrongpass") : "Clave errada");
      } 
    } catch (ConfigurationException e) {
      e.printStackTrace();
      throw new GausssoftException("Error doRetrieveConfiguration", e);
    } 
  }
  
  protected void doRetrieveUser(ApplicationEvent event, EventResponse response) {
    String username = null;
    String password = null;
    String changePassword = null;
    String autoLoginValue = "";
    Boolean autoLogin = Boolean.valueOf(false);
    autoLoginValue = (String)event.getRequest().getSession().getAttribute("AUTOLOGIN");
    if (autoLoginValue != null && autoLoginValue.equals("T"))
      autoLogin = Boolean.valueOf(true); 
    if (!autoLogin.booleanValue()) {
      username = event.getRequest().getParameter("username").toUpperCase();
      password = event.getRequest().getParameter("password");
    } else {
      username = (String)event.getRequest().getSession().getAttribute("USERNAME");
      username = username.toUpperCase();
    } 
    ResourceString resourceString = (ResourceString)event.getRequest().getSession().getAttribute(
        "RESOURCESTRING");
    boolean license = false;
    Calendar cal = Calendar.getInstance();
    Calendar currentcal = Calendar.getInstance();
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    logger.log(Level.INFO, "**** > doRetrieveUser (Login): " + RactDAO.getCurrentDateAndTime());
    long startTime = System.nanoTime();
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext().getAtribute(
          "siscorp.framework.application.ComponentManager");
      if (manager == null)
        throw new Exception("Component Manager = NULL "); 
      DatabaseSC db = manager.getDatabase("activityrecord");
      User user = UserDAO.findUser(username, db);
      if (user == null) {
        response.setResultCode(100);
        response.setAttribute("message", resourceString.getStringReplace("usuario", "x=" + username));
        return;
      } 
      if (user.isLocked()) {
        response.setResultCode(100);
        response.setAttribute("user", user);
        response.setAttribute("message", resourceString.getString("userlocked"));
        return;
      } 
      if (this.authService.isActive())
        if (this.authService.testAuthentication(username, password)) {
          response.setResultCode(0);
          response.setAttribute("user", user);
          response.setAttribute("message", resourceString.getStringReplace("bienvenida", "x=" + username));
        } else {
          response.setResultCode(100);
          response.setAttribute("message", resourceString.getString("authldaperror"));
          return;
        }  
      if (!user.hasAccess("RACT")) {
        response.setResultCode(100);
        response.setAttribute("user", user);
        response.setAttribute("message", resourceString.getString("noaccess"));
        return;
      } 
      if (!user.verifyCheckSum()) {
        response.setResultCode(100);
        response.setAttribute("user", user);
        response.setAttribute("message", resourceString.getString("corruptedsystem"));
        return;
      } 
      if (!user.checkPassword(password) && !autoLogin.booleanValue() && !this.authService.isActive()) {
        response.setResultCode(100);
        response.setAttribute("user", user);
        response.setAttribute("message", resourceString.getString("password"));
        return;
      } 
      response.setAttribute("user", user);
      changePassword = event.getRequest().getParameter("changePassword");
      if (changePassword != null && changePassword.equals("true")) {
        changePassword(event, response, user);
        return;
      } 
      if (user.ifChangePassword() && !this.authService.isActive()) {
        response.setResultCode(100);
        response.setAttribute("user", user);
        response.setAttribute("message", resourceString.getString("changepassword"));
        return;
      } 
      response.setResultCode(0);
      response.setAttribute("message", 
          resourceString.getStringReplace("bienvenida", "x=" + user
            .getName()));
    } catch (Exception exception) {
      response.setResultCode(200);
      response.setAttribute("message", resourceString.getString("eventhandler"));
      response.setAttribute("exception", exception);
    } 
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    logger.log(Level.INFO, "doRetrieveUser (Login) DURATION:" + (duration / 1000000000L));
    logger.log(Level.INFO, "*****");
    logger.log(Level.INFO, "**** < doRetrieveUser (Login): " + RactDAO.getCurrentDateAndTime());
  }
  
  protected void changePassword(ApplicationEvent event, EventResponse response, User user) {
    String newPassword = null;
    ResourceString resourceString = (ResourceString)event.getRequest().getSession().getAttribute(
        "RESOURCESTRING");
    newPassword = event.getRequest().getParameter("newpassword");
    try {
      GenericComponentManager manager = (GenericComponentManager)event.getApplicationContext().getAtribute(
          "siscorp.framework.application.ComponentManager");
      if (manager == null)
        throw new Exception("Component Manager = NULL "); 
      DatabaseSC db = manager.getDatabase("activityrecord");
      user.setPassword(newPassword.toUpperCase());
      UserDAO.saveUser(user, db);
      response.setResultCode(0);
      response.setAttribute("message", 
          resourceString.getStringReplace("bienvenida", "x=" + user
            .getName()));
    } catch (Exception exception) {
      response.setResultCode(200);
      response.setAttribute("message", resourceString.getString("errorchangepass"));
      response.setAttribute("exception", exception);
    } 
  }
}
