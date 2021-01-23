package pl.integrable.dusterserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import pl.integrable.dusterserver.repository.ContentRepository

@Controller
class ContentController {

    @Autowired
    lateinit var contentRepository: ContentRepository

    @GetMapping("/content")
    fun information(@RequestParam(name = "name", required = true) name: String,
                    model: Model) : String {

        val content = contentRepository.findByName(name)
        val allContent = contentRepository.findAll();

        if (content.isPresent) {
            model.addAttribute("content", content.get())
            model.addAttribute("allContent", allContent)

            return "content"
        }

        return "redirect:/error"
    }
}