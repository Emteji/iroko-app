package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.HttpHeaders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendCoreRepository @Inject constructor() {
    private val client = HttpClient(OkHttp)
    private val baseUrl = BuildConfig.BACKEND_CORE_URL

    private fun api(path: String): String {
        return if (baseUrl.endsWith("/api")) "$baseUrl$path" else "$baseUrl/api$path"
    }

    suspend fun health(): Boolean {
        return try {
            val res = client.post(api("/health"))
            res.bodyAsText().contains("ok")
        } catch (e: Exception) {
            false
        }
    }

    suspend fun completeTask(childId: String, taskId: String, deviceId: String, proofUrl: String?): Result<Unit> {
        return try {
            val res = client.post(api("/child/$childId/task-complete")) {
                contentType(ContentType.Application.Json)
                headers { append("X-Device-ID", deviceId) }
                setBody(mapOf(
                    "task_id" to taskId,
                    "proof_url" to proofUrl
                ))
            }
            if (res.status.value in 200..299) Result.success(Unit) else Result.failure(Exception("${res.status.value}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateGuidanceForChild(childId: String, payload: Map<String, Any> = emptyMap()): Result<String> {
        return try {
            val body = payload + mapOf("child_id" to childId)
            val res = client.post(api("/ai/guidance")) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            val text = res.bodyAsText()
            if (res.status.value in 200..299) Result.success(text) else Result.failure(Exception(text))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun startChildSession(childId: String, deviceId: String, sessionEnd: String?, bearerToken: String): Result<Unit> {
        return try {
            val res = client.post(api("/parent/session/start")) {
                contentType(ContentType.Application.Json)
                headers { append(HttpHeaders.Authorization, "Bearer $bearerToken") }
                setBody(mapOf(
                    "child_id" to childId,
                    "device_id" to deviceId,
                    "session_end" to sessionEnd
                ))
            }
            if (res.status.value in 200..299) Result.success(Unit) else Result.failure(Exception(res.bodyAsText()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun stopChildSession(childId: String, bearerToken: String): Result<Unit> {
        return try {
            val res = client.post(api("/parent/session/stop")) {
                contentType(ContentType.Application.Json)
                headers { append(HttpHeaders.Authorization, "Bearer $bearerToken") }
                setBody(mapOf("child_id" to childId))
            }
            if (res.status.value in 200..299) Result.success(Unit) else Result.failure(Exception(res.bodyAsText()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateAIResponse(prompt: String): Result<String> {
        return try {
            val res = client.post(api("/ai/generate")) {
                contentType(ContentType.Application.Json)
                setBody(mapOf("prompt" to prompt))
            }
            val text = res.bodyAsText()
            // Expecting {"text": "..."}
            // Simple string extraction to avoid dependency issues if JSON parser not set up on response
            val content = if (text.contains("\"text\":")) {
                text.substringAfter("\"text\":").substringAfter("\"").substringBeforeLast("\"")
            } else {
                text
            }
            
            if (res.status.value in 200..299) Result.success(content) else Result.failure(Exception(text))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
