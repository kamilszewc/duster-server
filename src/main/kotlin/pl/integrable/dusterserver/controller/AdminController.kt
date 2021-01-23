package pl.integrable.dusterserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import pl.integrable.dusterserver.repository.ContentRepository

@Controller
class AdminController {

    @Autowired
    lateinit var contentRepository: ContentRepository

    @GetMapping("/admin")
    fun admin(model: Model) : String {

        val allContent = contentRepository.findAll()
        model.addAttribute("allContent", allContent)

        return "admin"
    }


}