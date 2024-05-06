package com.example.Cloud.Storage.service;

import com.example.Cloud.Storage.mapper.UserMapper;
import com.example.Cloud.Storage.model.UserModel;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    /**
     * @Param String username
     * @Description checks if username is not already taken
     * @Returns returns true if username can be used
     */
    public boolean isUsernameAvailable(String username)
    {
        return userMapper.getUser(username) == null;
    }

    public UserModel getUser(String username)
    {
        return userMapper.getUser(username);
    }

    public int createUser(UserModel userModel)
    {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(userModel.getPassword(), encodedSalt);

        return userMapper.insert(new UserModel(null, userModel.getUsername(), encodedSalt, hashedPassword, userModel.getFirstname(), userModel.getLastname()));
    }

}
