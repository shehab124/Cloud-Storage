package com.example.Cloud.Storage.service;

import com.example.Cloud.Storage.mapper.NoteMapper;
import com.example.Cloud.Storage.mapper.UserMapper;
import com.example.Cloud.Storage.model.NoteModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NoteListService {

    private NoteMapper noteMapper;
    private UserMapper userMapper;

    public NoteListService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public ArrayList<NoteModel> getAllNotes()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        return noteMapper.getAllNotes(name);
    }

    public int createNote(String title, String description)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userMapper.getUserId(authentication.getName());
        NoteModel noteModel = new NoteModel(null, title, description, userId);
        return noteMapper.createNote(noteModel);
    }

    public String deleteNote(Integer noteId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userMapper.getUserId(authentication.getName());
        int userId2 = noteMapper.getUserId(noteId);
        int rowsAffected;
        if(userId == userId2) {
            rowsAffected = noteMapper.deleteNote(noteId);
            if(rowsAffected == 0)
                return "Invalid Note Id";
            return "Success";
        }
        else
         return "Invalid User Id";
    }

    public void updateNote(NoteModel noteModel)
    {
        noteMapper.updateNote(noteModel);
    }

}
