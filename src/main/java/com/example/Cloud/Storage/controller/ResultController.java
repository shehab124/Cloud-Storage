package com.example.Cloud.Storage.controller;

import com.example.Cloud.Storage.model.NoteModel;
import com.example.Cloud.Storage.service.NoteListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/result")
public class ResultController {

    private final NoteListService noteListService;

    public ResultController(NoteListService noteListService) {
        this.noteListService = noteListService;
    }

    /**
     * @Desc add or update a note
     */
    @PostMapping("/addNote")
    public String addNote(NoteModel noteModel, Model model)
    {
        if(noteModel.getNoteId() == null)
            noteListService.createNote(noteModel.getNoteTitle(), noteModel.getNoteDescription());
        else
            noteListService.updateNote(noteModel);
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/deleteNote/{noteId}")
    public String deleteNote(NoteModel noteModel, @PathVariable Integer noteId, Model model)
    {
        String result = noteListService.deleteNote(noteId);
        if(result.equals("Success"))
        {
            model.addAttribute("success", true);
        }
        else {
            model.addAttribute("fail", false);
            model.addAttribute("message", result);
        }
        return "result";
    }
}
