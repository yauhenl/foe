package com.yauhenl.foe.web

import com.yauhenl.foe.service.RequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class IndexController(@Autowired val requestService: RequestService) {

    @GetMapping("/")
    @ResponseBody
    fun initialize(): String {
        requestService.initialize()
        return ""
    }
}
