package de.thm.arsnova.service.httpgateway.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.temporal.TemporalAmount

@Component
@ConfigurationProperties
class HttpGatewayProperties {
    var security: Security? = null
    var httpClient: HttpClient? = null
    var routing: Routing? = null
}
data class Security (
    var jwt: Jwt? = null
)
data class Jwt (
    var publicSecret: String? = "",
    var internalSecret: String? = "",
    var serverId: String? = "",
    var validityPeriod: TemporalAmount? = Duration.ofMinutes(5)
)
data class HttpClient (
        var authService: String = "",
        var commentService: String = "",
        var core: String = ""
)

data class Routing (
        var endpoints: Endpoints? = null
)
data class Endpoints (
        var core: String = "",
        var commentService: String = "",
        var roomaccessService: String = "",
        var formattingService: String = ""
)
