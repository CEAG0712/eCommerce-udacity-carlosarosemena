package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderControllerTest {



    private OrderController orderController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Cart tCart;

    private User tUser;
    private String username = "username";
    private String password = "password";

    private Item tItem;
    private String tItemName = "testItem";
    private BigDecimal tItemPrice = new BigDecimal(500.99);
    private String tItemDescription = "testDescription";

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        tUser = userRepository.findByUsername(username);

        tUser = new User();
        tUser.setUsername(username);
        tUser.setPassword(password);
        userRepository.save(tUser);


        tItem = new Item();
        tItem.setName(tItemName);
        tItem.setDescription(tItemDescription);
        tItem.setPrice(tItemPrice);
        itemRepository.save(tItem);

        tCart = new Cart();
        tCart.setUser(tUser);
        tCart.addItem(tItem);
        tUser.setCart(tCart);
        cartRepository.save(tCart);
    }



    @Test
    public void submit(){
        ResponseEntity<UserOrder> response = orderController.submit(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, orderRepository.findAll().size());
    }

    @Test
    public void getOrdersForUser(){
        ResponseEntity responseEntity = orderController.getOrdersForUser(username);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }
}
