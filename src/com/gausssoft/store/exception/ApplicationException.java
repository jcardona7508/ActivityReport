package com.gausssoft.store.exception;

import com.gausssoft.services.ServiceException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ApplicationException extends ServiceException {
  private static final long serialVersionUID = 1L;
  
  private static String PROJECT_MESSAGES_RESOURCE = "com.gausssoft.store.exception.ApplicationExceptionMessages";
  
  public ApplicationException(String id) {
    super(id);
  }
  
  public ApplicationException(String id, Throwable exception, Object[] arguments) {
    super(id, exception, arguments);
  }
  
  public ApplicationException(String id, Throwable exception) {
    super(id, exception);
  }
  
  protected ResourceBundle getMessagesResource() {
    Locale locale = Locale.getDefault();
    return ResourceBundle.getBundle(PROJECT_MESSAGES_RESOURCE, locale);
  }
}
