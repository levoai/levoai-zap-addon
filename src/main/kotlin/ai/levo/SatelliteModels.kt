package ai.levo

import com.google.gson.annotations.SerializedName

data class SatelliteHttpRequest(
    @SerializedName("headers") val headers: Map<String, String?>?,
    @SerializedName("body") val body: String?,
    @SerializedName("truncated") val truncated: Boolean?,
    @SerializedName("body_sha256_digest") val bodySha256Digest: String?,
    @SerializedName("body_original_length") val bodyOriginalLength: Int?
)

data class SatelliteHttpResponse(
    @SerializedName("headers") val headers: Map<String, String?>?,
    @SerializedName("body") val body: String?,
    @SerializedName("truncated") val truncated: Boolean?,
    @SerializedName("body_sha256_digest") val bodySha256Digest: String?,
    @SerializedName("body_original_length") val bodyOriginalLength: Int?
)

data class SatelliteNetwork(
    @SerializedName("hostname") val hostname: String?,
    @SerializedName("ip") val ip: String?,
    @SerializedName("port") val port: Int
)

data class SatelliteMessage(
    @SerializedName("http_scheme") val httpScheme: String?,
    @SerializedName("request") val request: SatelliteHttpRequest,
    @SerializedName("response") val response: SatelliteHttpResponse,
    @SerializedName("resource") val resource: Map<String, Any>,
    @SerializedName("duration_ns") val durationNs: Int?,
    @SerializedName("request_time_ns") val requestTimeNs: Long?,
    @SerializedName("trace_id") val traceId: String?,
    @SerializedName("parent_id") val parentId: String?,
    @SerializedName("span_id") val spanId: String?,
    @SerializedName("span_kind") val spanKind: String?,
    @SerializedName("local_net") val localNetwork: SatelliteNetwork?,
    @SerializedName("remote_net") val remoteNetwork: SatelliteNetwork?
)
