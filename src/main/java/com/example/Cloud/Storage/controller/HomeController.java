package com.example.Cloud.Storage.controller;

import com.example.Cloud.Storage.model.NoteModel;
import com.example.Cloud.Storage.service.NoteListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteListService noteListService;

    public HomeController(NoteListService noteListService) {
        this.noteListService = noteListService;
    }

    @GetMapping()
    public String getHome(Model model, @RequestParam(value = "fragment", required = false, defaultValue = "nav-files") String fragment)
    {
        model.addAttribute("activeTab", fragment);

        switch (fragment)
        {
            case "nav-notes":
                model.addAttribute("notes", noteListService.getAllNotes());
                ArrayList<NoteModel> noteModels = noteListService.getAllNotes();
                System.out.println(Arrays.toString(noteModels.toArray()));
                break;
            default:
                break;
        }

        return "home";
    }
}
