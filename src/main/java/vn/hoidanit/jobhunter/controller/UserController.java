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

import io.micrometer.core.instrument.Meter.Id;
import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Entity.User;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserCreateDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.RestResponse;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.UserFetchToDTO;
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
    public ResponseEntity<ResUserCreateDTO> createNewUser(@Valid @RequestBody User user) throws IdInvalidException { // class
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        if (this.userService.existsByEmail(user.getEmail())) {
            throw new IdInvalidException(
                    "Email " + user.getEmail() + " đã tồn tại");
            // RestResponse<Void> rest = new RestResponse<Void>();
            // rest.setStatusCode(400);
            // rest.setError("Email đã tồn tại!");
            // rest.setMessage("Exception occrus....");
            // rest.setData(null);
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rest);
        }
        User userRe = this.userService.saveUser(user);
        // UserDTO dto = new UserDTO();
        // dto.setId(userRe.getId());
        // dto.setName(userRe.getName());
        // dto.setEmail(userRe.getEmail());
        // dto.setAddress(userRe.getAddress());
        // dto.setAge(userRe.getAge());
        // dto.setGender(userRe.getGender());
        // dto.setCreateAt(userRe.getCreateAt());
        // // dto.setCreateBy(userRe.getCreateBy());
        return ResponseEntity.ok(this.userService.convertUserToDTO(userRe));
        // return ResponseEntity.status(HttpStatus.CREATED).body(userRe);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if (this.userService.fetchUserById(id) == null) {
            throw new IdInvalidException("user voi id = " + id + "khong ton tai");
        }
        this.userService.DeleteById(id);
        return ResponseEntity.ok(null);
    }

    // @GetMapping("/users")
    // public ResponseEntity<ResultPaginationDTO> fetchAllUser(

    // @RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional) {
    // String currentPage = currentOptional.isPresent() ? currentOptional.get() :
    // "";
    // String pageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
    // Integer current = Integer.parseInt(currentPage);
    // Integer pageSizeNumber = Integer.parseInt(pageSize);
    // Pageable pageable = PageRequest.of(current - 1, pageSizeNumber);
    // // return this.userService.fetchAllUser();
    // return
    // ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(pageable));
    // }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUserPagin(
            @Filter Specification<User> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUserPlus(pageable, spec));

    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch User By Id")
    public ResponseEntity<UserFetchToDTO> fetchUserById(@PathVariable("id") Long id) throws IdInvalidException {
        // return this.userService.fetchUserById(id);
        if (this.userService.checkId(id)) {
            return ResponseEntity.ok(this.userService.convertUserFetchToDTO(this.userService.fetchUserById(id)));
        } else {
            throw new IdInvalidException("id " + id + " không tồn tại");
        }
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUserById(@RequestBody User user) throws IdInvalidException {
        // return this.userService.updateUserById(user);
        if (this.userService.checkId(user.getId())) {
            return ResponseEntity.ok(this.userService.convertUpdate(this.userService.handleUpdateUser(user)));
        } else {
            throw new IdInvalidException(String.format("id = %d không được tìm thấy", user.getId()));
        }
        // return
        // ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUserById(user));
    }

}
