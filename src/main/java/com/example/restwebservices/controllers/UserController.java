package com.example.restwebservices.controllers;

import com.example.restwebservices.errors.UserNotFoundException;
import com.example.restwebservices.model.User;
import com.example.restwebservices.service.UserDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserDaoService userDaoService;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userDaoService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel> getUser(@PathVariable int id){
        User user = userDaoService.findOne(id);

        if (user == null) throw new UserNotFoundException("" + id);

        Link link =
                linkTo(methodOn(this.getClass()).getAllUsers())
                        .withRel("all-users");

        return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(user, link));
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userDaoService.addUser(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(userDaoService.deleteById(id));
    }

}
