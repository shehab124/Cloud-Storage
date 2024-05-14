package com.example.Cloud.Storage.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String getErrorPage(HttpServletRequest httpServletRequest, Model model)
    {
        Integer status = (Integer)httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        String errorMsg = "";

        switch (status) {
            case 400: {
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }

        model.addAttribute("errorMsg" ,errorMsg);

        return "error";
    }

}
