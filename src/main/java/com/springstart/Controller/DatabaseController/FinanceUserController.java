package com.springstart.Controller.DatabaseController;

import com.springstart.Controller.InventoryController;
import com.springstart.Model.Entity.DatabaseEntity.FinanceUser;
import com.springstart.Service.DatabaseService.FinanceUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/financeuser")
public class FinanceUserController {

  private static final Logger logger = LoggerFactory.getLogger(FinanceUserController.class);
  FinanceUserService financeUserService;

  @RequestMapping(path = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
  public void createUser(@RequestBody FinanceUser financeUser){
    logger.info("FinanceUser: " + financeUser);
    financeUserService.createFinanceUser(financeUser);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<FinanceUser> getAll(){
    return financeUserService.getAllFinanceUsers();
  }


  public FinanceUserService getFinanceUserService() {
    return financeUserService;
  }

  @Autowired
  public void setFinanceUserService(FinanceUserService financeUserService) {
    this.financeUserService = financeUserService;
  }
}
