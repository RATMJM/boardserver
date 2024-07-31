package com.boardTest.boardServer.service;

import com.boardTest.boardServer.dto.UserDTO;

//시그니처만 작성, 메서드내용은 작성하지않음
public interface UserService
{
    void register(UserDTO userProfile);

    UserDTO login(String id, String password);

    boolean isDuplicatedId(String id);

    UserDTO getUserInfo(String userId);

    void updatePassword(String id, String beforePassword, String afterPassword);

    void deleteId(String id, String password);

    UserDTO getUserProfile(UserDTO userProfile, String userId);

}
