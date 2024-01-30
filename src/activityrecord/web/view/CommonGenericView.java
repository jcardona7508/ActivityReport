package activityrecord.web.view;

import common.web.view.GenericHtmlView;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;

public class CommonGenericView extends GenericHtmlView {
  public static final int UNPROCESSED = -1;
  
  public static final int SUCCESS = 0;
  
  public static final int CONTINUE = 1;
  
  public static final int FORWARD = 2;
  
  public static final int REDIRECT = 3;
  
  public static final int FAILED = 100;
  
  public static final int EXCEPTION = 200;
  
  private int[] acceptedEvents;
  
  private String attributeName;
  
  private String attributeValue;
  
  public CommonGenericView(String attributeName, String attributeValue, Integer... acceptedEvents) {
    this.acceptedEvents = new int[acceptedEvents.length];
    for (int i = 0; i < acceptedEvents.length; i++)
      this.acceptedEvents[i] = acceptedEvents[i].intValue(); 
    this.attributeName = attributeName;
    this.attributeValue = attributeValue;
  }
  
  public void render(ApplicationEvent event, EventResponse response) throws Exception {
    byte b;
    int i;
    int[] arrayOfInt;
    for (i = (arrayOfInt = this.acceptedEvents).length, b = 0; b < i; ) {
      int item = arrayOfInt[b];
      if (response.getResultCode() == item) {
        response.setAttribute(this.attributeName, this.attributeValue);
        break;
      } 
      b++;
    } 
    super.render(event, response);
  }
}
