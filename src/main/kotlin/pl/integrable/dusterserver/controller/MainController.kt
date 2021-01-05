package pl.integrable.dusterserver.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class MainController {

    @GetMapping("/")
    fun main() : String {
        return "redirect:/sensors"
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST, RequestMethod.GET])
    fun login(): String? {
        return "login"
    }

    // Login form with error
    @RequestMapping("/login-error")
    fun loginError(model: Model): String? {
        model.addAttribute("loginError", true)
        return "login"
    }
}