package pl.integrable.dusterserver.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import pl.integrable.dusterserver.property.FileStorageProperties
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class FileStorageService @Autowired constructor(fileStorageProperties: FileStorageProperties) {
    private val fileStorageLocation: Path
    val taskStorage: Path
        get() = Paths.get(fileStorageLocation.toString())

    fun storeFile(file: MultipartFile): String {
        // Normalize file name
        val fileName = StringUtils.cleanPath(file.getOriginalFilename()!!)
        var targetLocation = Path.of("$taskStorage")
        return try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw Exception("Sorry! Filename contains invalid path sequence $fileName")
            }

            // Copy file to the target location (Replacing existing file with the same name)
            targetLocation = targetLocation.resolve(fileName)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING)
            fileName
        } catch (ex: IOException) {
            throw Exception("Could not store file $fileName. Please try again!", ex)
        }
    }

    fun loadFileAsResource(fileName: String): Resource {
        return try {
            val storageLocation = taskStorage
            val filePath = storageLocation.resolve(fileName)
            val resource: Resource = UrlResource(filePath.toUri())
            if (resource.exists()) {
                resource
            } else {
                throw Exception("File not found $fileName")
            }
        } catch (ex: MalformedURLException) {
            throw Exception("File not found $fileName", ex)
        }
    }

    fun listFiles(): List<String> {
        val storageLocation = Paths.get(fileStorageLocation.toString())
        val file = File(storageLocation.toString())
        val unfilteredList = Arrays.asList(*file.list())
        val list: MutableList<String> = ArrayList()
        for (filename in unfilteredList) {
            val filePath = storageLocation.resolve(filename)
            val f = File(filePath.toString())
            if (!f.isDirectory) {
                list.add(filename)
            }
        }
        return list
    }

    fun renameFile(oldFilename: String, newFilename: String?) {

        // Get file path
        val targetDir = taskStorage
        val oldLocation = targetDir.resolve(oldFilename)
        val newLocation = targetDir.resolve(newFilename)
        // Get file
        val file = File(oldLocation.toString())
        // Rename file
        if (file.exists()) {
            // Rename file
            file.renameTo(File(newLocation.toString()))
        } else {
            throw Exception("File not found $oldFilename")
        }
    }

    fun deleteFile(filename: String) {
        // Get file path
        val targetDir = Path.of("$taskStorage")
        val location = targetDir.resolve(filename)
        // Get file
        val file = File(location.toString())
        // Delete file
        if (file.exists()) {
            // Rename file
            FileSystemUtils.deleteRecursively(file)
        } else {
            throw Exception("File not found $filename")
        }
    }

    init {
        fileStorageLocation = Paths.get(fileStorageProperties.directory)
            .toAbsolutePath().normalize()
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (ex: Exception) {
            if (!Files.isSymbolicLink(fileStorageLocation)) {
                throw Exception(
                    "Could not create the directory where the uploaded files will be stored.",
                    ex
                )
            }
        }
    }
}