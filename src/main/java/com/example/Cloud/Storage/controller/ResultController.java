package com.example.Cloud.Storage.controller;

import com.example.Cloud.Storage.model.CredentialModel;
import com.example.Cloud.Storage.model.FileModel;
import com.example.Cloud.Storage.model.NoteModel;
import com.example.Cloud.Storage.service.CredentialListService;
import com.example.Cloud.Storage.service.FileListService;
import com.example.Cloud.Storage.service.NoteListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/result")
public class ResultController {

    private final NoteListService noteListService;
    private final CredentialListService credentialListService;
    private final FileListService fileListService;

    public ResultController(NoteListService noteListService, CredentialListService credentialListService, FileListService fileListService) {
        this.noteListService = noteListService;
        this.credentialListService = credentialListService;
        this.fileListService = fileListService;
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

    @PostMapping("/addCredential")
    public String addCredential(CredentialModel credentialModel, Model model)
    {
        if(credentialModel.getCredentialId() == null)
            credentialListService.createCredential(credentialModel.getUrl(), credentialModel.getUsername(), credentialModel.getPassword());
        else
            credentialListService.updateCredential(credentialModel);
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredential(CredentialModel credentialModel, @PathVariable Integer credentialId, Model model)
    {
        String result = credentialListService.deleteCredential(credentialId);
        if(result.equals("Success"))
        {
            model.addAttribute("success", true);
        }
        else
        {
            model.addAttribute("fail", false);
            model.addAttribute("message", result);
        }
        return "result";
    }

    @PostMapping("/addFile")
    public String addFile(@RequestParam("file") MultipartFile multipartFile, Model model)
    {
        try {
            if (multipartFile.isEmpty())
                throw new Exception("No file selected, Please select a file");
            fileListService.createFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize(), multipartFile.getBytes());
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("fail", true);
            model.addAttribute("message", e.getMessage());
        }
        return "result";
    }

    @GetMapping("/deleteFile/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Model model)
    {
        try {
            fileListService.deleteFile(fileId);
            model.addAttribute("success", true);
        }
        catch (Exception e)
        {
            model.addAttribute("fail", true);
            model.addAttribute("message", e.getMessage());
        }
        return "result";
    }
}
