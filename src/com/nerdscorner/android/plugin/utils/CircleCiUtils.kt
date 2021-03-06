package com.nerdscorner.android.plugin.utils

import com.google.gson.JsonParser
import com.intellij.ide.util.PropertiesComponent
import com.squareup.okhttp.Callback
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import com.squareup.okhttp.Response
import java.io.IOException

object CircleCiUtils {
    private const val BASE_URL = "https://circleci.com/api/v2"
    private const val RERUN_WORKFLOW_URL = "$BASE_URL/workflow/{id}/rerun"
    private const val APPLICATION_JSON = "application/json"
    private const val CIRCLE_TOKEN = "Circle-Token"
    private const val WORKFLOW_ID_KEY = "workflow-id"

    private val client = OkHttpClient()

    private fun extractWorkflowId(externalId: String) = JsonParser()
            .parse(externalId)
            .asJsonObject
            .getAsJsonPrimitive(WORKFLOW_ID_KEY)
            .asString

    fun reRunWorkflow(
            externalId: String,
            success: () -> Unit = {},
            fail: (String?) -> Unit = {}
    ) {
        val propertiesComponent = PropertiesComponent.getInstance()
        val circleToken = propertiesComponent.getValue(Strings.CIRCLE_CI_TOKEN_PROPERTY, Strings.BLANK)
        if (circleToken.isEmpty()) {
            fail("Circle CI token not set")
        }
        val rerunUrl = RERUN_WORKFLOW_URL.replace("{id}", extractWorkflowId(externalId) ?: return)
        val rerunWorkflowRequest = Request
                .Builder()
                .url(rerunUrl)
                .post(RequestBody.create(MediaType.parse(APPLICATION_JSON), Strings.BLANK))
                .header(CIRCLE_TOKEN, circleToken)
                .build()
        client
                .newCall(rerunWorkflowRequest)
                .enqueue(object : Callback {
                    override fun onResponse(response: Response?) {
                        if (response?.isSuccessful == true) {
                            success()
                        } else {
                            fail(response?.body()?.toString())
                        }
                    }

                    override fun onFailure(request: Request?, exception: IOException?) {
                        fail(exception?.message)
                    }
                })
    }
}