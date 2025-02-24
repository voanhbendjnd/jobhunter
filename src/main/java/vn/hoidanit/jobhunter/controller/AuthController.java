package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.proc.SecurityContext;

import io.micrometer.core.instrument.Meter.Id;
import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.config.SercurityConfiguration;
import vn.hoidanit.jobhunter.domain.Entity.User;
import vn.hoidanit.jobhunter.domain.request.RequestLoginDTO;
import vn.hoidanit.jobhunter.domain.response.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @Value("${hoidanit.jwt.access-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> Login(@Valid @RequestBody RequestLoginDTO dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                dto.getUsername(), dto.getPassword());
        // xác thực người dùng : cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // set thông tn người dùng đăng nhập vào context ()
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO res = new ResLoginDTO();
        User userDataBase = this.userService.fecthUserByUserName(dto.getUsername());
        if (userDataBase != null) {
            ResLoginDTO.UserLogin son = new ResLoginDTO.UserLogin(userDataBase.getId(), userDataBase.getEmail(),
                    userDataBase.getName());
            res.setUser(son);
        }
        // create token
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), res.getUser());
        res.setAccessToken(access_token);
        // create token re
        String refreshToken = this.securityUtil.createRefreshToken(dto.getUsername(), res);
        // update
        this.userService.updateUserToken(refreshToken, dto.getUsername());
        // set cookies
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true) // chi chap nhan moi server nay
                .secure(true) // chi nhan https
                .path("/") // cho phep gui cookies cho tat ca giao thuc
                .maxAge(refreshTokenExpiration) // thoi gian het han

                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User userDataBase = this.userService.fecthUserByUserName(email);
        ResLoginDTO.UserLogin son = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if (userDataBase != null) {
            son.setId(userDataBase.getId());
            son.setEmail(userDataBase.getEmail());
            son.setName(userDataBase.getName());
            userGetAccount.setUser(son);

        }
        return ResponseEntity.ok(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get new Token Refresh")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String rfToken)
            throws IdInvalidException {
        if (rfToken.equals("abc")) {
            throw new IdInvalidException("---Không truyền lên token!---");
        }
        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(rfToken);
        String email = decodedToken.getSubject();
        User user = this.userService.getUserByRefreshTokenAndEmail(rfToken, email);
        if (user == null) {
            throw new IdInvalidException("----Refresh Token Không hơp lệ----");
        }

        ResLoginDTO res = new ResLoginDTO();
        User userDataBase = this.userService.fecthUserByUserName(email);
        if (userDataBase != null) {
            ResLoginDTO.UserLogin son = new ResLoginDTO.UserLogin(userDataBase.getId(), userDataBase.getEmail(),
                    userDataBase.getName());
            res.setUser(son);
        }
        // create token
        String access_token = this.securityUtil.createAccessToken(email, res.getUser());
        res.setAccessToken(access_token);
        // create token re
        String new_refreshToken = this.securityUtil.createRefreshToken(email, res);
        // update
        this.userService.updateUserToken(new_refreshToken, email);
        // set cookies
        ResponseCookie cookie = ResponseCookie.from("refresh_token", new_refreshToken)
                .httpOnly(true) // chi chap nhan moi server nay
                .secure(true) // chi nhan https
                .path("/") // cho phep gui cookies cho tat ca giao thuc
                .maxAge(refreshTokenExpiration) // thoi gian het han

                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
    }

    // logout
    @PutMapping("auth/logout")
    public ResponseEntity<Void> postLogout(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String rfToken) throws IdInvalidException {
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(rfToken);
        String eamil = decodedToken.getSubject();
        User user = this.userService.getUserByRefreshTokenAndEmail(rfToken, eamil);
        user.setRefreshToken(null);
        this.userService.saveUser(user);
        return ResponseEntity.ok(null);
    }

    @PostMapping("auth/logout")
    public ResponseEntity<Void> postLogout2() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Access Token không hợp lệ");
        }
        // update
        this.userService.updateUserToken(null, email);

        // remove
        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(null);
    }

}
