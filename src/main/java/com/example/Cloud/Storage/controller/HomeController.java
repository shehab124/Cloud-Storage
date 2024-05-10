package com.example.Cloud.Storage.controller;

import com.example.Cloud.Storage.model.CredentialModel;
import com.example.Cloud.Storage.model.NoteModel;
import com.example.Cloud.Storage.service.CredentialListService;
import com.example.Cloud.Storage.service.EncryptionService;
import com.example.Cloud.Storage.service.NoteListService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteListService noteListService;
    private CredentialListService credentialListService;
    private EncryptionService encryptionService;

    public HomeController(NoteListService noteListService, CredentialListService credentialListService, EncryptionService encryptionService) {
        this.noteListService = noteListService;
        this.credentialListService = credentialListService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String getHome(Model model,
                          NoteModel noteModel,
                          CredentialModel credentialModel,
                          @RequestParam(value = "fragment", required = false, defaultValue = "nav-files") String fragment)
    {
        model.addAttribute("activeTab", fragment);

        switch (fragment)
        {
            case "nav-notes":
                model.addAttribute("notes", noteListService.getAllNotes());
                break;
            case "nav-credentials":
                model.addAttribute("credentials", credentialListService.getAllCredentials());
            default:
                break;
        }
        return "home";
    }

    @GetMapping("/decrypt")
    @ResponseBody
    public String decrypt(@RequestParam(name = "credId", required = true) Integer credId)
    {
        CredentialModel credentialModel = credentialListService.getCredential(credId);
        return encryptionService.decryptValue(credentialModel.getPassword(), credentialModel.getKeys());

    }
}
