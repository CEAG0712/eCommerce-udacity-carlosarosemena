package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserControllerTest {

    //https://devops.com/13-best-practices-successful-software-testing-projects/

    private UserController userController;




    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private User testUser;
    private String usernameTest = "username";
    private String passwordTest = "password";

    @BeforeTransaction
    public void setUp(){
        userController = new UserController();

        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);

    }

    @Before
    public void loadData(){

        testUser = new User();
        testUser.setUsername(usernameTest);
        testUser.setPassword(passwordTest);
        testUser.setId(1L);
        userRepository.save(testUser);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }


    @Test
    public void findById() throws Exception {
        Optional<User> user = userRepository.findById(1L);
        ResponseEntity responseEntity = userController.findById(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


    }


    @Test
    public void findById_NotFound() throws Exception {
        ResponseEntity responseEntity = userController.findById(1012L);
        assertEquals(Optional.empty(),responseEntity.getBody());
    }


    @Test
    public void findByUserName() throws Exception {
        ResponseEntity responseEntity = userController.findByUserName("username");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    public void findByUserName_NotFound() throws Exception {
        ResponseEntity responseEntity = userController.findByUserName("usernameDoesNotExists");
        assertEquals(null,responseEntity.getBody());
    }

    @Test
    public void passwordDoNotMatch(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("Password");
        userRequest.setConfirmPassword("Passwrdxxxxx");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}
