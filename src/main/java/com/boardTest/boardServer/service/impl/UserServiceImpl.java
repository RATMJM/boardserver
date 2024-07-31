package com.boardTest.boardServer.service.impl;

import com.boardTest.boardServer.dto.UserDTO;
import com.boardTest.boardServer.exception.DuplicateIdException;
import com.boardTest.boardServer.mapper.UserProfileMapper;
import com.boardTest.boardServer.service.ExternalApiService;
import com.boardTest.boardServer.service.UserService;
import com.boardTest.boardServer.utils.SHA256Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.boardTest.boardServer.utils.SHA256Util.encryptSHA256;
@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    public UserServiceImpl( UserProfileMapper userProfileMapper){
        this.userProfileMapper = userProfileMapper;
    }

    @Autowired
    private ExternalApiService externalApiService;
    @Override
    public UserDTO getUserInfo(String userId) {
        return userProfileMapper.getUserProfile(userId);
    }

    @Override
    public void register(UserDTO userDTO) {
        boolean duplIdResult = isDuplicatedId(userDTO.getUserId());
        if (duplIdResult) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        userDTO.setCreateTime(new Date());
        userDTO.setPassword(SHA256Util.encryptSHA256(userDTO.getPassword()));
        int insertCount = userProfileMapper.register(userDTO);

        if (insertCount != 1) {
            log.error("insertMember ERROR! {}", userDTO);
            throw new RuntimeException(
                    "insertUser ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + userDTO);
        }
    }

    @Override
    public UserDTO login(String id, String password) {
        String cryptPassword = SHA256Util.encryptSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptPassword );
        return memberInfo;
    }

    @Override
    public boolean isDuplicatedId(String id) {
        return userProfileMapper.idCheck(id) == 1;
    }


    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        String cryptoPassword = SHA256Util.encryptSHA256(beforePassword);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);

        if (memberInfo != null) {
            memberInfo.setPassword(SHA256Util.encryptSHA256(afterPassword));
            int insertCount = userProfileMapper.updatePassword(memberInfo);
        } else {
            log.error("updatePasswrod ERROR! {}", memberInfo);
            throw new IllegalArgumentException("updatePasswrod ERROR! 비밀번호 변경 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String crytPassword = encryptSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByUserIdAndPassword(id, crytPassword);
        if(memberInfo != null){
            int deleteCount = userProfileMapper.deleteUserProfile(id);
        }else{
            log.error("delete userinfo Error!  {}" , memberInfo);
            throw new RuntimeException("비밀번호가 일치하는 정보가 없습니다.");
        }
    }

    @Override
    public UserDTO getUserProfile(UserDTO userDTO, String Id){
//        userDTO.setNickName("");
//
//        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        String cacheName= ops.get("nameKey:"+Id );// 레디스에 저장됐는지 조회
//        if(cacheName != null){ //cache hit
//            userDTO.setNickName(cacheName);
//        }else{ //empty
//
//            ops.set("nameKey:"+Id, nickName, 5, TimeUnit.SECONDS);
//        }


        int age =  externalApiService.getUserAge(Id);  // 레디스 캐싱 적용
        String nickName=  externalApiService.getUserName(Id); //레디스 캐싱 적용

        userDTO.setAge(age);
        userDTO.setNickName(nickName);
//        return   UserDTO(nickName, age);
        return userDTO;
    }
}
