/*
 * Copyright 2022 Levo.ai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

data class SatelliteResource(
    @SerializedName("host_name") val hostName: String?,
    @SerializedName("levo_env") val environment: String?,
    @SerializedName("sensor_type") val sensorType: String?,
    @SerializedName("sensor_version") val sensorVersion: String?
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
    @SerializedName("resource") val resource: SatelliteResource,
    @SerializedName("duration_ns") val durationNs: Int?,
    @SerializedName("request_time_ns") val requestTimeNs: Long?,
    @SerializedName("trace_id") val traceId: String?,
    @SerializedName("parent_id") val parentId: String?,
    @SerializedName("span_id") val spanId: String?,
    @SerializedName("span_kind") val spanKind: String?,
    @SerializedName("local_net") val localNetwork: SatelliteNetwork?,
    @SerializedName("remote_net") val remoteNetwork: SatelliteNetwork?
)
