package com.yauhenl.foe.web;

import com.yauhenl.foe.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("data", accountService.getData("", ""));
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String sid, @RequestParam String userKey, Model model) {
        model.addAttribute("data", accountService.getData(sid, userKey));
        return "index";
    }
}
