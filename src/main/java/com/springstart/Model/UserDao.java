package com.springstart.Model;

import com.springstart.Model.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Repository
public class UserDao {

    public static final String REST_SERVICE_URI = "https://jsonplaceholder.typicode.com/";

    private RestTemplate restTemplate;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /* GET */
    @SuppressWarnings("unchecked")
    public ResponseEntity<User[]> getAllUsers() {
        System.out.println("Testing getAllUsers API-----------");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User[]> users = restTemplate.getForEntity(REST_SERVICE_URI + "/users/", User[].class);

        for (User user : users.getBody()) {
            System.out.println(user);
        }

        return users;
    }

    /* GET */
    @SuppressWarnings("unchecked")
    public User getUserById(int id) {
        System.out.println("Testing get user by id API-----------");

        ResponseEntity<User> userResponseEntity = restTemplate.getForEntity(REST_SERVICE_URI + "/users/" + id, User.class);

        System.out.println("Retrieved object: " + userResponseEntity.getBody());

        return userResponseEntity.getBody();
    }

    /* POST */
    @SuppressWarnings("unchecked")
    public User createUser(User user) {
        System.out.println("Testing create user");

        ResponseEntity<User> responseEntity =restTemplate.postForEntity(REST_SERVICE_URI + "/users", user, User.class);

        return responseEntity.getBody();
    }

    /* PUT */
    @SuppressWarnings("unchecked")
    public User updateUser(User user) {
        System.out.println("Testing create user");

        restTemplate.put(REST_SERVICE_URI + "/users", user, User.class);


        return null;
    }

    public boolean isExist(int id){
        try {
            restTemplate.getForEntity(REST_SERVICE_URI + "/users/" + id, User.class);
        }catch (HttpClientErrorException ex){
            return false;
        }

        return true;
    }


}
