package com.example.Cloud.Storage.service;

import com.example.Cloud.Storage.mapper.NoteMapper;
import com.example.Cloud.Storage.model.NoteModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NoteListService {

    private NoteMapper noteMapper;

    public NoteListService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public ArrayList<NoteModel> getAllNotes()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        return noteMapper.getAllNotes(name);
    }
}
