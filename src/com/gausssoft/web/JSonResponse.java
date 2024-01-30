package com.gausssoft.web;

import java.io.Serializable;

public class JSonResponse implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public String status = null;
  
  public Object message;
  
  public Object response = null;
}
