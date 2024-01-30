package com.gausssoft.client;

import java.io.Serializable;
import java.util.HashMap;

public class MessageTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public String server;
  
  public String client;
  
  public HashMap<String, String> params;
  
  public MessageTO(String server, String client, HashMap<String, String> params) {
    this.server = server;
    this.client = client;
    this.params = params;
  }
  
  public MessageTO() {
    this.params = new HashMap<>();
  }
}
