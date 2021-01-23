package pl.integrable.dusterserver.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.integrable.dusterserver.model.Content
import java.util.*

interface ContentRepository : JpaRepository<Content, Long> {

    override fun findAll() : List<Content>

    fun findByName(name: String) : Optional<Content>
}