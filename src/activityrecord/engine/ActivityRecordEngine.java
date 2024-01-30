package activityrecord.engine;

import com.gausssoft.GausssoftException;
import common.engine.Engine;
import common.sql.SQLManager;
import common.sql.SQLManagerFactory;
import java.util.Properties;
import siscorp.dbmanagement.DatabaseSC;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.ApplicationMap;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.model.ModelAction;
import siscorp.framework.application.model.ModelProcessor;

public class ActivityRecordEngine extends Engine {
  protected SQLManager sqlManager;
  
  public void init(Properties properties) throws GausssoftException {
    super.init(properties);
    initSqlManager();
  }
  
  public void initSqlManager() throws GausssoftException {
    DatabaseSC db = this.componentManager.getDatabase("activityrecord");
    Properties sqlManagerProperties = (Properties)getContext().getAtribute("propertiesApp");
    if (sqlManagerProperties != null) {
      SQLManagerFactory sqlManagerFactory = new SQLManagerFactory(sqlManagerProperties);
      SQLManager localSqlManager = sqlManagerFactory.getSQLManager();
      localSqlManager.setDatabase(db);
      this.componentManager.addComponent(String.valueOf(this.componentManager.getNameSpace()) + ".sqleditor", db.getAlias(), localSqlManager);
    } 
  }
  
  public EventResponse doProcess(ApplicationEvent event) throws GausssoftException {
    ModelAction command = null;
    ApplicationEvent currentEvent = event;
    EventResponse response = null;
    while (currentEvent != null) {
      command = getCommandInstance(currentEvent);
      if (command != null) {
        command.doStart(currentEvent);
        response = command.execute(currentEvent);
      } 
      command.doEnd(currentEvent, response);
      currentEvent = response.getNextEvent();
    } 
    return response;
  }
  
  protected synchronized ModelAction getCommandInstance(ApplicationEvent event) throws GausssoftException {
    ApplicationMap mapping = null;
    ModelAction command = null;
    if (!this.cache.containsObject("common.model.action", event.getName())) {
      mapping = getMappings().getApplicationMap(event.getName());
      if (mapping != null) {
        command = (ModelAction)mapping.getInstance();
      } else {
        throw new GausssoftException("Command mapping not found");
      } 
      command.init((ModelProcessor)this);
      this.cache.addObject("common.model.action", event.getName(), command);
    } 
    command = (ModelAction)this.cache.getObject("common.model.action", event.getName());
    return command;
  }
}
