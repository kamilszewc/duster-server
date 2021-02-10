package pl.integrable.dusterserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import pl.integrable.dusterserver.controller.rest.ContentRestContoller
import pl.integrable.dusterserver.model.Content
import pl.integrable.dusterserver.repository.ContentRepository
import java.awt.PageAttributes

@Controller
class ContentController {

    @Autowired
    lateinit var contentRepository: ContentRepository

    @Autowired
    lateinit var contentRestContoller: ContentRestContoller

    @GetMapping("/content")
    fun information(@RequestParam(name = "id", required = true) id: Long,
                    model: Model) : String {

        val content = contentRepository.findById(id)
        val allContent = contentRepository.findAll();

        if (content.isPresent) {
            model.addAttribute("content", content.get())
            model.addAttribute("allContent", allContent)

            return "content"
        }

        return "redirect:/error"
    }

    @GetMapping("/content/edit")
    fun contentEdit(@RequestParam(name = "id", required = false) id: Long,
                    model: Model) : String {

        val allContent = contentRepository.findAll()
        val content = contentRepository.findById(id)
        val files = contentRestContoller.fileList().body

        if (content.isPresent) {
            model.addAttribute("allContent", allContent)
            model.addAttribute("content", content.get())
            model.addAttribute("files", files)
        }

        return "contentEdit"
    }

    @PostMapping("/content/edit")
    fun contentEditPost(@RequestParam(name = "id", required = false) id: Long,
                        content: String) : String {

        val contentObject = contentRepository.findById(id)
        if (contentObject.isPresent) {
            contentObject.get().content = content

            contentRepository.save(contentObject.get())

            return "redirect:/content/edit?id=" + id
        }

        return "redirect:/error"
    }
}