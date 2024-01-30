package activityrecord.data;

import activityrecord.data.CopyProjectDAO;
import activityrecord.data.ProjectDAO;
import activityrecord.data.ProyectActivityDAO;
import activityrecord.ract.Activity;
import activityrecord.ract.ActivityChoiceTO;
import activityrecord.ract.ActivityRecordCalendar;
import activityrecord.ract.ActivityReportResource;
import activityrecord.ract.Afix;
import activityrecord.ract.AmountWeeklyHours;
import activityrecord.ract.DailyDetail;
import activityrecord.ract.DetailChoiceTO;
import activityrecord.ract.ObjectChoiceTO;
import activityrecord.ract.PercentsPerTimePeriod;
import activityrecord.ract.PeriodType;
import activityrecord.ract.UserAOAux;
import activityrecord.ract.WeekDetail;
import activityrecord.ract.WorkPerTimePeriod;
import com.gausssoft.GausssoftException;
import com.gausssoft.client.ActivityTO;
import com.gausssoft.client.WeekDetailTO;
import common.sql.SQLEntry;
import common.sql.SQLManager;
import common.sql.SQLResult;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscorp.dbmanagement.DatabaseManagementExceptionSC;
import siscorp.system.DateSC;

public class RactDAO {
  public static synchronized int saveRact(Activity ract, SQLManager sqlManager) {
    int rowsAfected = 0;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    HashMap<Object, Object> parameters = new HashMap<>(10);
    try {
      parameters.put("acur", ract.getAcur());
      parameters.put("acuracum", ract.getAcur_acur());
      parameters.put("acurusua", ract.getAcur_usua());
      parameters.put("nume", ract.getNume());
      parameters.put("fech", ract.getFech());
      parameters.put("acua", ract.getAcua());
      parameters.put("cana", ract.getCana());
      parameters.put("acuo", ract.getAcuo());
      parameters.put("cano", ract.getCano());
      parameters.put("acu4", ract.getAcu4());
      parameters.put("come", (ract.getCome().length() == 0) ? " " : ract.getCome());
      parameters.put("stam", ract.getStam());
      parameters.put("cust1", ract.getCustomFieldValue(0));
      parameters.put("cust2", ract.getCustomFieldValue(1));
      parameters.put("cust3", ract.getCustomFieldValue(2));
      parameters.put("cust4", ract.getCustomFieldValue(3));
      parameters.put("cust5", ract.getCustomFieldValue(4));
      parameters.put("fbill", ract.isBillable().booleanValue() ? "T" : "F");
      SQLEntry sql = sqlManager.findSQL("InsertRact");
      SQLResult result = sql.execute(sqlManager.getDatabase(), parameters);
      if (result.getSuccess())
        rowsAfected = 1; 
    } catch (Exception e) {
      logger.log(Level.SEVERE, " Error Ejecutando InsertRact" + e);
      System.out.println("Error grabando RACT." + e);
      e.printStackTrace(System.out);
    } 
    return rowsAfected;
  }
  
  public static synchronized List getResourceActivities(String usuario, String condition, SQLManager sqlManager) {
    ArrayList<ActivityChoiceTO> list = null;
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    list = new ArrayList(50);
    HashMap<Object, Object> parameters = new HashMap<>(5);
    parameters.put("Resource", usuario);
    parameters.put("Condition", condition);
    try {
      logger.log(Level.INFO, " > Ejecutando GetResActivities");
      SQLEntry sql = sqlManager.findSQL("GetResActivities");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      if (result.getSuccess())
        while (result.getResult().next()) {
          ActivityChoiceTO choice = new ActivityChoiceTO();
          choice.codigo = result.getResult().getString(1);
          choice.nombre = result.getResult().getString(2);
          choice.tipo = result.getResult().getString(3);
          choice.unidad = result.getResult().getString(4);
          list.add(choice);
        }  
      logger.log(Level.INFO, " < GetResActivities Ejecutado");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetActivities" + exception);
      System.out.println("Error getResourceActivities" + exception);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return list;
  }
  
  public static synchronized List getObjectDetail(String usuario, String activityCode, String objectCode, String condition, SQLManager sqlManager) {
    ArrayList<DetailChoiceTO> list = null;
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    list = new ArrayList(50);
    HashMap<Object, Object> parameters = new HashMap<>(5);
    parameters.put("Resource", usuario);
    parameters.put("Activity", activityCode);
    parameters.put("Object", objectCode);
    parameters.put("Condition", condition);
    try {
      logger.log(Level.INFO, " > Ejecutando getObjectDetail");
      SQLEntry sql = sqlManager.findSQL("GetObjectDetail");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      if (result.getSuccess())
        while (result.getResult().next()) {
          DetailChoiceTO choice = new DetailChoiceTO();
          choice.codigo = result.getResult().getString(1);
          choice.nombre = result.getResult().getString(2);
          list.add(choice);
        }  
      logger.log(Level.INFO, " < getObjectDetail Ejecutado");
    } catch (Exception exception) {
      System.out.println("Error getObjectDetails" + exception);
      logger.log(Level.SEVERE, " Error Ejecutando getObjectDetails " + exception);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return list;
  }
  
  public static synchronized List getActivityObjects(String usuario, String activityCode, String condition, SQLManager sqlManager) {
    ArrayList<ObjectChoiceTO> list = null;
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    list = new ArrayList(50);
    HashMap<Object, Object> parameters = new HashMap<>(5);
    parameters.put("Resource", usuario);
    parameters.put("Activity", activityCode);
    parameters.put("Condition", condition);
    try {
      logger.log(Level.INFO, " > Ejecutando getActivityObjects");
      SQLEntry sql = sqlManager.findSQL("GetActivityObjects");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      if (result.getSuccess())
        while (result.getResult().next()) {
          ObjectChoiceTO choice = new ObjectChoiceTO();
          choice.codigo = result.getResult().getString(1);
          choice.nombre = result.getResult().getString(2);
          list.add(choice);
        }  
      logger.log(Level.INFO, " < getActivityObjects Ejecutado");
    } catch (Exception exception) {
      System.out.println("Error getActivityObjects" + exception);
      logger.log(Level.SEVERE, " Error Ejecutando getActivityObjects " + exception);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return list;
  }
  
  public static synchronized Activity getActivityObjectDetail(SQLManager sqlManager, SQLEntry sql, HashMap parameters) {
    Activity activity = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    SQLResult result = sql.execute(sqlManager.getDatabase(), parameters);
    try {
      if (result.getSuccess())
        while (result.getResult().next()) {
          activity = new Activity();
          activity.setAcur(result.getResult().getString(1));
          activity.setNume(result.getResult().getString(2));
          activity.setFech(result.getResult().getString(3));
          activity.setAcua(result.getResult().getString(4));
          activity.setActividad(result.getResult().getString(5));
          activity.setCana(new Float(result.getResult().getFloat(6)));
          activity.setAcuo(result.getResult().getString(7));
          activity.setObjeto(result.getResult().getString(8));
          activity.setCano(new Float(result.getResult().getFloat(9)));
          activity.setAcu4(result.getResult().getString(10));
          activity.setDetail(result.getResult().getString(11));
          activity.setCome(result.getResult().getString(12));
          activity.setStam(result.getResult().getString(13));
          String fixed = result.getResult().getString(14);
          activity.setFixed(fixed.equals("T"));
          activity.setType(result.getResult().getString(15));
          for (int i = 0; i < 5; i++) {
            String str = result.getResult().getString(i + 16);
            if (str.equals("T"))
              activity.enableCustomField(i); 
          } 
          String booleanValue = result.getResult().getString(21);
          activity.setBillable(Boolean.valueOf(booleanValue.equals("T")));
          activity.setRegistros(result.getResult().getInt(22));
          logger.log(Level.FINER, "RactDAO.getActivityObjectDetail.fech: " + activity.getFech());
          logger.log(Level.FINER, "RactDAO.getActivityObjectDetail.come: " + activity.getCome());
        }  
      logger.log(Level.INFO, " < GetActivityObjects Ejecutado");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetActivityObjects" + exception);
      System.out.println("RACTDAO.getActivity" + exception);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return activity;
  }
  
  public static synchronized Activity getActivity(String resourceCode, String activityCode, SQLManager sqlManager) {
    Activity activity = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    HashMap<Object, Object> parameters = new HashMap<>(5);
    parameters.put("Code", activityCode);
    parameters.put("Resource", resourceCode);
    try {
      logger.log(Level.INFO, " > Ejecutando GetActivity");
      SQLEntry sql = sqlManager.findSQL("GetActivity");
      activity = getActivityObjectDetail(sqlManager, sql, parameters);
      logger.log(Level.INFO, " < GetActivity Ejecutado");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetActivity" + exception);
      System.out.println("RACTDAO.getActivity" + exception);
    } 
    return activity;
  }
  
  public static synchronized Activity getObject(String resourceCode, String activityCode, String objectCode, SQLManager sqlManager) {
    Activity activity = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    HashMap<Object, Object> parameters = new HashMap<>(5);
    parameters.put("ActivityCode", activityCode);
    parameters.put("ObjectCode", objectCode);
    parameters.put("Resource", resourceCode);
    try {
      logger.log(Level.INFO, " > Ejecutando GetObject");
      SQLEntry sql = sqlManager.findSQL("GetObject");
      activity = getActivityObjectDetail(sqlManager, sql, parameters);
      logger.log(Level.INFO, " < GetObject Ejecutado");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetObject" + exception);
      System.out.println("RACTDAO.getObject" + exception);
    } 
    return activity;
  }
  
  public static synchronized List<Activity> loadActivitiesFromSQLResult(SQLResult result) {
    Activity activity = null;
    ArrayList<Activity> list = new ArrayList<>();
    try {
      if (result.getSuccess())
        while (result.getResult().next()) {
          activity = new Activity();
          activity.setAcur(result.getResult().getString(1));
          activity.setNume(result.getResult().getString(2));
          activity.setFech(result.getResult().getString(3));
          activity.setAcua(result.getResult().getString(4));
          activity.setActividad(result.getResult().getString(5));
          activity.setCana(new Float(result.getResult().getFloat(6)));
          activity.setAcuo(result.getResult().getString(7));
          activity.setObjeto(result.getResult().getString(8));
          activity.setCano(new Float(result.getResult().getFloat(9)));
          activity.setAcu4(result.getResult().getString(10));
          activity.setDetail(result.getResult().getString(11));
          activity.setCome(result.getResult().getString(12));
          activity.setStam(result.getResult().getString(13));
          String fixed = result.getResult().getString(14);
          activity.setFixed(fixed.equals("T"));
          activity.setType(result.getResult().getString(15));
          for (int i = 0; i < 5; i++) {
            String str = result.getResult().getString(i + 16);
            if (str.equals("T"))
              activity.enableCustomField(i); 
          } 
          String booleanValue = result.getResult().getString(21);
          activity.setBillable(Boolean.valueOf(booleanValue.equals("T")));
          activity.setRegistros(result.getResult().getInt(22));
          list.add(activity);
        }  
    } catch (Exception exception) {
      System.out.println("Error loadActivitiesFromSQLResult" + exception);
    } 
    return list;
  }
  
  public static synchronized List<Activity> getObjects(String resourceCode, String activityCode, SQLManager sqlManager) {
    List<Activity> list = null;
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    list = new ArrayList<>();
    HashMap<String, String> parameters = new HashMap<>();
    parameters.put("ActivityCode", activityCode);
    parameters.put("Resource", resourceCode);
    try {
      logger.log(Level.INFO, " > Ejecutando GetObjects");
      SQLEntry sql = sqlManager.findSQL("GetObjects");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      list = loadActivitiesFromSQLResult(result);
      logger.log(Level.INFO, " < getObjects Ejecutado");
    } catch (Exception exception) {
      System.out.println("Error getObjects" + exception);
      logger.log(Level.SEVERE, " Error Ejecutando getObjects " + exception);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return list;
  }
  
  public static synchronized List<Activity> getActivities(String resourceCode, SQLManager sqlManager) {
    List<Activity> lst = null;
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    HashMap<String, String> parameters = new HashMap<>();
    parameters.put("Resource", resourceCode);
    try {
      logger.log(Level.INFO, " > Ejecutando GetActivities");
      SQLEntry sql = sqlManager.findSQL("GetActivities");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      lst = loadActivitiesFromSQLResult(result);
      logger.log(Level.INFO, " < GetActivity GetActivities");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetActivities" + exception);
      System.out.println("RACTDAO.getActivity" + exception);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return lst;
  }
  
  public static synchronized List<Activity> getDetails(String resourceCode, String activityCode, String objectCode, SQLManager sqlManager) {
    List<Activity> lst = null;
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    HashMap<Object, Object> parameters = new HashMap<>(5);
    parameters.put("Resource", resourceCode);
    parameters.put("ActivityCode", activityCode);
    parameters.put("ObjectCode", objectCode);
    try {
      logger.log(Level.INFO, " > Ejecutando GetDetails");
      SQLEntry sql = sqlManager.findSQL("GetDetails");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      lst = loadActivitiesFromSQLResult(result);
      logger.log(Level.INFO, " < getDetails Ejecutado");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetDetails " + exception);
      System.out.println("RACTDAO.getDetails " + exception);
    } 
    return lst;
  }
  
  public static synchronized Activity getDetail(String resourceCode, String activityCode, String objectCode, String detailCode, SQLManager sqlManager) {
    Activity activity = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    HashMap<Object, Object> parameters = new HashMap<>(5);
    parameters.put("Resource", resourceCode);
    parameters.put("ActivityCode", activityCode);
    parameters.put("ObjectCode", objectCode);
    parameters.put("DetailCode", detailCode);
    try {
      logger.log(Level.INFO, " > Ejecutando GetDetail");
      SQLEntry sql = sqlManager.findSQL("GetDetail");
      activity = getActivityObjectDetail(sqlManager, sql, parameters);
      logger.log(Level.INFO, " < getDetail Ejecutado");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetDetail " + exception);
      System.out.println("RACTDAO.getDetail " + exception);
    } 
    return activity;
  }
  
  public static String FormatPercentage(Float percentage) {
    NumberFormat percentageFormat = DecimalFormat.getPercentInstance();
    return percentageFormat.format((percentage.floatValue() / 100.0F));
  }
  
  public static String FormatHour(Float hour) {
    DecimalFormat hourFormat = new DecimalFormat("0");
    DecimalFormat minFormat = new DecimalFormat("00");
    double hours = Math.floor(hour.doubleValue());
    double minutes = hour.doubleValue() - hours;
    hours += Math.floor(minutes / 60.0D);
    minutes -= Math.floor(minutes / 60.0D);
    return String.valueOf(hourFormat.format(hours)) + 
      ":" + 
      minFormat.format(60.0D * minutes);
  }
  
  public static synchronized WeekDetailTO getResourceWeekActivities(String startDate, String endDate, String usuario, SQLManager sqlManager) {
    WeekDetailTO weekDetail = null;
    SQLResult result = null;
    HashMap<String, String> parameters = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    parameters = new HashMap<>();
    parameters.put("Resource", usuario);
    parameters.put("Date", startDate);
    try {
      logger.log(Level.INFO, " > Ejecutando WeekFixedActivities DAO");
      SQLEntry sql = sqlManager.findSQL("WeekFixedActivities");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      boolean success = result.getSuccess();
      if (success) {
        weekDetail = new WeekDetailTO();
        while (result.getResult().next()) {
          ActivityTO activity = new ActivityTO();
          activity.ACUR = result.getResult().getString("ACUR");
          activity.NUME = result.getResult().getString("NUME");
          activity.ACUA = result.getResult().getString("ACUA");
          activity.ACTIVIDAD = result.getResult().getString("ACTIVIDAD");
          activity.ACUO = result.getResult().getString("ACUO");
          activity.OBJETO = result.getResult().getString("OBJETO");
          activity.ACU4 = result.getResult().getString("ACU4");
          activity.DETAIL = result.getResult().getString("DETAIL");
          activity.FIXED = result.getResult().getString("FIXED");
          activity.TYPE = result.getResult().getString("TYPE");
          activity.CUST1 = result.getResult().getString("CUST1");
          activity.FEDIT1 = result.getResult().getString("FEDIT1");
          activity.CUST2 = result.getResult().getString("CUST2");
          activity.FEDIT2 = result.getResult().getString("FEDIT2");
          activity.CUST3 = result.getResult().getString("CUST3");
          activity.FEDIT3 = result.getResult().getString("FEDIT3");
          activity.CUST4 = result.getResult().getString("CUST4");
          activity.FEDIT4 = result.getResult().getString("FEDIT4");
          activity.CUST5 = result.getResult().getString("CUST5");
          activity.FEDIT5 = result.getResult().getString("FEDIT5");
          activity.FBILL = result.getResult().getString("FBILL");
          activity.REGISTROS = result.getResult().getString("REGISTROS");
          weekDetail.fixed.add(activity);
        } 
      } 
      if (result != null)
        result.close(); 
      logger.log(Level.INFO, " < WeekFixedActivities Ejecutado DAO");
      if (success) {
        parameters = new HashMap<>(5);
        parameters.put("Resource", usuario);
        parameters.put("WeekStartDate", startDate);
        parameters.put("WeekEndDate", endDate);
        logger.log(Level.INFO, " > Ejecutando WeekUnFixActivities");
        sql = sqlManager.findSQL("WeekUnFixActivities");
        result = sql.execute(sqlManager.getDatabase(), parameters);
        if (result.getSuccess())
          while (result.getResult().next()) {
            ActivityTO activity = new ActivityTO();
            activity.ACUR = result.getResult().getString("ACUR");
            activity.NUME = result.getResult().getString("NUME");
            activity.ACUA = result.getResult().getString("ACUA");
            activity.ACTIVIDAD = result.getResult().getString("ACTIVIDAD");
            activity.ACUO = result.getResult().getString("ACUO");
            activity.OBJETO = result.getResult().getString("OBJETO");
            activity.ACU4 = result.getResult().getString("ACU4");
            activity.DETAIL = result.getResult().getString("DETAIL");
            activity.FIXED = result.getResult().getString("FIXED");
            activity.TYPE = result.getResult().getString("TYPE");
            activity.CUST1 = result.getResult().getString("CUST1");
            activity.FEDIT1 = result.getResult().getString("FEDIT1");
            activity.CUST2 = result.getResult().getString("CUST2");
            activity.FEDIT2 = result.getResult().getString("FEDIT2");
            activity.CUST3 = result.getResult().getString("CUST3");
            activity.FEDIT3 = result.getResult().getString("FEDIT3");
            activity.CUST4 = result.getResult().getString("CUST4");
            activity.FEDIT4 = result.getResult().getString("FEDIT4");
            activity.CUST5 = result.getResult().getString("CUST5");
            activity.FEDIT5 = result.getResult().getString("FEDIT5");
            activity.FBILL = result.getResult().getString("FBILL");
            activity.REGISTROS = result.getResult().getString("REGISTROS");
            weekDetail.unFixed.add(activity);
          }  
        if (result != null)
          result.close(); 
        logger.log(Level.INFO, " < WeekUnFixActivities Ejecutado DAO");
        parameters = new HashMap<>(5);
        parameters.put("Resource", usuario);
        parameters.put("WeekStartDate", startDate.toString());
        parameters.put("WeekEndDate", endDate.toString());
        logger.log(Level.INFO, " > Ejecutando WeekDetail DAO");
        sql = sqlManager.findSQL("WeekDetail");
        result = sql.execute(sqlManager.getDatabase(), parameters);
        if (result.getSuccess())
          while (result.getResult().next()) {
            ActivityTO activity = new ActivityTO();
            activity.ACUR = result.getResult().getString("ACUR");
            activity.NUME = result.getResult().getString("NUME");
            activity.FECH = result.getResult().getString("FECH");
            activity.ACUA = result.getResult().getString("ACUA");
            activity.ACTIVIDAD = result.getResult().getString(5);
            activity.CANA = result.getResult().getString("CANA");
            activity.ACUO = result.getResult().getString("ACUO");
            activity.OBJETO = result.getResult().getString(8);
            activity.ACU4 = result.getResult().getString("ACU4");
            activity.DETAIL = result.getResult().getString("DETAIL");
            activity.CANO = result.getResult().getString("CANO");
            activity.COME = result.getResult().getString("COME");
            activity.STAM = result.getResult().getString("STAM");
            activity.FIXED = result.getResult().getString("FIXED");
            activity.TYPE = result.getResult().getString("TYPE");
            activity.CUST1 = result.getResult().getString("CUST1");
            activity.FEDIT1 = result.getResult().getString("FEDIT1");
            activity.CUST2 = result.getResult().getString("CUST2");
            activity.FEDIT2 = result.getResult().getString("FEDIT2");
            activity.CUST3 = result.getResult().getString("CUST3");
            activity.FEDIT3 = result.getResult().getString("FEDIT3");
            activity.CUST4 = result.getResult().getString("CUST4");
            activity.FEDIT4 = result.getResult().getString("FEDIT4");
            activity.CUST5 = result.getResult().getString("CUST5");
            activity.FEDIT5 = result.getResult().getString("FEDIT5");
            activity.FBILL = result.getResult().getString("FBILL");
            activity.REGISTROS = result.getResult().getString("REGISTROS");
            weekDetail.activities.add(activity);
          }  
        if (result != null)
          result.close(); 
        logger.log(Level.INFO, " < WeekDetail Ejecutado DAO");
      } 
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error en metodo getResourceWeekActivities" + e);
      System.out.println(e);
      e.printStackTrace(System.out);
    } finally {
      if (result != null)
        if (result.isOpen())
          result.close();  
    } 
    return weekDetail;
  }
  
  public static String getCurrentDateAndTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date custDate = new Date();
    return sdf.format(custDate);
  }
  
  public static synchronized WeekDetail getResourceWeekActivities(ActivityReportResource resource, DateSC startDate, DateSC endDate, String usuario, SQLManager sqlManager) {
    HashMap<Object, Object> parameters = null;
    SQLResult result = null;
    DailyDetail weekSummary = null;
    DailyDetail detail = null;
    WeekDetail weekDetail = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    logger.log(Level.FINER, "RactDAO.getResourceWeekActivities.startDate: " + startDate
        .toString());
    logger.log(Level.FINER, "RactDAO.getResourceWeekActivities.endDate: " + endDate.toString());
    parameters = new HashMap<>(5);
    parameters.put("Resource", usuario);
    parameters.put("Date", startDate.toString());
    long startTime = System.nanoTime();
    try {
      logger.log(Level.INFO, "**** > getResourceWeekActivities " + usuario + ": " + getCurrentDateAndTime());
      logger.log(Level.INFO, " > Ejecutando WeekFixedActivities" + usuario);
      SQLEntry sql = sqlManager.findSQL("WeekFixedActivities");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      boolean success = result.getSuccess();
      if (success) {
        weekSummary = new DailyDetail(null);
        weekDetail = new WeekDetail();
        while (result.getResult().next()) {
          Activity ractTmp = new Activity();
          ractTmp.setAcur(result.getResult().getString(1));
          ractTmp.setNume(result.getResult().getString(2));
          ractTmp.setAcua(result.getResult().getString(3));
          ractTmp.setActividad(result.getResult().getString(4));
          ractTmp.setAcuo(result.getResult().getString(5));
          ractTmp.setObjeto(result.getResult().getString(6));
          ractTmp.setAcu4(result.getResult().getString(7));
          ractTmp.setDetail(result.getResult().getString(8));
          ractTmp.setCana(new Float(0.0F));
          ractTmp.setCano(new Float(0.0F));
          String fixed = result.getResult().getString(9);
          ractTmp.setFixed(fixed.equals("T"));
          ractTmp.setType(result.getResult().getString(10));
          for (int i = 0, j = 0; i < 5; j += 2, i++) {
            String str1 = result.getResult().getString(j + 11);
            if (result.getResult().wasNull())
              str1 = ""; 
            ractTmp.setCustomFieldValue(i, str1);
            String booleanValue = result.getResult().getString(j + 12);
            if (booleanValue.equals("T"))
              ractTmp.enableCustomField(i); 
          } 
          String customValue = result.getResult().getString(21);
          if (result.getResult().wasNull())
            customValue = "F"; 
          ractTmp.setBillable(Boolean.valueOf(customValue.equals("T")));
          ractTmp.setRegistros(result.getResult().getInt(22));
          weekSummary.add(ractTmp);
        } 
      } 
      if (result != null)
        result.close(); 
      logger.log(Level.INFO, " < WeekFixedActivities Ejecutado " + usuario);
      if (success) {
        parameters = new HashMap<>(5);
        parameters.put("Resource", usuario);
        parameters.put("WeekStartDate", startDate.toString());
        parameters.put("WeekEndDate", endDate.toString());
        logger.log(Level.INFO, " > Ejecutando WeekUnFixActivities " + usuario);
        sql = sqlManager.findSQL("WeekUnFixActivities");
        result = sql.execute(sqlManager.getDatabase(), parameters);
        if (result.getSuccess())
          while (result.getResult().next()) {
            Activity ractTmp = new Activity();
            ractTmp.setAcur(result.getResult().getString(1));
            ractTmp.setNume(result.getResult().getString(2));
            ractTmp.setAcua(result.getResult().getString(3));
            ractTmp.setActividad(result.getResult().getString(4));
            ractTmp.setAcuo(result.getResult().getString(5));
            ractTmp.setObjeto(result.getResult().getString(6));
            ractTmp.setAcu4(result.getResult().getString(7));
            ractTmp.setDetail(result.getResult().getString(8));
            ractTmp.setCana(new Float(0.0F));
            ractTmp.setCano(new Float(0.0F));
            String fixed = result.getResult().getString(9);
            ractTmp.setFixed(fixed.equals("T"));
            ractTmp.setType(result.getResult().getString(10));
            for (int i = 0, j = 0; i < 5; j += 2, i++) {
              String str1 = result.getResult().getString(j + 11);
              if (result.getResult().wasNull())
                str1 = ""; 
              ractTmp.setCustomFieldValue(i, str1);
              String booleanValue = result.getResult().getString(j + 12);
              if (booleanValue.equals("T"))
                ractTmp.enableCustomField(i); 
            } 
            String customValue = result.getResult().getString(21);
            if (result.getResult().wasNull())
              customValue = "F"; 
            ractTmp.setBillable(Boolean.valueOf(customValue.equals("T")));
            ractTmp.setRegistros(result.getResult().getInt(22));
            weekSummary.add(ractTmp);
          }  
        if (result != null)
          result.close(); 
        logger.log(Level.INFO, " < WeekUnFixActivities Ejecutado " + usuario);
        weekDetail.setWeekSummary(weekSummary);
        ActivityRecordCalendar calendar = new ActivityRecordCalendar();
        DateSC date = new DateSC(startDate);
        date.setFormat(startDate.getFormat());
        calendar.setTimeCalendar(date);
        logger.log(Level.FINEST, "RactDAO.getResourceWeekActivities.startDate Format" + startDate
            .getFormat());
        while (date.compareTo((Date)endDate) <= 0) {
          logger.log(Level.FINEST, 
              "RactDAO.getResourceWeekActivities.endDate date" + date);
          logger.log(Level.FINEST, "RactDAO.getResourceWeekActivities.endDate date Format" + date
              .getFormat());
          detail = (DailyDetail)weekSummary.clone();
          weekDetail.addDailyDetail(detail, date);
          detail.setDate(date);
          calendar.add(7, 1);
          date = calendar.getCurrentTime();
          date.setFormat(endDate.getFormat());
        } 
        parameters = new HashMap<>(5);
        parameters.put("Resource", usuario);
        parameters.put("WeekStartDate", startDate.toString());
        parameters.put("WeekEndDate", endDate.toString());
        logger.log(Level.INFO, " > Ejecutando WeekDetail " + usuario);
        sql = sqlManager.findSQL("WeekDetail");
        result = sql.execute(sqlManager.getDatabase(), parameters);
        detail = null;
        if (result.getSuccess())
          while (result.getResult().next()) {
            Activity ractTmp = new Activity();
            ractTmp.setAcur(result.getResult().getString(1));
            ractTmp.setNume(result.getResult().getString(2));
            date = new DateSC(result.getResult().getString(3), startDate.getFormat());
            date.setFormat(endDate.getFormat());
            ractTmp.setFech(date.toString());
            ractTmp.setAcua(result.getResult().getString(4));
            ractTmp.setActividad(result.getResult().getString(5));
            ractTmp.setCana(new Float(result.getResult().getFloat(6)));
            ractTmp.setAcuo(result.getResult().getString(7));
            ractTmp.setObjeto(result.getResult().getString(8));
            ractTmp.setAcu4(result.getResult().getString(9));
            ractTmp.setDetail(result.getResult().getString(10));
            ractTmp.setCano(new Float(result.getResult().getFloat(11)));
            ractTmp.setCome(result.getResult().getString(12));
            ractTmp.setStam(result.getResult().getString(13));
            String fixed = result.getResult().getString(14);
            ractTmp.setFixed(fixed.equals("T"));
            ractTmp.setType(result.getResult().getString(15));
            for (int i = 0, j = 0; i < 5; j += 2, i++) {
              String str1 = result.getResult().getString(j + 16);
              if (result.getResult().wasNull())
                str1 = ""; 
              ractTmp.setCustomFieldValue(i, str1);
              String booleanValue = result.getResult().getString(j + 17);
              if (booleanValue.equals("T"))
                ractTmp.enableCustomField(i); 
            } 
            String customValue = result.getResult().getString(26);
            if (result.getResult().wasNull())
              customValue = "F"; 
            ractTmp.setBillable(Boolean.valueOf(customValue.equals("T")));
            ractTmp.setRegistros(result.getResult().getInt(27));
            detail = weekDetail.getDailyDetail(date);
            detail.set(ractTmp);
            weekDetail.updateSubtotal(ractTmp);
          }  
        if (result != null)
          result.close(); 
        logger.log(Level.INFO, " < WeekDetail Ejecutado " + usuario);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        logger.log(Level.INFO, "getResourceWeekActivities " + usuario + " DURATION:" + (duration / 1000000000L));
        logger.log(Level.INFO, "*****");
        logger.log(Level.INFO, "**** < getResourceWeekActivities " + usuario + " " + getCurrentDateAndTime());
      } 
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error en metodo getResourceWeekActivities" + e);
      System.out.println(e);
      e.printStackTrace(System.out);
    } finally {
      if (result != null)
        if (result.isOpen())
          result.close();  
    } 
    return weekDetail;
  }
  
  public static synchronized int deleteRact(String fecha, String usuario, SQLManager sqlManager) {
    SQLResult result = null;
    int rowsAfected = 0;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      logger.log(Level.INFO, " > Ejecutando DeleteActivities");
      SQLEntry sql = sqlManager.findSQL("DeleteActivities");
      result = sql.execute(sqlManager.getDatabase(), "fecha=" + fecha + ";usuario=" + usuario);
      if (result.getSuccess())
        return 1; 
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error ejecutando DeleteActivities: " + e);
    } finally {
      if (result != null)
        result.close(); 
    } 
    if (result != null)
      result.close(); 
    return rowsAfected;
  }
  
  public static synchronized int deleteRact(Activity registro, DateSC fecha, String usuario, SQLManager sqlManager) {
    SQLResult result = null;
    int rowsAfected = 0;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      logger.log(Level.INFO, " > Ejecutando DeleteRactByNume");
      SQLEntry sql = new SQLEntry();
      sql.setType(1);
      sql.setSqlStatement("DELETE FROM RACT WHERE FECH = '" + fecha.toString() + "' AND ACUR = '" + usuario + "' AND ACUA='" + registro.getAcua() + "' AND ACUO='" + registro.getAcuo() + "' AND ACU4='" + registro.getAcu4() + "'");
      result = sql.execute(sqlManager.getDatabase(), "");
      if (result.getSuccess())
        return 1; 
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error ejecutando DeleteActivities: " + e);
    } finally {
      if (result != null)
        result.close(); 
    } 
    if (result != null)
      result.close(); 
    return rowsAfected;
  }
  
  public static synchronized Activity getActivityBy(Activity registro, DateSC fecha, String usuario, SQLManager sqlManager) {
    Activity activity = null;
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      SQLEntry sql = new SQLEntry();
      sql.setType(0);
      sql.setSqlStatement("SELECT ACUR,NUME,ACUR_ACUM,ACUR_USUA,FECH,ACUA,CANA,ACUO,CANO,ACU4,COME,STAM,CUST1,CUST2,CUST3,CUST4,CUST5,FBILL FROM RACT WHERE FECH = '" + fecha.toString() + "' AND ACUR = '" + usuario + "' AND ACUA='" + registro.getAcua() + "' AND ACUO='" + registro.getAcuo() + "' AND ACU4='" + registro.getAcu4() + "'");
      result = sql.execute(sqlManager.getDatabase(), "");
      if (result.getSuccess())
        if (result.getResult().next()) {
          activity = new Activity();
          activity.setAcur(result.getResult().getString(1));
          activity.setNume(result.getResult().getString(2));
          activity.setAcur_acur(result.getResult().getString(3));
          activity.setAcur_usua(result.getResult().getString(4));
          activity.setFech(fecha.toString());
          activity.setAcua(result.getResult().getString(6));
          activity.setCana(result.getResult().getString(7));
          activity.setAcuo(result.getResult().getString(8));
          activity.setCano(result.getResult().getString(9));
          activity.setAcu4(result.getResult().getString(10));
          activity.setCome(result.getResult().getString(11));
          activity.setCustomFieldValue(0, result.getResult().getString(13));
          activity.setCustomFieldValue(1, result.getResult().getString(14));
          activity.setCustomFieldValue(2, result.getResult().getString(15));
          activity.setCustomFieldValue(3, result.getResult().getString(16));
          activity.setCustomFieldValue(4, result.getResult().getString(17));
        }  
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetObject" + exception);
      System.out.println("RACTDAO.getObject" + exception);
    } 
    return activity;
  }
  
  public static String getNextNume(DateSC fecha, String usuario, SQLManager sqlManager) {
    String nume = null;
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      logger.log(Level.INFO, " > Ejecutando getNextNume");
      SQLEntry sql = new SQLEntry();
      sql.setType(0);
      sql.setSqlStatement("SELECT MAX(NUME) NUME FROM RACT WHERE FECH = '" + fecha.toString() + "' AND ACUR = '" + usuario + "'");
      result = sql.execute(sqlManager.getDatabase(), "");
      if (result.getSuccess())
        if (result.getResult().next()) {
          nume = result.getResult().getString(1);
          if (nume != null) {
            nume = (new BigDecimal(result.getResult().getString(1))).toPlainString();
            nume = String.valueOf(Integer.parseInt(nume) + 1);
          } 
        }  
      logger.log(Level.INFO, " < Ejecutando getNextNume");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetObject" + exception);
      System.out.println("RACTDAO.getObject" + exception);
    } 
    return nume;
  }
  
  public static synchronized int saveAfix(Afix afix, SQLManager sqlManager) {
    SQLResult result = null;
    int rowsAfected = 0;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      SQLEntry sql = sqlManager.findSQL("SaveAfix");
      HashMap<Object, Object> parameters = new HashMap<>(10);
      parameters.put("usuario", afix.getUsuario());
      parameters.put("actividad", afix.getActividad());
      parameters.put("objeto", afix.getObjeto());
      parameters.put("objetoauxiliar", afix.getObjetoAux());
      parameters.put("nume", afix.getNume());
      result = sql.execute(sqlManager.getDatabase(), parameters);
      if (result.getSuccess())
        return 1; 
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error ejecutando SaveAfix : " + e);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return rowsAfected;
  }
  
  public static synchronized int deleteAfix(String usua, SQLManager sqlManager) {
    SQLResult result = null;
    int rowsAfected = 0;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      logger.log(Level.INFO, " > Ejecutando DeleteAfix");
      SQLEntry sql = sqlManager.findSQL("DeleteAfix");
      result = sql.execute(sqlManager.getDatabase(), "usuario=" + usua);
      logger.log(Level.INFO, " < DeleteAfix Ejecutado");
      if (result.getSuccess())
        return 1; 
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error Ejecutando DeleteAfix :" + e);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return rowsAfected;
  }
  
  public static synchronized List UserAOAux(String clase, String usuario, String tipo, SQLManager sqlManager) {
    SQLResult result = null;
    ArrayList<UserAOAux> list = new ArrayList(0);
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      logger.log(Level.INFO, " > Ejecutando UserAOAux");
      SQLEntry sql = sqlManager.findSQL("UserAOAux");
      result = sql.execute(sqlManager.getDatabase(), 
          "clase=" + clase + ";usuario=" + usuario + ";tipo=" + tipo);
      if (result.getSuccess())
        while (result.getResult().next()) {
          UserAOAux act = new UserAOAux();
          act.setCodigo(result.getResult().getString(1));
          act.setNombre(result.getResult().getString(2));
          act.setTipo(result.getResult().getString(3));
          list.add(act);
        }  
      logger.log(Level.INFO, " < Ejecutando UserAOAux");
    } catch (Exception e) {
      logger.log(Level.SEVERE, " > Ejecutando UserAOAux");
    } finally {
      if (result != null)
        result.close(); 
    } 
    return list;
  }
  
  public static synchronized AmountWeeklyHours getAmountWeeklySummary(String usuario, DateSC startDate, int weeks, SQLManager sqlManager) {
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    logger.log(Level.INFO, "**** > getAmountWeeklySummary " + usuario + ": " + getCurrentDateAndTime());
    long startTime = System.nanoTime();
    logger.log(Level.INFO, " > Ejecutando metodo getAmountWeeklySummary " + usuario);
    ActivityRecordCalendar calendar = new ActivityRecordCalendar();
    calendar.setTime((Date)startDate);
    calendar.set(4, 1);
    calendar.set(7, calendar.getFirstDayOfWeek());
    DateSC fecha = calendar.getCurrentTime();
    List dateList = new ArrayList();
    List valueList = new ArrayList();
    for (int i = 0; i < weeks; i++) {
      AmountWeeklyHours amountWeeklyHours = getAmountWeeklyHours(usuario, fecha, sqlManager);
      valueList.addAll(amountWeeklyHours.getCantidad());
      dateList.addAll(amountWeeklyHours.getFecha());
      calendar.goNextWeek();
      fecha = calendar.getCurrentTime();
    } 
    AmountWeeklyHours summary = new AmountWeeklyHours();
    summary.setCantidad(valueList);
    summary.setFecha(dateList);
    logger.log(Level.INFO, " < Metodo getAmountWeeklySummary Ejecutado " + usuario);
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    logger.log(Level.INFO, "getAmountWeeklySummary DURATION:" + (duration / 1000000000L));
    logger.log(Level.INFO, "*****");
    logger.log(Level.INFO, "**** < getAmountWeeklySummary: " + usuario + " " + getCurrentDateAndTime());
    return summary;
  }
  
  public static synchronized AmountWeeklyHours getAmountWeeklyHours(String usuario, DateSC fecha, SQLManager sqlManager) {
    SQLResult result = null;
    int index = 0;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    ActivityRecordCalendar calendar = new ActivityRecordCalendar(fecha);
    String format = "yyyy/MM/dd";
    calendar.setDateFormat(new SimpleDateFormat(format));
    ArrayList<Double> cantidades = new ArrayList(0);
    AmountWeeklyHours horasSemanales = new AmountWeeklyHours();
    List week = calendar.daysOfWeek();
    for (int i = 0; i < 7; i++)
      cantidades.add(new Double(0.0D)); 
    try {
      logger.log(Level.INFO, " > Ejecutando AmountWeeklyHours");
      SQLEntry sql = sqlManager.findSQL("AmountWeeklyHours");
      HashMap<Object, Object> parameters = new HashMap<>();
      parameters.put("usuario", usuario);
      parameters.put("fechinicial", week.get(0).toString());
      parameters.put("fechfinal", week.get(6).toString());
      result = sql.execute(sqlManager.getDatabase(), parameters);
      if (result.getSuccess()) {
        while (result.getResult().next()) {
          DateSC date = new DateSC(result.getResult().getString(1), format);
          date.setFormat(format);
          while (!date.equals(week.get(index)))
            index++; 
          cantidades.set(index, new Double(result.getResult().getDouble(2)));
          index++;
        } 
        logger.log(Level.INFO, " < AmountWeeklyHours Ejecutado");
      } 
      horasSemanales.setFecha(week);
      horasSemanales.setCantidad(cantidades);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error Ejecutando AmountWeeklyHours: " + e);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return horasSemanales;
  }
  
  public static synchronized PercentsPerTimePeriod getPercentsPerTimePeriodSummary(String userName, DateSC startDate, int periods, SQLManager sqlManager, PeriodType periodType) {
    ActivityRecordCalendar calendar = new ActivityRecordCalendar();
    calendar.setTime((Date)startDate);
    int year = calendar.get(1);
    calendar.setTime(new Date(System.currentTimeMillis()));
    int todayYear = calendar.get(1);
    if (year > todayYear + 1) {
      year = year - periods + 1;
    } else if (year > todayYear - periods + 1) {
      year = todayYear - periods + 2;
    } else {
      switch (periodType) {
        case MONTH:
          year = year / 2 * 2;
          break;
        case QUARTER:
          year = year / 4 * 4;
          break;
        case SEMESTER:
          year = year / 4 * 4;
          break;
        case YEAR:
          year = year / 6 * 6;
          break;
        default:
          throw new RuntimeException(
              "Program Error. unexpected periodType in getPercentsPerTimePeriodSummary()");
      } 
    } 
    calendar.setTime((Date)startDate);
    calendar.set(1, year);
    calendar.set(5, 1);
    DateSC date = calendar.getCurrentTime();
    PercentsPerTimePeriod summary = new PercentsPerTimePeriod(periodType);
    for (int i = 0; i < periods; i++) {
      PercentsPerTimePeriod partial = getPercentsPerTimePeriod(userName, date, sqlManager, periodType);
      summary.getWorkPeriods().addAll(partial.getWorkPeriods());
      calendar.add(1, 1);
      date = calendar.getCurrentTime();
    } 
    return summary;
  }
  
  private static synchronized PercentsPerTimePeriod getPercentsPerTimePeriod(String userName, DateSC currentDate, SQLManager sqlManager, PeriodType periodType) {
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.registroactividades");
    ActivityRecordCalendar calendar = new ActivityRecordCalendar(currentDate);
    calendar.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
    PercentsPerTimePeriod workPercents = new PercentsPerTimePeriod(periodType);
    List<DateSC> periods = calendar.datesOfPeriod(PeriodType.YEAR, periodType);
    logger.log(Level.FINE, "GetPercents Current Date 1 is:" + currentDate.toString());
    logger.log(Level.FINE, "GetPercents Period type is:" + periodType.toString());
    for (int i = 0; i < periods.size(); i++) {
      DateSC periodDate = periods.get(i);
      periodDate.setFormat("yyyy/MM/dd");
      workPercents.addWorkPeriod(periodDate);
      logger.log(Level.FINE, String.format(" GetPercents Period[%d] date is : %s", new Object[] { Integer.valueOf(i), periodDate
              .toString() }));
    } 
    try {
      SQLEntry sql = sqlManager.findSQL("AmountMonthlyHours");
      DateSC startDate = new DateSC((Date)periods.get(0));
      DateSC endDate = new DateSC((Date)periods.get(periods.size() - 1));
      logger.log(Level.FINE, "GetPercents periods startDate A:" + startDate.toString());
      logger.log(Level.FINE, "GetPercents periods endDate A:" + startDate.toString());
      startDate.setFormat("yyyy/MM/dd");
      endDate.setFormat("yyyy/MM/dd");
      logger.log(Level.FINE, "GetPercents periods startDate B:" + startDate.toString());
      logger.log(Level.FINE, "GetPercents periods endDate B:" + endDate.toString());
      HashMap<Object, Object> parameters = new HashMap<>();
      parameters.put("usuario", userName);
      parameters.put("fechinicial", startDate.toString());
      parameters.put("fechfinal", endDate.toString());
      result = sql.execute(sqlManager.getDatabase(), parameters);
      if (result.getSuccess())
        while (result.getResult().next()) {
          String firstField = result.getResult().getString(1);
          logger.log(Level.FINE, "GetPercents First field:" + firstField);
          DateSC date = new DateSC(firstField, "yyyy/MM/dd");
          logger.log(Level.FINE, "GetPercents First field (dateSC) A:" + date.toString());
          date.setFormat("dd/MM/yyyy");
          logger.log(Level.FINE, "GetPercents First field (dateSC) B:" + date.toString());
          WorkPerTimePeriod item = null;
          for (int j = 0; j < workPercents.getWorkPeriods().size(); j++) {
            WorkPerTimePeriod period = (WorkPerTimePeriod) workPercents.getWorkPeriods().get(j);
            calendar.setTime(period.getDate());
            calendar.gotoLastDayOfPeriod(periodType);
            if (period.getDate().compareTo((Date)date) <= 0 && calendar.getCurrentTime()
              .compareTo((Date)date) >= 0) {
              item = period;
              break;
            } 
          } 
          if (item == null) {
            System.out.println("DATE not found in RACTDAO for periods: " + date
                .toString());
            logger.log(Level.WARNING, "GetPercents Date not found, it was:" + date
                .toString());
            continue;
          } 
          double percent = result.getResult().getDouble(2);
          logger.log(Level.FINER, "Percent is:" + percent);
          item.accumulateWorkItem(percent, result.getResult().getString(3));
        }  
    } catch (Exception e) {
      logger.log(Level.SEVERE, "GetPercents Exception happened. " + e.getMessage(), e);
      e.printStackTrace();
    } finally {
      if (result != null)
        result.close(); 
    } 
    return workPercents;
  }
  
  public static synchronized ActivityReportResource getResourceByCode(String resourceCode, SQLManager sqlManager) throws GausssoftException {
    ActivityReportResource resource = null;
    SQLResult sqlResult = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    HashMap<Object, Object> parameters = new HashMap<>();
    parameters.put("ResourceCode", resourceCode);
    logger.log(Level.INFO, " > Ejecutando GetResource");
    SQLEntry sql = sqlManager.findSQL("GetResource");
    if (sql == null)
      throw new GausssoftException("SQL GetResource not found"); 
    sqlResult = sql.execute(sqlManager.getDatabase(), parameters);
    if (sqlResult.getSuccess())
      try {
        if (sqlResult.getResult().next()) {
          PeriodType periodType;
          resource = new ActivityReportResource(sqlResult.getResult().getString(1), 
              sqlResult.getResult().getString(2));
          resource.setPositionName(sqlResult.getResult().getString(3));
          resource.setUserName(sqlResult.getResult().getString(4));
          resource.setPosition(sqlResult.getResult().getString(5).equals("T"));
          try {
            periodType = PeriodType.getPeriodType(
                Character.valueOf(sqlResult.getResult().getString(6).charAt(0)));
          } catch (Exception e) {
            periodType = PeriodType.DAY;
          } 
          resource.setView(periodType);
        } 
        logger.log(Level.INFO, " > GetResource Ejecutado");
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Error ejecutando GetResource: " + e);
        System.out.println("Error excuting getResource ByCode " + e);
      } finally {
        if (sqlResult != null)
          sqlResult.close(); 
      }  
    return resource;
  }
  
  public static synchronized List getResources(String condition, SQLManager sqlManager) {
    ArrayList<ActivityChoiceTO> list = null;
    SQLResult result = null;
    list = new ArrayList(50);
    HashMap<Object, Object> parameters = new HashMap<>(5);
    parameters.put("Condition", condition);
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    long startTime = System.nanoTime();
    try {
      logger.log(Level.INFO, "**** > GetResources: " + getCurrentDateAndTime());
      logger.log(Level.INFO, " > Ejecutando GetResources");
      SQLEntry sql = sqlManager.findSQL("GetResources");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      if (result.getSuccess())
        while (result.getResult().next()) {
          ActivityChoiceTO choice = new ActivityChoiceTO();
          choice.codigo = result.getResult().getString(1);
          choice.nombre = result.getResult().getString(2);
          choice.tipo = result.getResult().getString(3);
          choice.unidad = result.getResult().getString(4);
          list.add(choice);
        }  
      logger.log(Level.INFO, " < GetResources Ejecutado");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, "Error ejecutando GetResources: " + exception);
      System.out.println("Error getResources" + exception);
    } finally {
      if (result != null)
        result.close(); 
    } 
    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    logger.log(Level.INFO, "GetResources DURATION:" + (duration / 1000000000L));
    logger.log(Level.INFO, "*****");
    logger.log(Level.INFO, "**** < GetResources: " + getCurrentDateAndTime());
    return list;
  }
  
  public static HashMap getRactParameters(SQLManager sqlManager) throws GausssoftException {
    SQLResult sqlResult = null;
    HashMap<Object, Object> parameters = new HashMap<>();
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    logger.log(Level.INFO, " > Ejecutando GetRactParameters");
    SQLEntry sql = sqlManager.findSQL("GetRactParameters");
    if (sql == null)
      throw new GausssoftException("SQL GetRactParameters was not found"); 
    sqlResult = sql.execute(sqlManager.getDatabase(), parameters);
    try {
      if (sqlResult.getSuccess()) {
        ResultSet resultSet = sqlResult.getResult();
        while (resultSet.next()) {
          String parameterName = resultSet.getString(1);
          String parameterValue = resultSet.getString(2);
          parameters.put(parameterName, parameterValue);
        } 
      } 
      logger.log(Level.INFO, " < GetRactParameters Ejecutado");
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error ejecutando GetParameters: " + e);
      throw new GausssoftException(e);
    } finally {
      if (sqlResult != null)
        sqlResult.close(); 
    } 
    return parameters;
  }
  
  public static synchronized ProjectDAO getProyect(String proyectCode, String usuario, SQLManager sqlManager, String orderBy) {
    ProjectDAO project = null;
    SQLResult result = null;
    if (orderBy == null)
      orderBy = "A.ACTIVIDAD"; 
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      logger.log(Level.INFO, " > Ejecutando getProyect");
      SQLEntry sql = new SQLEntry();
      sql.setType(0);
      sql.setSqlStatement("SELECT DISTINCT A.PROYECTO, A.NOMBRE_PROYECTO ,A.ACTIVIDAD , A.NOMBRE_ACTIVIDAD, A.UND_MED, CASE WHEN B.COSTO IS NULL THEN 0 ELSE B.COSTO END COSTO,CASE WHEN A.QTTY IS NULL THEN 0 WHEN A.QTTY='' AND (B.COSTO<>0 AND A.CONTRATADO>1) THEN (A.CONTRATADO/B.COSTO) WHEN TRY_CONVERT(FLOAT, A.QTTY)<1 THEN 0 ELSE A.QTTY END QTTY,CASE WHEN A.CONTRATADO IS NULL THEN 0 \t  WHEN A.CONTRATADO<1 THEN 0 ELSE A.CONTRATADO END CONTRATADO,A.COPYRIGHT, A.AGRUP_PROY, A.DEPARTAMENTO FROM ( SELECT PROJECT.CODIGO PROYECTO, PROJECT.NOMBRE NOMBRE_PROYECTO,OBJECT.ACUM ACTIVIDAD, ACUM.NOMBRE NOMBRE_ACTIVIDAD, ACUM.VALOR2 UND_MED , M.CONTRATADO, M.QTTY, PROJECT.VALOR COPYRIGHT, PROJECT.VALOR2 AGRUP_PROY, ACUM.VALOR DEPARTAMENTO FROM GASI RES INNER JOIN GASI ACTIVITY ON  ACTIVITY.CLAS = 'A' AND ACTIVITY.GDEF = RES.GDEF INNER JOIN GASI OBJECT ON OBJECT.GDEF = ACTIVITY.GDEF AND OBJECT.CLAS = 'O' INNER JOIN ACUM ON ACUM.CODIGO = OBJECT.ACUM INNER JOIN ACUM PROJECT ON PROJECT.CODIGO=ACTIVITY.ACUM LEFT JOIN TMAESTRO_CONTRATADOS M ON M.ACTIVIDAD=OBJECT.ACUM AND ACTIVITY.ACUM=M.PROYECTO WHERE RES.ACUM = '" + 
          
          usuario + "' AND " + 
          "RES.CLAS = 'R' " + 
          "AND ACTIVITY.ACUM = '" + proyectCode + "')A " + 
          "INNER JOIN " + 
          "(SELECT PROJECT.CODIGO PROYECTO, OBJECT.ACUM ACTIVIDAD, ACUM.NOMBRE NOMBRE_ACTIVIDAD, ACUM.VALOR2 UND_MED, M.COSTO, M.QTTY" + 
          ",PROJECT.VALOR COPYRIGHT, PROJECT.VALOR2 AGRUP_PROY, ACUM.VALOR DEPARTAMENTO," + 
          "OBJECT.ACUM COMPONENTE, M.FECHA_COMP, M.ANIO, M.MES, M.DIA" + 
          " FROM " + 
          "GASI RES " + 
          "INNER JOIN " + 
          "GASI ACTIVITY " + 
          "ON  ACTIVITY.CLAS = 'A' AND ACTIVITY.GDEF = RES.GDEF " + 
          "INNER JOIN GASI OBJECT " + 
          "ON OBJECT.GDEF = ACTIVITY.GDEF AND OBJECT.CLAS = 'O' " + 
          "INNER JOIN ACUM " + 
          "ON ACUM.CODIGO = OBJECT.ACUM " + 
          "INNER JOIN ACUM PROJECT " + 
          "ON PROJECT.CODIGO=ACTIVITY.ACUM " + 
          "LEFT JOIN TMAESTRO_COSTO M " + 
          "ON M.ACTIVIDAD=OBJECT.ACUM " + 
          "AND ACTIVITY.ACUM=M.PROYECTO " + 
          "WHERE RES.ACUM = '" + usuario + "' AND RES.CLAS = 'R' " + 
          "AND ACTIVITY.ACUM = '" + proyectCode + "')B " + 
          "ON A.PROYECTO=B.PROYECTO " + 
          "AND  A.ACTIVIDAD=B.ACTIVIDAD " + 
          "AND A.COPYRIGHT=B.COPYRIGHT " + 
          "AND A.AGRUP_PROY=B.AGRUP_PROY " + 
          "AND A.DEPARTAMENTO=B.DEPARTAMENTO " + 
          "ORDER BY " + orderBy);
      result = sql.execute(sqlManager.getDatabase(), "");
      if (result.getSuccess()) {
        project = new ProjectDAO();
        while (result.getResult().next()) {
          project.proyecto = result.getResult().getString("PROYECTO");
          project.nombreProyecto = result.getResult().getString("NOMBRE_PROYECTO");
          ProyectActivityDAO activity = new ProyectActivityDAO();
          activity.actividad = result.getResult().getString("ACTIVIDAD");
          activity.nombreActividad = result.getResult().getString("NOMBRE_ACTIVIDAD");
          activity.medida = result.getResult().getString("UND_MED");
          activity.costo = Float.valueOf((result.getResult().getString("COSTO") != null) ? result.getResult().getString("COSTO") : "0").floatValue();
          activity.qtty = Float.valueOf((result.getResult().getString("QTTY") != null) ? result.getResult().getString("QTTY") : "0").floatValue();
          activity.contratado = Float.valueOf((result.getResult().getString("CONTRATADO") != null) ? result.getResult().getString("CONTRATADO") : "0").floatValue();
          activity.copyright = result.getResult().getString("COPYRIGHT");
          activity.agrup_proy = result.getResult().getString("AGRUP_PROY");
          activity.departamento = result.getResult().getString("DEPARTAMENTO");
          project.addActivity(activity);
        } 
      } 
      logger.log(Level.INFO, " < Ejecutado getProyect");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetObject" + exception);
      System.out.println("RACTDAO.getObject" + exception);
    } 
    return project;
  }
  
  public static synchronized boolean updateProject(ProjectDAO project, SQLManager sqlManager, String user, boolean execSP) {
    SQLResult result = null;
    boolean ok = true;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      long startTime = System.nanoTime();
      logger.log(Level.INFO, " > Ejecutando updateProyect");
      SQLEntry sql = new SQLEntry();
      sql.setType(1);
      sqlManager.getDatabase().getConnection().getConnection().setAutoCommit(false);
      sql.setSqlStatement("DELETE FROM TMAESTRO_CONTRATADOS WHERE PROYECTO='" + project.proyecto + "'");
      result = sql.execute(sqlManager.getDatabase(), "");
      if (result.getSuccess()) {
        sql.setSqlStatement("DELETE FROM TMAESTRO_COSTO WHERE PROYECTO='" + project.proyecto + "'");
        result = sql.execute(sqlManager.getDatabase(), "");
        if (result.getSuccess()) {
          Calendar now = Calendar.getInstance();
          String date = String.valueOf(now.get(1)) + "/" + (now.get(2) + 1) + "/" + now.get(5);
          String year = String.valueOf(now.get(1));
          String month = String.valueOf(now.get(2) + 1);
          String day = String.valueOf(now.get(5));
          sql.setType(3);
          for (ProyectActivityDAO activity : project.actividades) {
            sql.setSqlStatement("INSERT INTO TMAESTRO_CONTRATADOS(USUARIO,FECHA_COMP,ANIO,MES,DIA,COPYRIGHT,AGRUP_PROY,PROYECTO,DEPARTAMENTO,UND_MED,ACTIVIDAD,HORAS,QTTY,CONTRATADO) VALUES('" + 
                
                user + "','" + date + "', '" + year + "', '" + month + "', '" + day + "', '" + activity.copyright + "','" + activity.agrup_proy + "','" + 
                project.proyecto + "','" + activity.departamento + "','" + activity.medida + "','" + activity.actividad + "',0.016666668," + 
                activity.qtty + "," + activity.contratado + ")");
            result = sql.execute(sqlManager.getDatabase(), "");
            if (!result.getSuccess()) {
              ok = false;
              sqlManager.getDatabase().getConnection().getConnection().rollback();
              break;
            } 
            sql.setSqlStatement("INSERT INTO TMAESTRO_COSTO(USUARIO,FECHA_COMP,ANIO,MES,DIA,COPYRIGHT,AGRUP_PROY,PROYECTO,COMPONENTE,DEPARTAMENTO,UND_MED,ACTIVIDAD,HORAS,QTTY,COSTO)VALUES('" + 
                
                user + "','" + date + "', '" + year + "', '" + month + "', '" + day + "', '" + activity.copyright + "','" + activity.agrup_proy + "','" + 
                project.proyecto + "','" + activity.actividad + "', '" + activity.departamento + "','" + activity.medida + "','" + activity.actividad + "',0.016666668," + 
                activity.qtty + "," + activity.costo + ")");
            result = sql.execute(sqlManager.getDatabase(), "");
            if (!result.getSuccess()) {
              ok = false;
              sqlManager.getDatabase().getConnection().getConnection().rollback();
              break;
            } 
          } 
        } else {
          ok = false;
          sqlManager.getDatabase().getConnection().getConnection().rollback();
        } 
      } else {
        ok = false;
        sqlManager.getDatabase().getConnection().getConnection().rollback();
      } 
      if (execSP) {
        logger.log(Level.INFO, " > Ejecutando sp ALTAVISTA");
        sql.setType(4);
        sql.setSqlStatement("EXEC('ALTAVISTA')");
        result = sql.execute(sqlManager.getDatabase(), "");
        if (!result.getSuccess()) {
          ok = false;
          sqlManager.getDatabase().getConnection().getConnection().rollback();
        } 
        logger.log(Level.INFO, " < Ejecutado sp ALTAVISTA");
      } 
      if (ok)
        sqlManager.getDatabase().getConnection().getConnection().commit(); 
      sqlManager.getDatabase().getConnection().getConnection().setAutoCommit(true);
      long endTime = System.nanoTime();
      long duration = endTime - startTime;
      System.out.println("updateProyect DURATION:" + (duration / 1000000000L));
      System.out.println("*****");
      logger.log(Level.INFO, " < Ejecutado updateProyect");
    } catch (Exception exception) {
      try {
        sqlManager.getDatabase().getConnection().getConnection().setAutoCommit(true);
        sqlManager.getDatabase().getConnection().getConnection().rollback();
      } catch (DatabaseManagementExceptionSC e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
      logger.log(Level.SEVERE, " Error Ejecutando GetObject" + exception);
      System.out.println("RACTDAO.getObject" + exception);
    } 
    return ok;
  }
  
  public static boolean execSP(SQLManager sqlManager) {
    SQLResult result = null;
    boolean ok = true;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      long startTime = System.nanoTime();
      SQLEntry sql = new SQLEntry();
      sqlManager.getDatabase().getConnection().getConnection().setAutoCommit(false);
      logger.log(Level.INFO, " > Ejecutando sp ALTAVISTA");
      sql.setType(4);
      sql.setSqlStatement("EXEC('ALTAVISTA')");
      result = sql.execute(sqlManager.getDatabase(), "");
      if (!result.getSuccess()) {
        ok = false;
        sqlManager.getDatabase().getConnection().getConnection().rollback();
      } 
      long endTime = System.nanoTime();
      long duration = endTime - startTime;
      System.out.println("sp ALTAVISTA DURATION:" + (duration / 1000000000L));
      System.out.println("*****");
      logger.log(Level.INFO, " < Ejecutado sp ALTAVISTA");
      if (ok)
        sqlManager.getDatabase().getConnection().getConnection().commit(); 
      sqlManager.getDatabase().getConnection().getConnection().setAutoCommit(true);
    } catch (Exception exception) {
      try {
        sqlManager.getDatabase().getConnection().getConnection().setAutoCommit(true);
        sqlManager.getDatabase().getConnection().getConnection().rollback();
      } catch (DatabaseManagementExceptionSC e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      } 
      logger.log(Level.SEVERE, " Error Ejecutando execSP" + exception);
      System.out.println("RACTDAO.execSP" + exception);
    } 
    return ok;
  }
  
  public static boolean copyProyectPriceActivities(CopyProjectDAO copy, String usuario, SQLManager sqlManager) {
    boolean resp = true;
    for (String proyectoCode : copy.proyectosDestino) {
      ProjectDAO project = getProyect(proyectoCode, usuario, sqlManager, "A.ACTIVIDAD");
      for (ProyectActivityDAO activity : copy.actividades) {
        ProyectActivityDAO currentAct = project.getActivity(activity.actividad);
        if (currentAct != null) {
          currentAct.costo = activity.costo;
          currentAct.contratado = currentAct.qtty * currentAct.costo;
        } 
      } 
      resp = (resp && updateProject(project, sqlManager, usuario, false));
    } 
    return resp;
  }
  
  public static synchronized List<ActivityTO> getSummary(HashMap<String, String> parameters, SQLManager sqlManager) {
    List<ActivityTO> lst = null;
    SQLResult result = null;
    Logger logger = Logger.getLogger("siscorp.activityrecord.data");
    try {
      long startTime = System.nanoTime();
      logger.log(Level.INFO, " > Ejecutando GetSummary");
      SQLEntry sql = sqlManager.findSQL("Summary");
      result = sql.execute(sqlManager.getDatabase(), parameters);
      lst = new ArrayList<>();
      if (result.getSuccess())
        while (result.getResult().next()) {
          ActivityTO activity = new ActivityTO();
          activity.ACTIVIDAD = result.getResult().getString("ACTIVIDAD");
          activity.OBJETO = result.getResult().getString("OBJETO");
          activity.DETAIL = result.getResult().getString("DETAIL");
          activity.CANA = result.getResult().getString("TOTAL");
          lst.add(activity);
        }  
      long endTime = System.nanoTime();
      long duration = endTime - startTime;
      System.out.println("GetSummary DURATION:" + (duration / 1000000000L));
      logger.log(Level.INFO, " < GetSummary");
    } catch (Exception exception) {
      logger.log(Level.SEVERE, " Error Ejecutando GetActivities" + exception);
      System.out.println("RACTDAO.getActivity" + exception);
    } finally {
      if (result != null)
        result.close(); 
    } 
    return lst;
  }
}
