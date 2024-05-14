package com.example.Cloud.Storage.controller;

import com.example.Cloud.Storage.model.CredentialModel;
import com.example.Cloud.Storage.model.FileModel;
import com.example.Cloud.Storage.model.NoteModel;
import com.example.Cloud.Storage.service.CredentialListService;
import com.example.Cloud.Storage.service.EncryptionService;
import com.example.Cloud.Storage.service.FileListService;
import com.example.Cloud.Storage.service.NoteListService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteListService noteListService;
    private CredentialListService credentialListService;
    private EncryptionService encryptionService;
    private FileListService fileListService;

    public HomeController(NoteListService noteListService,
                          CredentialListService credentialListService,
                          EncryptionService encryptionService,
                          FileListService fileListService) {
        this.noteListService = noteListService;
        this.credentialListService = credentialListService;
        this.encryptionService = encryptionService;
        this.fileListService = fileListService;
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
                break;
            case "nav-files":
                model.addAttribute("files", fileListService.getAllFiles());
                break;
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

    @GetMapping("/getFile/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId, Model model) throws Exception
    {
        FileModel fileModel = fileListService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileModel.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileModel.getFileName() + "\"")
                .body(new ByteArrayResource(fileModel.getFileData()));
    }
}
