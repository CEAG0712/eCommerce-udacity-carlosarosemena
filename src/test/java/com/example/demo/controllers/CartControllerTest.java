package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CartControllerTest {

    private CartController cartController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;


    private Cart tCart;

    private User tUser;
    private String tUsername = "tUsername";
    private String tPassword = "tPassword";

    private Item tItem;
    private String tItemName = "tItem";
    private BigDecimal tItemPrice = new BigDecimal(600.99);
    private String tItemDescription = "tDescription";


    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);


        tUser = new User();
        tUser.setUsername(tUsername);
        tUser.setPassword(tPassword);
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
    public void addToCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(tUsername);
        request.setItemId(tItem.getId());
        int quantity = 2;
        request.setQuantity(quantity);

        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void removeFromCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(tUsername);
        request.setItemId(tItem.getId());
        int quantity = 1;
        request.setQuantity(quantity);

        ResponseEntity<Cart> response = cartController.removeFromCart(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
