package com.example.Cloud.Storage.mapper;

import com.example.Cloud.Storage.model.FileModel;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface FileMapper {

    @Select("SELECT f.fileid, f.filename, f.contenttype, f.filesize" +
            " FROM FILES as f " +
            " INNER JOIN USERS as users ON f.userid = users.userid" +
            " WHERE users.username = #{username}")
    ArrayList<FileModel> getAllFiles(String username);

    @Select("SELECT userid FROM FILES WHERE fileid = #{fileId}")
    int getUserId(Integer fileId);

    @Select("SELECT filename FROM FILES WHERE filename = #{fileName} AND userid = #{userid}")
    String isFileNameAvailable(String fileName, Integer userid);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    FileModel getFile(Integer fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int createFile(FileModel fileModel);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    int deleteFile(Integer fileId);

}
