package com.gausssoft.commands;

import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class UploadLogo implements ICommand {
  private String name;
  
  public UploadLogo() {
    setName("SaveProyectPriceActivities");
  }
  
  public IResponseContext execute(IRequestContext request) {
    ResponseContext result = new ResponseContext();
    try {
      File logoUploaded = new File(request.getFile("defaultLogo").getPath());
      File gausssoftlogo = new File(logoUploaded.getParentFile(), "gausssoftlogo.png");
      FileUtils.copyFile(logoUploaded, gausssoftlogo, false);
      result.setResultCode(ResultCode.SUCCESS);
    } catch (Exception e) {
      result.setValue("message", e.getMessage());
      result.setExceptionMessage(e.getMessage());
      result.setResultCode(ResultCode.EXCEPTION);
    } 
    return (IResponseContext)result;
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
