package com.springstart.Listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionCreatedEvent;

public class SessionListener implements ApplicationListener<HttpSessionCreatedEvent> {

  private final Logger logger = LoggerFactory.getLogger(SessionListener.class);

  public void onApplicationEvent(HttpSessionCreatedEvent httpSessionCreatedEvent) {
        logger.info("SessionListener - Session has been created: " + httpSessionCreatedEvent.getSession().getId() );
  }

}
