package pl.integrable.dusterserver.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity(name="db_content")
class Content(
        var name: String,
        var content: String,
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
) {
}