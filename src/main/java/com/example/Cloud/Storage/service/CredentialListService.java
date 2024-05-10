package com.example.Cloud.Storage.service;

import com.example.Cloud.Storage.mapper.CredentialMapper;
import com.example.Cloud.Storage.mapper.UserMapper;
import com.example.Cloud.Storage.model.CredentialModel;
import com.example.Cloud.Storage.model.NoteModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CredentialListService {

    private final UserMapper userMapper;
    private final CredentialMapper credentialMapper;
    private final  EncryptionService encryptionService;

    public CredentialListService(UserMapper userMapper, CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.userMapper = userMapper;
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public ArrayList<CredentialModel> getAllCredentials()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        return credentialMapper.getAllCredentials(name);
    }

    public CredentialModel getCredential(Integer credentialId)
    {
        return credentialMapper.getCredential(credentialId);
    }

    public int createCredential(String url, String username, String password)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userMapper.getUserId(authentication.getName());

        String key = encryptionService.generateSecretKey();
        String encryptedPass = encryptionService.encryptValue(password, key);

        CredentialModel credentialModel = new CredentialModel(null, url, username, key, encryptedPass, userId);
        return credentialMapper.createCredential(credentialModel);
    }

    public void updateCredential(CredentialModel credentialModel)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userMapper.getUserId(authentication.getName());

        String key = encryptionService.generateSecretKey();
        String encryptedPass = encryptionService.encryptValue(credentialModel.getPassword(), key);

        credentialModel.setKeys(key);
        credentialModel.setPassword(encryptedPass);
        credentialModel.setUserId(userId);

        credentialMapper.updateCredential(credentialModel);
    }

    public String deleteCredential(Integer credentialId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userMapper.getUserId(authentication.getName());
        int userId2 = credentialMapper.getUserId(credentialId);

        int rowsAffected;
        if(userId == userId2)
        {
            rowsAffected = credentialMapper.deleteCredential(credentialId);
            if(rowsAffected == 0)
                return "Invalid Credential Id";
            else
                return "Success";
        }
        else
            return "Invalid User Id";
    }

}
