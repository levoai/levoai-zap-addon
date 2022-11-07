package ai.levo

import com.google.gson.Gson
import org.apache.commons.httpclient.URI
import org.parosproxy.paros.network.HttpHeader
import org.parosproxy.paros.network.HttpMessage
import org.parosproxy.paros.network.HttpRequestHeader
import org.parosproxy.paros.network.HttpSender
import org.zaproxy.zap.network.HttpSenderListener
import java.util.Base64
import java.util.UUID

private val CONTENT_TYPES_SUPPORTED_MAP: Map<String, Boolean> = mapOf(
    // Parsable Formats
    "application/json" to true,
    "application/ld+json" to true,
    "text/json" to true,
    "application/x-www-form-urlencoded" to true,
    "multipart/form-data" to true,
    "application/xml" to true,
    "text/xml" to true,
    "text/plain" to true,
    // Additional Text Formats
    "application/javascript" to false,
    "text/javascript" to false,
    "text/html" to false,
    "text/css" to false,
    "text/csv" to false,
    "text/calendar" to false,
    // Images
    "image/avif" to false,
    "image/bmp" to false,
    "image/gif" to false,
    "image/vnd.microsoft.icon" to false,
    "image/jpeg" to false,
    "image/png" to false,
    "image/svg+xml" to false,
    "image/tiff" to false,
    "image/webp" to false,
    // Videos
    "video/x-msvideo" to false,
    "video/mp4" to false,
    "video/mpeg" to false,
    "video/ogg" to false,
    "video/mp2t" to false,
    "video/webm" to false,
    "video/3gpp" to false,
    "video/3gpp2" to false,
    // Audio
    "audio/3gpp" to false,
    "audio/3gpp2" to false,
    "audio/aac" to false,
    "audio/midi" to false,
    "audio/x-midi" to false,
    "audio/mpeg" to false,
    "audio/ogg" to false,
    "audio/opus" to false,
    "application/x-cdf" to false,
    "audio/wav" to false,
    "audio/webm" to false,
    // Other Binary Formats
    "application/x-abiword" to false,
    "application/x-freearc" to false,
    "application/vnd.amazon.ebook" to false,
    "application/octet-stream" to false,
    "application/x-bzip" to false,
    "application/x-bzip2" to false,
    "application/x-csh" to false,
    "application/msword" to false,
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to false,
    "application/vnd.ms-fontobject" to false,
    "application/epub+zip" to false,
    "application/gzip" to false,
    "application/java-archive" to false,
    "application/vnd.apple.installer+xml" to false,
    "application/vnd.oasis.opendocument.presentation" to false,
    "application/vnd.oasis.opendocument.spreadsheet" to false,
    "application/vnd.oasis.opendocument.text" to false,
    "application/ogg" to false,
    "application/pdf" to false,
    "application/x-httpd-php" to false,
    "application/vnd.ms-powerpoint" to false,
    "application/vnd.openxmlformats-officedocument.presentationml.presentation" to false,
    "application/vnd.rar" to false,
    "application/rtf" to false,
    "application/x-sh" to false,
    "application/x-shockwave-flash" to false,
    "application/x-tar" to false,
    "application/vnd.visio" to false,
    "application/xhtml+xml" to false,
    "application/vnd.ms-excel" to false,
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" to false,
    "application/vnd.mozilla.xul+xml" to false,
    "application/zip" to false,
    "application/x-7z-compressed" to false,
    "application/x-zip-compressed" to false,
    "font/otf" to false,
    "font/ttf" to false,
    "font/woff" to false,
    "font/woff2" to false
)

// Use a non-existent initiator value
private const val HTTP_SENDER_LEVO_INITIATOR = 1350

class LevoHttpSenderListener(private val extensionLevo: ExtensionLevo) : HttpSenderListener {

    private val gson by lazy { Gson() }

    override fun getListenerOrder(): Int = Int.MAX_VALUE - 1

    override fun onHttpRequestSend(msg: HttpMessage?, initiator: Int, sender: HttpSender?) {}

    override fun onHttpResponseReceive(msg: HttpMessage?, initiator: Int, sender: HttpSender?) {
        if (!extensionLevo.param.enabled
            || initiator == HTTP_SENDER_LEVO_INITIATOR
        ) {
            return
        }

        val requestContentType: String = msg?.requestHeader?.normalisedContentTypeValue ?: ""
        val isSupportedContentType: Boolean = CONTENT_TYPES_SUPPORTED_MAP[requestContentType] ?: true
        if (!isSupportedContentType) {
            return
        }

        val requestHeaders: MutableMap<String, String?> = mutableMapOf(
            ":authority" to (msg?.requestHeader?.uri?.authority
                ?: msg?.requestHeader?.getHeader(HttpRequestHeader.HOST)),
            ":method" to msg?.requestHeader?.method,
            ":path" to msg?.requestHeader?.uri?.path
        )
        for (header in msg?.requestHeader?.headers ?: listOf()) {
            requestHeaders[header.name] = header.value
        }
        val request = SatelliteHttpRequest(
            headers = requestHeaders,
            body = Base64.getEncoder().encodeToString(msg?.requestBody?.bytes ?: ByteArray(0)),
            truncated = false,
            bodySha256Digest = null,
            bodyOriginalLength = msg?.requestBody?.length() ?: 0
        )

        val responseHeaders: MutableMap<String, String?> = mutableMapOf(
            ":status" to msg?.responseHeader?.statusCode.toString()
        )
        for (header in msg?.responseHeader?.headers ?: listOf()) {
            responseHeaders[header.name] = header.value
        }
        val response = SatelliteHttpResponse(
            headers = responseHeaders,
            body = Base64.getEncoder().encodeToString(msg?.responseBody?.bytes ?: ByteArray(0)),
            truncated = false,
            bodySha256Digest = null,
            bodyOriginalLength = msg?.responseBody?.length() ?: 0
        )

        val satelliteMessage = SatelliteMessage(
            httpScheme = msg?.requestHeader?.uri?.scheme ?: "http",
            request = request,
            response = response,
            resource = mapOf(),
            durationNs = msg?.timeElapsedMillis?.times(1000000) ?: 0,
            requestTimeNs = msg?.timeSentMillis?.times(1000000) ?: 0,
            traceId = UUID.randomUUID().toString(),
            parentId = null,
            spanId = UUID.randomUUID().toString(),
            spanKind = "server", // TODO: Should this be client?
            localNetwork = null,
            remoteNetwork = null
        )

        val json = gson.toJson(satelliteMessage)

        val httpRequestHeader = HttpRequestHeader(
            HttpRequestHeader.POST,
            URI("${extensionLevo.param.satelliteUrl}/1.0/ebpf/traces", true),
            HttpHeader.HTTP11
        )
        httpRequestHeader.setHeader(HttpHeader.CONTENT_TYPE, HttpHeader.JSON_CONTENT_TYPE)
        httpRequestHeader.setHeader(HttpHeader.CONTENT_LENGTH, json.length.toString())
        val httpMessage = HttpMessage(httpRequestHeader)
        httpMessage.requestBody.setBody(json)

        HttpSender(HTTP_SENDER_LEVO_INITIATOR).sendAndReceive(httpMessage)
    }
}