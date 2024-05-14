package com.example.Cloud.Storage.service;

import com.example.Cloud.Storage.mapper.FileMapper;
import com.example.Cloud.Storage.mapper.UserMapper;
import com.example.Cloud.Storage.model.FileModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FileListService {

    private FileMapper fileMapper;
    private UserMapper userMapper;

    public FileListService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public ArrayList<FileModel> getAllFiles()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        return fileMapper.getAllFiles(name);
    }

    public void createFile(String fileName, String contentType, long fileSize, byte[] fileData) throws Exception {
        if(fileName.contains(".."))
            throw new Exception("Invalid file name");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userMapper.getUserId(authentication.getName());

        String available = fileMapper.isFileNameAvailable(fileName, userId);

        if(available == null)
        {
            FileModel fileModel = new FileModel(null, fileName, contentType, fileSize, userId, fileData);
            fileMapper.createFile(fileModel);
        }
        else
            throw new Exception("File name already exists");

    }

    public void deleteFile(Integer fileId) throws Exception
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userMapper.getUserId(authentication.getName());
        int userId2 = fileMapper.getUserId(fileId);
        int rowsAffected;
        if(userId == userId2)
        {
            rowsAffected = fileMapper.deleteFile(fileId);
            if (rowsAffected == 0)
                throw new Exception("Invalid File Id");
        }
        else
            throw new Exception("Invalid User Id");
    }

    public FileModel getFile(Integer fileId) throws Exception
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Integer id = userMapper.getUserId(name);

        int id2 = fileMapper.getUserId(fileId);

        if(id == id2)
        {
            return fileMapper.getFile(fileId);
        }
        else
            throw new Exception("Invalid File Id");
    }

}
