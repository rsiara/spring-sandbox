package com.springstart.Model.sessionbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionBean {
  private final Logger logger = LoggerFactory.getLogger(SessionBean.class);

  public SessionBean() {
    logger.info("SessionBean has been created");
  }

  public void printSomething(){
    logger.info("SessionBean - printSomethind()");
  }
}
