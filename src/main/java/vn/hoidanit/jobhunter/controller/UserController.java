package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User userRe = this.userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userRe);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("id no large than 1500");
        }
        this.userService.DeleteById(id);
        // return ResponseEntity.ok("delete success!");
        return ResponseEntity.status(HttpStatus.OK).body("delete success");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchAllUser() {
        // return this.userService.fetchAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable("id") Long id) {
        // return this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchUserById(id));
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUserById(@RequestBody User user) {
        // return this.userService.updateUserById(user);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUserById(user));
    }

}
