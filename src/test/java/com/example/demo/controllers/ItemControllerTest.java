package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ItemControllerTest {

    private ItemController itemController;

    @Autowired
    private ItemRepository itemRepository;

    private Item tItem;
    private String tItemName = "itemName";
    private BigDecimal tItemPrice = new BigDecimal(100.10);
    private String tItemDescription = "itemDescription";

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

        tItem = new Item();
        tItem.setName(tItemName);
        tItem.setDescription(tItemDescription);
        tItem.setPrice(tItemPrice);
        itemRepository.save(tItem);

    }

    @Test
    public void getItemById(){
        ResponseEntity<Item> response = itemController.getItemById(tItem.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tItem.getPrice(), response.getBody().getPrice());
    }

    @Test
    public void getItemById_NotFound(){
        ResponseEntity<Item> response = itemController.getItemById(1209L);
        assertEquals(null,response.getBody());

    }

    @Test
    public void getItems(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getItemsByName(){
        ResponseEntity<List<Item>>response = itemController.getItemsByName(tItemName);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getItemsByName_NotFound(){
        ResponseEntity<List<Item>>response = itemController.getItemsByName("whateverName");
        assertEquals(null, response.getBody());
    }


}
