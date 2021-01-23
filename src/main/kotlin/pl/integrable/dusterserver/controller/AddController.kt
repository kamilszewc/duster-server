package pl.integrable.dusterserver.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AddController {

    @GetMapping("/add")
    fun add() : String {
        return "add"
    }
}