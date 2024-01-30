package com.gausssoft.client;

import java.io.Serializable;
import java.util.List;

public class UserTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public String name;
  
  public String usercode;
  
  public String password;
  
  public String profile;
  
  public List<String> accessList;
  
  public String toString() {
    return "UserTO [ name=" + this.name + ", usercode=" + this.usercode + " password=" + this.password + ", profile=" + this.profile + "]";
  }
}
