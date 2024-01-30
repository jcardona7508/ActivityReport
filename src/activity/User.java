package activity;

import com.gausssoft.client.UserTO;
import com.gausssoft.crypto.DESCipher;
import com.gausssoft.services.TransferObject;

public class User implements TransferObject<UserTO> {
  private String name;
  
  private String usercode;
  
  private String password;
  
  private String profile;
  
  public User(String name, String usercode, String password, String profile) {
    setName(name);
    setUsercode(usercode);
    setPassword(password);
    setProfile(profile);
  }
  
  public User() {}
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name.toUpperCase();
  }
  
  public String getUsercode() {
    return this.usercode;
  }
  
  public void setUsercode(String usercode) {
    this.usercode = usercode.toUpperCase();
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public void setPassword(String password) {
    DESCipher desCipher = new DESCipher();
    this.password = desCipher.isEncrypted(password) ? password : desCipher.encrypt(password);
  }
  
  public String getProfile() {
    return this.profile;
  }
  
  public void setProfile(String profile) {
    this.profile = profile;
  }
  
  public UserTO createTransferObject() {
    UserTO userTO = new UserTO();
    userTO.name = getName();
    userTO.usercode = getUsercode();
    userTO.password = getPassword();
    userTO.profile = getProfile();
    return userTO;
  }
  
  public UserTO createTransferObject(boolean deep) {
    return createTransferObject();
  }
  
  public void loadFromTransferObject(UserTO transferObject) {
    setName(transferObject.name);
    setUsercode(transferObject.usercode);
    setPassword(transferObject.password);
    setProfile(transferObject.profile);
  }
}
