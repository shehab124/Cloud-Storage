package com.example.Cloud.Storage.mapper;

import com.example.Cloud.Storage.model.CredentialModel;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface CredentialMapper {

    @Select("SELECT creds.credentialid, creds.url, creds.username, creds.keys, creds.password, creds.userid" +
            " FROM CREDENTIALS as creds" +
            " INNER JOIN USERS as users ON creds.userid = users.userid" +
            " WHERE  users.username = #{username};")
    ArrayList<CredentialModel> getAllCredentials(String username);

    @Select("SELECT userid FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    int getUserId(Integer credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    CredentialModel getCredential(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, keys, password, userid) VALUES (#{url}, #{username}, #{keys}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int createCredential(CredentialModel credentialModel);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, keys = #{keys}, password = #{password} WHERE credentialid = #{credentialId}")
    void updateCredential(CredentialModel credentialModel);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    int deleteCredential(Integer credentialId);

}
