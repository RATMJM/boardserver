package com.boardTest.boardServer.controller;

import com.boardTest.boardServer.aop.LoginCheck;
import com.boardTest.boardServer.dto.UserDTO;
import com.boardTest.boardServer.dto.request.UserDeleteId;
import com.boardTest.boardServer.dto.request.UserLoginRequest;
import com.boardTest.boardServer.dto.request.UserUpdatePasswordRequest;
import com.boardTest.boardServer.dto.response.LoginResponse;
import com.boardTest.boardServer.dto.response.UserInfoResponse;
import com.boardTest.boardServer.service.impl.UserServiceImpl;
import com.boardTest.boardServer.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    private final UserServiceImpl userService;
    private static final ResponseEntity<LoginResponse> FAIL_RESPONSE = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
    private static LoginResponse loginResponse;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

//    HashMap<String, String> sessionMap = new HashMap<>();
    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDTO userDTO) {
        if (UserDTO.hasNullDataBeforeSignup(userDTO)) {
            throw new NullPointerException("회원가입시 필수 데이터를 모두 입력해야 합니다.");
        }
        userService.register(userDTO);
    }

    @PostMapping("sign-in")
    public HttpStatus login(@RequestBody UserLoginRequest loginRequest,
                            HttpSession session) {

        ResponseEntity<LoginResponse> responseEntity = null;
        String id = loginRequest.getUserId();
        String password = loginRequest.getPassword();
        UserDTO userInfo = userService.login(id, password);

        if (userInfo == null) { // 유저정보 불일치
            return HttpStatus.NOT_FOUND;
        } else if (userInfo != null) { // 유저정보 일치
            loginResponse = LoginResponse.success(userInfo);
            if (userInfo.getStatus() == (UserDTO.Status.ADMIN)) //어드민일때
                SessionUtil.setLoginAdminId(session, id);
            else // 일반유저
                SessionUtil.setLoginMemberId(session, id);

            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("Login Error! 유저 정보가 없거나 지워진 유저 정보입니다.");
        }

        return HttpStatus.OK;
    }

    @GetMapping("my-info")
    public UserInfoResponse memberInfo(HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);
        if (id == null) id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        return new UserInfoResponse(memberInfo);
    }

    @PutMapping("logout")
    public void logout(String accountId, HttpSession session) {
        SessionUtil.clear(session);
    }

    @PatchMapping("password")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> updateUserPassword(  String accountId, @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                                            HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String Id = accountId;
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            userService.updatePassword(Id, beforePassword, afterPassword);
            ResponseEntity.ok(new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK));
        } catch (IllegalArgumentException e) {
            log.error("updatePassword 실패", e);
            responseEntity = FAIL_RESPONSE;
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteId userDeleteId,
                                                  HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String Id = SessionUtil.getLoginMemberId(session);

        try {
            userService.deleteId(Id, userDeleteId.getPassword());
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.info("deleteID 실패");
            responseEntity = FAIL_RESPONSE;
        }
        return responseEntity;
    }

    @GetMapping("/{userId}/profile")
    public UserDTO getUserProfile(@RequestBody UserDTO userDTO, @PathVariable(value="userId") String userId){
        log.info(userId+ " 여기ㅣㅂㄴ디ㅏ ");
        return userService.getUserProfile(userDTO, userId);
    }

//    @GetMapping("/redis-login")
//    public String login(HttpSession httpSession, @RequestParam("name") String name){
////        sessionMap.put(httpSession.getId(), name);
//        redis.set(session.)
//        return "saved.";
//    }
//
//    @GetMapping("/myname")
//    public String myname(HttpSession httpSession){
////        String myname = sessionMap.get(httpSession.getId());
//
//        return myname;
//    }


}