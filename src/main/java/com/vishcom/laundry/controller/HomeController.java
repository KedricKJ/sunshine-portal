package com.vishcom.laundry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping(value = "/")
    public String index() {
        return "index.html";
    }

    /*@RequestMapping( method = {RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.GET}, path = {"/api/**", "/path2/**", "/"} )
    public String forwardAngularPaths() {
        return "forward:/index.html";
    }*/
}
