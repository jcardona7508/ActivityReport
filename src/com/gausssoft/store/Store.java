package com.gausssoft.store;

import activityrecord.data.CopyProjectDAO;
import activityrecord.data.GraphDataTO;
import activityrecord.data.ProjectDAO;
import activityrecord.data.RactDAO;
import activityrecord.ract.ActiTO;
import activityrecord.ract.Activity;
import activityrecord.ract.ActivityChoiceTO;
import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.ActivityRequestTO;
import activityrecord.ract.Afix;
import activityrecord.ract.AmountWeeklyHours;
import activityrecord.ract.DetailChoiceTO;
import activityrecord.ract.MessageUser;
import activityrecord.ract.MessageUserTO;
import activityrecord.ract.ObjectChoiceTO;
import activityrecord.ract.PercentsPerTimePeriod;
import activityrecord.ract.PeriodType;
import activityrecord.ract.TimePeriod;
import activityrecord.ract.Week;
import activityrecord.ract.WorkPerTimePeriod;
import com.gausssoft.GausssoftException;
import com.gausssoft.client.ActivityTO;
import com.gausssoft.client.CustomFieldTO;
import com.gausssoft.client.WeekDetailTO;
import com.gausssoft.crypto.DESCipher;
import com.gausssoft.store.exception.ApplicationException;
import com.gausssoft.system.GaussSoftSettings;
import com.gausssoft.system.ISettings;
import com.gausssoft.system.SettingsException;
import common.data.UserDAO;
import common.sql.SQLEntry;
import common.sql.SQLManager;
import common.sql.SQLManagerFactory;
import common.sql.SQLResult;
import common.user.User;
import common.web.ClientInfo;
import gausssoft.drawing.UserSpace;
import gausssoft.graph.serie.DateSerieArrayListImplementation;
import gausssoft.graph.serie.DateValue;
import gausssoft.graph.serie.NumericSerieArrayListImplementation;
import java.awt.Dimension;
import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscorp.dbmanagement.DatabaseManagementExceptionSC;
import siscorp.dbmanagement.DatabaseSC;
import siscorp.system.DateSC;

public class Store {
  private static com.gausssoft.store.Store instance = null;
  
  private DatabaseSC dbConnection;
  
  private String dbName;
  
  private String dbDateFormat;
  
  private String applicationPath;
  
  private SQLManager localSqlManager;
  
  private HashMap ractParameters;
  
  private Properties props;
  
  private PrintStream defaultStream;
  
  public Store() throws ApplicationException {
    try {
      this.defaultStream = System.out;
      init();
      this.ractParameters = RactDAO.getRactParameters(this.localSqlManager);
    } catch (Exception e) {
      String code = "CloudLicensingStore error";
      System.out.println("CloudLicensingStore Exception: " + e.getMessage());
      if (e.getMessage().contains("Database Management Exception"))
        code = "dberror"; 
      throw new ApplicationException(code);
    } 
  }
  
  private void init() throws ApplicationException {
    ISettings settings = GaussSoftSettings.getInstance();
    try {
      if (!settings.isSection("ActivityRecordWeb"))
        throw new RuntimeException("ActivityRecordWeb is not installed  or properly configured"); 
      this.props = settings.getSection("ActivityRecordWeb").getProperties();
      DESCipher desCipher = new DESCipher();
      String passwd = this.props.getProperty("activityrecord.Password");
      passwd = desCipher.isEncrypted(passwd) ? desCipher.decrypt(passwd) : passwd;
      this.props.put("activityrecord.Password", passwd);
      this.dbConnection = new DatabaseSC(this.props, "activityrecord");
      this.dbConnection.open();
      Properties sqlProperties = new Properties();
      sqlProperties.put("SqlManager", "common.sql.XMLSqlManager");
      sqlProperties.put("SqlManagerDAO", "common.sql.XmlDAO");
      this.applicationPath = settings.getSection("ActivityRecordWeb").getString("applicationPath", "UNINSTALLED");
      File xmlFile = new File(this.applicationPath);
      xmlFile = new File(xmlFile, "ActivityRecordWeb");
      xmlFile = new File(xmlFile, "XmlFileSql.xml");
      sqlProperties.put("XmlFileSql", xmlFile.getPath());
      SQLManagerFactory sqlManagerFactory = new SQLManagerFactory(sqlProperties);
      this.localSqlManager = sqlManagerFactory.getSQLManager();
      this.localSqlManager.setDatabase(this.dbConnection);
      String dbLocalName = settings.getSection("ActivityRecordWeb").getString("dbName", "UNINSTALLED");
      this.dbName = dbLocalName;
      this.dbConnection.getConnection().setAutoCommit(true);
      String logLevel = this.props.getProperty("LogLevel");
      if (logLevel.equals("DEBUG")) {
        File logFile = new File(this.props.getProperty("LogFileName"));
        PrintStream stream = new PrintStream(logFile);
        System.setOut(stream);
      } else {
        System.setOut(this.defaultStream);
      } 
    } catch (Exception e) {
      String code = "CloudLicensingStore error";
      System.out.println("CloudLicensingStore Exception: " + e.getMessage());
      if (e.getMessage().contains("Database Management Exception"))
        code = "dberror"; 
      throw new ApplicationException(code);
    } 
  }
  
  public void updateRactParameters() throws ApplicationException {
    try {
      boolean change = false;
      ISettings settings = GaussSoftSettings.getInstance();
      Properties propsCurrent = settings.getSection("ActivityRecordWeb").getProperties();
      if (!this.props.getProperty("activityrecord.DSN").equals(propsCurrent.getProperty("activityrecord.DSN")))
        change = true; 
      if (!this.props.getProperty("activityrecord.User").equals(propsCurrent.getProperty("activityrecord.User")))
        change = true; 
      DESCipher desCipher = new DESCipher();
      String passwd = propsCurrent.getProperty("activityrecord.Password");
      passwd = desCipher.isEncrypted(passwd) ? desCipher.decrypt(passwd) : passwd;
      String passwd2 = this.props.getProperty("activityrecord.Password");
      passwd2 = desCipher.isEncrypted(passwd2) ? desCipher.decrypt(passwd2) : passwd2;
      if (!passwd2.equals(passwd))
        change = true; 
      if (change) {
        init();
        System.out.println("Nuevos parámetros de conexión");
      } else {
        this.props = propsCurrent;
      } 
      this.ractParameters = RactDAO.getRactParameters(this.localSqlManager);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ApplicationException("updateRactParameters");
    } 
  }
  
  public static com.gausssoft.store.Store getInstance() throws ApplicationException {
    if (instance == null)
      instance = new com.gausssoft.store.Store(); 
    return instance;
  }
  
  public Properties getEmailProperties() throws ApplicationException {
    Properties mailProperties = new Properties();
    ISettings settings = GaussSoftSettings.getInstance();
    try {
      mailProperties.put("mail.from", settings.getSection("CloudLicensing").getString("mail.from", "UNINSTALLED"));
      mailProperties.put("mail.cc", settings.getSection("CloudLicensing").getString("mail.cc", "gausscloud@gausssoft.com"));
      mailProperties.put("mail.password", settings.getSection("CloudLicensing").getString("mail.password", "UNINSTALLED"));
      mailProperties.put("mail.smtp.host", settings.getSection("CloudLicensing").getString("mail.smtp.host", "UNINSTALLED"));
      mailProperties.put("mail.smtp.port", settings.getSection("CloudLicensing").getString("mail.smtp.port", "UNINSTALLED"));
      mailProperties.put("mail.to", settings.getSection("CloudLicensing").getString("mail.to", "UNINSTALLED"));
      mailProperties.put("mail.user", settings.getSection("CloudLicensing").getString("mail.user", "UNINSTALLED"));
      mailProperties.put("mail.smtp.auth", settings.getSection("CloudLicensing").getString("mail.smtp.auth", "true"));
      mailProperties.put("mail.smtp.starttls.enable", settings.getSection("CloudLicensing").getString("mail.smtp.starttls.enable", "true"));
    } catch (SettingsException e) {
      System.out.println("CloudLicensingStore Exception: " + e.getMessage());
      throw new ApplicationException("getMailProperties error");
    } 
    return mailProperties;
  }
  
  public DatabaseSC getLocalConnection() throws ApplicationException {
    ISettings settings = GaussSoftSettings.getInstance();
    try {
      if (!settings.isSection("ActivityRecordWeb"))
        throw new RuntimeException("ActivityRecordWeb is not installed  or properly configured"); 
      Properties props = settings.getSection("ActivityRecordWeb").getProperties();
      String dbLocalName = settings.getSection("ActivityRecordWeb").getString("dbName", "UNINSTALLED");
      String ldbDateFormat = settings.getSection("ActivityRecordWeb").getString("dbDateFormat", "yyyy/MM/dd");
      if (!dbLocalName.equalsIgnoreCase(this.dbName)) {
        if (this.dbConnection.isOpen())
          this.dbConnection.close(); 
        this.dbConnection = new DatabaseSC(props, "activityrecord");
        this.dbConnection.open();
        this.dbName = dbLocalName;
      } 
      if (!ldbDateFormat.equalsIgnoreCase(this.dbDateFormat))
        this.dbDateFormat = ldbDateFormat; 
      return this.dbConnection;
    } catch (Exception e) {
      System.out.println("getLocalConnection Exception: " + e.getMessage());
      throw new ApplicationException("Database Manager error");
    } 
  }
  
  public String getDbDateFormat() {
    return this.dbDateFormat;
  }
  
  public void commit() throws ApplicationException, DatabaseManagementExceptionSC {
    try {
      getLocalConnection().getConnection().getConnection().commit();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ApplicationException("1", null, new String[] { "ErrorCommit" });
    } 
  }
  
  public void rollback() throws ApplicationException, DatabaseManagementExceptionSC {
    try {
      getLocalConnection().getConnection().getConnection().rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new ApplicationException("1", null, new String[] { "ErrorCommit" });
    } 
  }
  
  public User getUser(String userCode) throws ApplicationException {
    return UserDAO.findUser(userCode, getLocalConnection());
  }
  
  public int updateUser(User user) throws ApplicationException {
    return UserDAO.saveUser(user, getLocalConnection());
  }
  
  public List<ActivityChoiceTO> getResources() throws ApplicationException, GausssoftException {
    List<ActivityChoiceTO> lst = RactDAO.getResources("", this.localSqlManager);
    return lst;
  }
  
  public WeekDetailTO getResourceActivities(String startDate, String endDate, String usuario) throws ApplicationException, GausssoftException {
    ActivityReportResource resource = getResourceByCode(usuario);
    if (resource == null)
      throw new GausssoftException("GETRESOURCE", null); 
    String periodType = resource.getView().toString();
    String dobl = (String)this.ractParameters.get("DOBL");
    try {
      DateSC currTime = new DateSC(startDate, this.dbDateFormat);
      ActivityRecordCalendar calendar = new ActivityRecordCalendar(currTime);
      calendar.setDateFormat(new SimpleDateFormat(this.dbDateFormat));
      if (!resource.getView().equals(PeriodType.DAY)) {
        calendar.gotoFirstDayOfPeriod(resource.getView());
        DateSC startDateSC = calendar.getCurrentTime();
        startDateSC.setFormat(this.dbDateFormat);
        startDate = startDateSC.toString();
        endDate = startDateSC.toString();
      } 
    } catch (ParseException e) {
      e.printStackTrace();
    } 
    WeekDetailTO result = RactDAO.getResourceWeekActivities(startDate, endDate, resource.getResourceCode(), this.localSqlManager);
    if (this.ractParameters.get("RFROM") == null || this.ractParameters.get("RTO") == null)
      throw new GausssoftException("RFROM", null); 
    Date RFROM = java.sql.Date.valueOf(((String)this.ractParameters.get("RFROM")).replace("/", "-"));
    Date RTO = java.sql.Date.valueOf(((String)this.ractParameters.get("RTO")).replace("/", "-"));
    Date startDateSql = java.sql.Date.valueOf(startDate.replace("/", "-"));
    if (startDateSql.compareTo(RFROM) >= 0 && startDateSql.compareTo(RTO) <= 0)
      result.editMode = "T"; 
    result.maxTime = (String)this.ractParameters.get("TIME");
    result.percentLimit = (String)this.ractParameters.get("PERCENT_LIMIT");
    result.showActivityCode = this.props.getProperty("activityrecord.ShowActivityCode", "T");
    startDate.replace("/", "-");
    for (int i = 1; i <= 5; i++) {
      String title = (String)this.ractParameters.get("CUST_" + i + "_TIT");
      if (title != null && !title.trim().equals(""))
        result.customTitles[i - 1] = title; 
      CustomFieldTO customField = new CustomFieldTO();
      String allowNull = (String)this.ractParameters.get("CUST_" + i + "_NUL");
      String type = (String)this.ractParameters.get("CUST_" + i + "_TYP");
      if (type != null && type.length() > 0)
        customField.TYPE = type; 
      if (allowNull != null && allowNull.length() > 0)
        customField.NULL = allowNull; 
      String nList = (String)this.ractParameters.get("CUST_" + i + "_LST_CNT");
      nList = (nList != null) ? nList : "0";
      customField.LIST = new ArrayList();
      if (nList != "0")
        for (int j = 1; j <= Integer.parseInt(nList); j++)
          customField.LIST.add((String)this.ractParameters.get("CUST_" + i + "_LST_" + j));  
      result.customFields.add(customField);
    } 
    result.periodType = periodType;
    result.dobl = dobl;
    return result;
  }
  
  public ActivityReportResource getResourceByCode(String usuario) throws GausssoftException {
    return RactDAO.getResourceByCode(usuario, this.localSqlManager);
  }
  
  public List<ActivityChoiceTO> getResActivities(String resourcecode) throws ApplicationException, GausssoftException {
    ActivityReportResource resource = getResourceByCode(resourcecode);
    resourcecode = resource.getResourceCode().split("/")[0];
    List<ActivityChoiceTO> lst = new ArrayList<>();
    List<Activity> lstAct = RactDAO.getActivities(resourcecode, this.localSqlManager);
    for (Activity act : lstAct) {
      ActivityChoiceTO choice = new ActivityChoiceTO();
      choice.codigo = act.getAcuo();
      choice.nombre = act.getActividad();
      choice.type = (act.getRegistros() > 0) ? "D" : "S";
      choice.customFields = new String[5];
      for (int i = 0; i < choice.customFields.length; i++)
        choice.customFields[i] = act.hasCustomField(i).booleanValue() ? "T" : "F"; 
      lst.add(choice);
    } 
    return lst;
  }
  
  public List<ObjectChoiceTO> getActivityObjects(String resourcecode, String activityCode) throws ApplicationException, GausssoftException {
    ActivityReportResource resource = getResourceByCode(resourcecode);
    resourcecode = resource.getResourceCode().split("/")[0];
    List<ObjectChoiceTO> lst = new ArrayList<>();
    List<Activity> lstAct = RactDAO.getObjects(resourcecode, activityCode, this.localSqlManager);
    for (Activity act : lstAct) {
      ObjectChoiceTO choice = new ObjectChoiceTO();
      choice.codigo = act.getAcuo();
      choice.nombre = act.getObjeto();
      choice.type = (act.getRegistros() > 0) ? "D" : "S";
      choice.customFields = new String[5];
      for (int i = 0; i < choice.customFields.length; i++)
        choice.customFields[i] = act.hasCustomField(i).booleanValue() ? "T" : "F"; 
      lst.add(choice);
    } 
    return lst;
  }
  
  public List<DetailChoiceTO> getObjectDetail(String resourcecode, String activityCode, String objectCode) throws ApplicationException, GausssoftException {
    ActivityReportResource resource = getResourceByCode(resourcecode);
    resourcecode = resource.getResourceCode().split("/")[0];
    List<DetailChoiceTO> lst = new ArrayList<>();
    List<Activity> lstAct = RactDAO.getDetails(resourcecode, activityCode, objectCode, this.localSqlManager);
    for (Activity act : lstAct) {
      DetailChoiceTO choice = new DetailChoiceTO();
      choice.codigo = act.getAcu4();
      choice.nombre = act.getDetail();
      choice.customFields = new String[5];
      for (int i = 0; i < choice.customFields.length; i++)
        choice.customFields[i] = act.hasCustomField(i).booleanValue() ? "T" : "F"; 
      lst.add(choice);
    } 
    return lst;
  }
  
  public Activity getActivity(String resourcecode, String activityCode) throws ApplicationException, GausssoftException {
    return RactDAO.getActivity(resourcecode, activityCode, this.localSqlManager);
  }
  
  public Activity getObject(String resourcecode, String activityCode, String objectCode) throws ApplicationException, GausssoftException {
    return RactDAO.getObject(resourcecode, activityCode, objectCode, this.localSqlManager);
  }
  
  public void doUpdateRact(ActivityRequestTO activityRequestTO) throws GausssoftException {
    long startTime = System.nanoTime();
    DateSC currentTime = new DateSC();
    currentTime.setFormat("yyyy/MM/dd hh:mm:ss");
    Afix afix = new Afix();
    ActivityReportResource resource = getResourceByCode(activityRequestTO.resourcecode);
    RactDAO.deleteAfix(resource.getResourceCode(), this.localSqlManager);
    try {
      int i;
      for (i = 0; i < activityRequestTO.activities.size(); i++) {
        ActiTO acti = activityRequestTO.activities.get(i);
        Activity registro = new Activity();
        registro.setAcur(resource.getResourceCode());
        registro.setFech(acti.FECH);
        registro.generateNumber(i);
        registro.setAcur_acur(resource.getCode());
        registro.setAcur_usua(resource.getUserName());
        registro.setAcua(acti.ACUA);
        registro.setCana(Float.valueOf(Float.parseFloat(acti.CANA)));
        registro.setAcuo(acti.ACUO);
        registro.setCano(Float.valueOf(Float.parseFloat(acti.CANO)));
        registro.setAcu4(acti.ACU4);
        registro.setCome((acti.COME == null) ? "" : acti.COME);
        registro.setStam(currentTime.toString());
        DateSC date = new DateSC(acti.FECH, "yyyy/MM/dd");
        for (int j = 0; j < acti.CUSTOMFIELDS.length; j++)
          registro.setCustomFieldValue(j, acti.CUSTOMFIELDS[j]); 
        Activity tmp = RactDAO.getActivityBy(registro, date, resource.getResourceCode(), this.localSqlManager);
        if (tmp == null) {
          if (registro.getCana().floatValue() > 0.0F || registro.getCano().floatValue() > 0.0F) {
            String newNume = RactDAO.getNextNume(date, resource.getResourceCode(), this.localSqlManager);
            if (newNume != null)
              registro.setNume(newNume); 
            RactDAO.saveRact(registro, this.localSqlManager);
          } 
        } else {
          if (tmp.getCome() == null)
            tmp.setCome(""); 
          if (registro.getCana().floatValue() != tmp.getCana().floatValue() || registro.getCano().floatValue() != tmp.getCano().floatValue() || (registro.getCome() != null && !registro.getCome().equals(tmp.getCome())) || !tmp.getCustomFieldValues().equals(registro.getCustomFieldValues())) {
            RactDAO.deleteRact(registro, date, resource.getResourceCode(), this.localSqlManager);
            if (registro.getCana().floatValue() > 0.0F || registro.getCano().floatValue() > 0.0F) {
              registro.setNume(tmp.getNume());
              RactDAO.saveRact(registro, this.localSqlManager);
            } 
          } 
        } 
      } 
      for (i = 0; i < activityRequestTO.activities.size(); i++) {
        ActiTO acti = activityRequestTO.activities.get(i);
        if (acti.FIXED.equals("T")) {
          afix.setUsuario(resource.getResourceCode());
          afix.setNume(new Integer(i));
          afix.setActividad(acti.ACUA);
          afix.setObjeto(acti.ACUO);
          afix.setObjetoAux(acti.ACU4);
          RactDAO.saveAfix(afix, this.localSqlManager);
        } 
      } 
    } catch (Exception exception) {}
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    System.out.println("SAVE DURATION:" + (duration / 1000000000L));
    System.out.println("*****");
  }
  
  public synchronized GraphDataTO getImage(String resourcecode, String date, String screenwidth, String screenheight) throws ApplicationException, GausssoftException {
    DateSC currTime = null;
    GraphDataTO transferObject = null;
    ActivityReportResource resource = getResourceByCode(resourcecode);
    try {
      currTime = new DateSC(date, this.dbDateFormat);
    } catch (ParseException ex) {
      Logger.getLogger("siscorp.activityreport").log(Level.SEVERE, (String)null, ex);
      currTime = new DateSC();
      currTime.setFormat(this.dbDateFormat);
    } 
    ActivityRecordCalendar calendar = new ActivityRecordCalendar(currTime);
    calendar.setDateFormat(new SimpleDateFormat(this.dbDateFormat));
    PeriodType periodType = resource.getView();
    DateSC fecha = calendar.getCurrentTime();
    fecha.setFormat(this.dbDateFormat);
    ClientInfo clientInfo = new ClientInfo();
    if (Integer.parseInt(screenwidth) >= 800) {
      clientInfo.setScreenWidth(screenwidth);
      clientInfo.setScreenHeight(screenheight);
    } else {
      clientInfo.setScreenWidth("1240");
      clientInfo.setScreenHeight("866");
    } 
    clientInfo.addSupportedResolution(Integer.valueOf(800));
    clientInfo.addSupportedResolution(Integer.valueOf(1024));
    clientInfo.setNameNavigator("mozilla");
    clientInfo.setNavigatorVersion("30.0");
    clientInfo.setPlataform("windows");
    Locale locale = new Locale("es");
    periodType = resource.getView();
    DateSC minDate = DateSC.fromString((String)this.ractParameters.get("RFROM"), 
        "yyyy/MM/dd", new DateSC());
    DateSC maxDate = DateSC.fromString((String)this.ractParameters.get("RTO"), 
        "yyyy/MM/dd", new DateSC());
    int resolution = Integer.parseInt(clientInfo.getScreenWidth());
    Dimension dimension = new Dimension(resolution - 10, 80);
    try {
      DateSC startDate;
      UserSpace userSpace;
      int periods;
      if (periodType == PeriodType.DAY) {
        AmountWeeklyHours summary = RactDAO.getAmountWeeklySummary(resource.getResourceCode(), fecha, 5, this.localSqlManager);
        DateSerieArrayListImplementation dateDataSerie = new DateSerieArrayListImplementation(
            (DateSC[])summary.getFecha().toArray(
              (Object[])new DateSC[summary.getFecha().size()]));
        NumericSerieArrayListImplementation numericDataSerie = new NumericSerieArrayListImplementation(
            (Number[])summary.getCantidad().toArray(
              (Object[])new Double[summary.getCantidad().size()]));
        double userHeight = numericDataSerie.getMaxValue().getValue().doubleValue();
        userSpace = new UserSpace(dimension, 
            0.0D, 
            0.0D, (
            resolution - 10), 
            16.0D, 
            0.0D, 0.0D, 0.0D, 0.0D);
        Week week = new Week(calendar.getCurrentTime());
        dateDataSerie.setDateFormat("dd");
        transferObject = new GraphDataTO(week, 
            dimension, 
            userSpace, 
            dateDataSerie, 
            numericDataSerie, 
            minDate, 
            maxDate);
        periods = numericDataSerie.size();
        startDate = ((DateValue)dateDataSerie.get(0)).getValue();
      } else {
        int titleHeight, years = 4;
        if (periodType == PeriodType.MONTH)
          years = 2; 
        if (periodType == PeriodType.QUARTER)
          years = 4; 
        if (periodType == PeriodType.SEMESTER)
          years = 8; 
        if (periodType == PeriodType.YEAR)
          years = 12; 
        PercentsPerTimePeriod summary = RactDAO.getPercentsPerTimePeriodSummary(resource.getResourceCode(), 
            fecha, years, this.localSqlManager, periodType);
        calendar.setTime((Date)minDate);
        calendar.gotoFirstDayOfPeriod(periodType);
        int sizeInDays = calendar.getSmallPeriodsCount(PeriodType.MONTH, periodType, minDate) * 30;
        if (minDate.diference(calendar.getCurrentTime()).longValue() > (sizeInDays / 2))
          calendar.goNextTimePeriod(periodType); 
        minDate = calendar.getCurrentTime();
        minDate.setFormat("yyyy/MM/dd");
        maxDate = DateSC.fromString((String)this.ractParameters.get("RTO"), 
            "yyyy/MM/dd", new DateSC());
        calendar.setTime((Date)maxDate);
        calendar.gotoLastDayOfPeriod(periodType);
        if (calendar.getCurrentTime().diference(maxDate).longValue() > (sizeInDays / 2))
          calendar.goPreviousTimePeriod(periodType); 
        maxDate = calendar.getCurrentTime();
        maxDate.setFormat("yyyy/MM/dd");
        calendar.setTime((Date)currTime);
        if (periodType == PeriodType.YEAR) {
          titleHeight = 12;
        } else {
          titleHeight = 24;
        } 
        userSpace = new UserSpace(dimension, 
            0.0D, 
            0.0D, (
            resolution - 10), 
            80.0D, 
            0.0D, titleHeight, 0.0D, 12.0D);
        TimePeriod currentPeriod = new TimePeriod(calendar.getCurrentTime(), periodType);
        TimePeriod bigCurrentPeriod = new TimePeriod(calendar.getCurrentTime(), PeriodType.YEAR);
        transferObject = new GraphDataTO(currentPeriod, 
            dimension, 
            userSpace, 
            summary, 
            minDate, 
            maxDate, 
            bigCurrentPeriod, 
            locale);
        periods = summary.getWorkPeriods().size();
        startDate = ((WorkPerTimePeriod)summary.getWorkPeriods().get(0)).getDate();
      } 
      transferObject.htmlMap = getImageMap(periods, (userSpace.getDevice()).width, 
          userSpace.getTopMargin(), 
          userSpace.getDevice().getHeight() - userSpace.getBottomMargin(), 
          startDate, periodType, locale);
      transferObject.setDateSeriesTO(periods, startDate, periodType);
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
    } 
    return transferObject;
  }
  
  private String getImageMap(int periods, double totalWidth, double top, double bottom, DateSC startDate, PeriodType periodType, Locale locale) {
    StringBuilder st = new StringBuilder();
    ActivityRecordCalendar cal = new ActivityRecordCalendar(startDate);
    cal.setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
    double step = totalWidth / periods;
    for (int i = 0; i < periods; i++) {
      String title, date = cal.getCurrentTime().toString();
      switch (periodType) {
        case YEAR:
          title = Integer.toString(cal.get(1));
          break;
        case DAY:
          title = cal.getCurrentTime().toString();
          break;
        default:
          title = String.valueOf(cal.getTimePeriod(periodType, locale)) + "/" + cal.get(1);
          break;
      } 
      st.append("<area title=\"" + title + "\" target=\"_top\" href=\"javascript:changeDate('','" + date + "')\" shape=\"rect\" coords=\"" + (step * i) + "," + top + "," + (step * (i + 1) - 1.0D) + "," + bottom + "\" />");
      cal.goNextTimePeriod(periodType);
    } 
    return st.toString();
  }
  
  public List<ActivityChoiceTO> getProjectsContracts(String resourceCode) {
    return RactDAO.getResourceActivities(resourceCode, "", this.localSqlManager);
  }
  
  public ProjectDAO getProject(String proyectCode, String usuario, String orderBy) {
    return RactDAO.getProyect(proyectCode, usuario, this.localSqlManager, orderBy);
  }
  
  public boolean updateProject(ProjectDAO project, String user, boolean execSP) {
    return RactDAO.updateProject(project, this.localSqlManager, user, execSP);
  }
  
  public boolean copyProyectPriceActivities(CopyProjectDAO copy, String usuario) {
    return RactDAO.copyProyectPriceActivities(copy, usuario, this.localSqlManager);
  }
  
  public void execSP() {
    RactDAO.execSP(this.localSqlManager);
  }
  
  public String getLastDayofMonth(String date) {
    try {
      Date init = (new SimpleDateFormat("yyyy/MM/dd")).parse(date);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(init);
      calendar.add(2, 1);
      calendar.set(5, 1);
      calendar.add(5, -1);
      Date lastDayOfMonth = calendar.getTime();
      DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      return sdf.format(lastDayOfMonth);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public List<ActivityTO> getSummary(String resourceCode, String date) throws GausssoftException {
    String WeekEndDate;
    ActivityReportResource resource = RactDAO.getResourceByCode(resourceCode, this.localSqlManager);
    HashMap<String, String> parameters = new HashMap<>();
    parameters.put("Resource", resource.getResourceCode());
    String WeekStartDate = date;
    if (resource.getView().equals(PeriodType.DAY) || resource.getView().equals(PeriodType.MONTH)) {
      WeekEndDate = getLastDayofMonth(date);
    } else {
      try {
        DateSC dateSC = new DateSC(date, this.dbDateFormat);
        ActivityRecordCalendar cal = new ActivityRecordCalendar(dateSC);
        cal.gotoFirstDayOfPeriod(resource.getView());
        cal.getCurrentTime().setFormat(this.dbDateFormat);
        WeekStartDate = (new SimpleDateFormat(this.dbDateFormat)).format((Date)cal.getCurrentTime());
        cal.gotoLastDayOfPeriod(resource.getView());
        WeekEndDate = (new SimpleDateFormat(this.dbDateFormat)).format((Date)cal.getCurrentTime());
      } catch (Exception e) {
        e.printStackTrace();
        throw new GausssoftException("1");
      } 
    } 
    parameters.put("WeekStartDate", WeekStartDate);
    parameters.put("WeekEndDate", WeekEndDate);
    return RactDAO.getSummary(parameters, this.localSqlManager);
  }
  
  public List<MessageUserTO> getMessageList(String licenseCode) throws GausssoftException {
    ArrayList<MessageUserTO> lst;
    SQLResult result = null;
    String statement = "select fid, flicensecode, fsubject, fmessage, fread, fdate from tmessage where licensecode =" + licenseCode + " order by fdate DESC";
    try {
      lst = new ArrayList<>();
      SQLEntry sql = new SQLEntry();
      sql.setType(0);
      sql.setSqlStatement(statement);
      result = sql.execute(this.localSqlManager.getDatabase(), "");
      if (result.getSuccess())
        while (result.getResult().next()) {
          MessageUser message = loadMessage(result);
          lst.add(message.createTransferObject());
        }  
    } catch (SQLException e) {
      e.printStackTrace();
      throw new GausssoftException("1", null);
    } finally {
      result.close();
    } 
    return lst;
  }
  
  private MessageUser loadMessage(SQLResult result) throws GausssoftException {
    MessageUser msg = new MessageUser();
    try {
      msg.setId((result.getResult().getString("fid") != null) ? Integer.parseInt(result.getResult().getString("fid")) : 0);
      msg.setLicenseCode(result.getResult().getString("flicensecode"));
      msg.setNewmessage(result.getResult().getString("fmessage"));
      msg.setSubject(result.getResult().getString("fsubject"));
      msg.setRead((result.getResult().getString("fread") != null && result.getResult().getString("fread").equals("1")));
      DateSC date = new DateSC(result.getResult().getString("fdate"), this.dbDateFormat);
      date.setFormat(this.dbDateFormat);
      msg.setDate((Date)date);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      throw new GausssoftException("2", null);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new GausssoftException("2", null);
    } catch (ParseException e) {
      e.printStackTrace();
      throw new GausssoftException("2", null);
    } 
    return msg;
  }
}
