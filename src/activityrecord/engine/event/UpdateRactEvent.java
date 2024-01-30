package activityrecord.engine.event;

import common.engine.event.GenericEvent;
import siscorp.framework.application.ApplicationRequest;
import siscorp.framework.application.Context;

public class UpdateRactEvent extends GenericEvent {
  public static final int RETRIEVE_RACT = 0;
  
  public static final int RETRIEVE_MENU = 2;
  
  public UpdateRactEvent(ApplicationRequest request) {
    super("updateRactEvent", request);
  }
  
  public UpdateRactEvent(ApplicationRequest request, Context context) {
    super("updateRactEvent", request, context);
  }
}
