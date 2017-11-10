package com.springstart.Controller;

import com.springstart.Model.Entity.Student;
import com.springstart.Model.sessionbean.SessionBean;
import org.apache.zookeeper.server.SessionTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
public class HomeController implements ApplicationContextAware{

  private final Logger logger = LoggerFactory.getLogger(HomeController.class);
  @Autowired
  private SessionBean sessionBean;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home(Model model) {

    sessionBean.printSomething();
      logger.info("HomeController - home()");

      model.addAttribute("message", "Hello Spring MVC Framework!");
      return "WEB-INF/view/index";
  }

  @RequestMapping(value = "/session", method = RequestMethod.GET)
  public String session(Model model, HttpServletRequest request,
                     HttpServletResponse response) {

    //   sessionBean.printSomething();
/*    logger.info("HomeController - session()");
    HttpSession session =request.getSession();
    if(request.getSession(false) == null){
      session = request.getSession();
    }*/

    //model.addAttribute("message", "Session : " +  session.getId());

    return "WEB-INF/view/index";
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    //sessionBean = applicationContext.getBean("sessionBean", SessionBean.class);
  }
}
