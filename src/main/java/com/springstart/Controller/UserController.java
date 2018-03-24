package com.springstart.Controller;

import com.springstart.Model.Entity.User;
import com.springstart.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    //-------------- GET ------------------------
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<User[]> getAllUsers() {
        logger.info("GET all users");
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        logger.info("GET  user by id: " + id);
        User user = userService.getUserById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<User> responseEntity = new ResponseEntity<User>(user, headers, HttpStatus.OK);
        return responseEntity;
    }

    //-------------- POST ------------------------
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> createUser(@RequestBody User user) {

        if (userService.exists(user)) {
            return new ResponseEntity<User>(HttpStatus.CONFLICT);
        }

        System.out.println("NOTHING HAPPEN");
        User retrievedUser = userService.createUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<User> responseEntity = new ResponseEntity<User>(retrievedUser, headers, HttpStatus.CREATED);

        return responseEntity;
    }


    //-------------- GET / SET ------------------------

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }



}
