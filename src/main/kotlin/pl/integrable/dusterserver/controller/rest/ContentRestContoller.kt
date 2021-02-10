package pl.integrable.dusterserver.controller.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.integrable.dusterserver.repository.SensorRepository
import pl.integrable.dusterserver.service.FileStorageService
import pl.integrable.dusterserver.service.JwtTokenService
import java.io.File
import java.lang.Exception
import java.util.*

@RestController
class ContentRestContoller {

    @Autowired
    lateinit var fileStorageService: FileStorageService



    @PostMapping("/api/v1/content/uploadFile")
    fun uploadFile(@RequestParam("file") file : MultipartFile) : ResponseEntity<String> {

        if (!file.isEmpty()) {

            fileStorageService.storeFile(file)

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Uploaded")
        }

        return return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Not uploaded")
    }

    @GetMapping("/api/v1/content/fileList")
    fun fileList() : ResponseEntity<List<String>> {

        return try {
            // Get list of files
            val files = fileStorageService.listFiles()
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(files)
        } catch (ex: Exception) {
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(null)
        }
    }

    @DeleteMapping("/api/v1/content/deleteFile/{filename}")
    fun deleteFile(@PathVariable filename: String) : ResponseEntity<String> {

        // Delete file
        return try {
            fileStorageService.deleteFile(filename)
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("File successfully deleted")
        } catch (ex: Exception) {
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Can not delete file")
        }
    }


}