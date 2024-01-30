package activityrecord.ract;

import activityrecord.ract.MessageUserTO;
import com.gausssoft.services.TransferObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageUser implements TransferObject<MessageUserTO> {
  private int id;
  
  private String licenseCode;
  
  private String subject;
  
  private String newmessage;
  
  private Date date;
  
  private boolean read;
  
  public MessageUser(String licensecCode, String subject, String newmessage) {
    this.licenseCode = licensecCode;
    this.newmessage = newmessage;
    this.date = new Date();
    this.read = false;
    setSubject(subject);
  }
  
  public MessageUser() {
    this.date = new Date();
    this.read = false;
    this.id = -1;
  }
  
  public String getLicenseCode() {
    return this.licenseCode;
  }
  
  public void setLicenseCode(String licenseCode) {
    this.licenseCode = licenseCode;
  }
  
  public String getNewmessage() {
    return this.newmessage;
  }
  
  public void setNewmessage(String newmessage) {
    this.newmessage = newmessage;
  }
  
  public Date getDate() {
    return this.date;
  }
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setDate(Date date) {
    this.date = date;
  }
  
  public boolean isRead() {
    return this.read;
  }
  
  public void setRead(boolean read) {
    this.read = read;
  }
  
  public MessageUserTO createTransferObject() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    MessageUserTO messageTO = new MessageUserTO();
    messageTO.id = getId();
    messageTO.licenseCode = getLicenseCode();
    messageTO.message = getNewmessage();
    messageTO.read = isRead();
    messageTO.date = sdf.format(getDate());
    messageTO.subject = getSubject();
    return messageTO;
  }
  
  public MessageUserTO createTransferObject(boolean deep) {
    return createTransferObject();
  }
  
  public void loadFromTransferObject(MessageUserTO transferObject) {}
  
  public String getSubject() {
    return this.subject;
  }
  
  public void setSubject(String subject) {
    this.subject = subject;
  }
}
