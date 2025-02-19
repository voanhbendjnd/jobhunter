package vn.hoidanit.jobhunter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.RestResponse;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.UserDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<?> createNewUser(@RequestBody User user) throws IdInvalidException {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        if (this.userService.existsByEmail(user.getEmail())) {
            RestResponse<Void> rest = new RestResponse<Void>();
            rest.setStatusCode(400);
            rest.setError("Email đã tồn tại!");
            rest.setMessage("Exception occrus....");
            rest.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rest);
        }
        User userRe = this.userService.saveUser(user);
        UserDTO dto = new UserDTO();
        dto.setId(userRe.getId());
        dto.setName(userRe.getName());
        dto.setEmail(userRe.getEmail());
        dto.setAddress(userRe.getAddress());
        dto.setAge(userRe.getAge());
        dto.setCreateAt(userRe.getCreateAt());
        dto.setCreateBy(userRe.getCreateBy());
        return ResponseEntity.ok(dto);
        // return ResponseEntity.status(HttpStatus.CREATED).body(userRe);
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
    public ResponseEntity<ResultPaginationDTO> fetchAllUser(

            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String currentPage = currentOptional.isPresent() ? currentOptional.get() : "";
        String pageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        Integer current = Integer.parseInt(currentPage);
        Integer pageSizeNumber = Integer.parseInt(pageSize);
        Pageable pageable = PageRequest.of(current - 1, pageSizeNumber);
        // return this.userService.fetchAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(pageable));
    }

    @GetMapping("/users2")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUser2(
            @Filter Specification<User> spec,
            Pageable pageble) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser2(spec, pageble));
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
