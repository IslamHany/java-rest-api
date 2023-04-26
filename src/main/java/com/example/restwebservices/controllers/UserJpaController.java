package com.example.restwebservices.controllers;

import com.example.restwebservices.errors.UserNotFoundException;
import com.example.restwebservices.model.Post;
import com.example.restwebservices.model.User;
import com.example.restwebservices.repos.PostRepository;
import com.example.restwebservices.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJpaController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/jpa/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public ResponseEntity<EntityModel> getUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) throw new UserNotFoundException("" + id);

        Link link =
                linkTo(methodOn(this.getClass()).getAllUsers())
                        .withRel("all-users");

        return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(user, link));
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

    @DeleteMapping("/jpa/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) throw new UserNotFoundException("" + id);

        userRepository.deleteById(id);
        return ResponseEntity.ok().body(user.get());
    }

    @GetMapping("/jpa/users/{id}/posts")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable Integer id){
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) throw new UserNotFoundException("" + id);

        return ResponseEntity.ok().body(user.get().getPosts());
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<EntityModel> createPost(@PathVariable Integer id, @RequestBody Post post){
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) throw new UserNotFoundException("" + id);

        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        Link link =
                linkTo(methodOn(this.getClass()).getUserPosts(user.get().getId()))
                        .withRel("post");

        return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(post, link));
    }
}
