package pl.integrable.dusterserver.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "credentials")
class CredentialsProperties {
    var adminPassword: String = ""
    var tokenSecret: String = ""
}