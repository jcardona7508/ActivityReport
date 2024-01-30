package activityrecord.ract;

import java.io.Serializable;

public class MessageUserTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public String message;
  
  public int id;
  
  public boolean read;
  
  public String date;
  
  public String licenseCode;
  
  public String subject;
  
  public String toString() {
    return "MessageUserTO [licenseCode=" + this.licenseCode + ", message=" + this.message + ", id=" + 
      this.id + ", read=" + this.read + ", date=" + this.date + ", licenseCode=" + this.licenseCode + 
      ", subject=" + this.subject + "]";
  }
}
