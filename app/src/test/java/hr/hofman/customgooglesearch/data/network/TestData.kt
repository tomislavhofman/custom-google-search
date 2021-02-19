package hr.hofman.customgooglesearch.data.network

import com.squareup.moshi.Moshi
import hr.hofman.customgooglesearch.data.network.models.ApiSearchResult
import hr.hofman.customgooglesearch.data.utils.FileReader
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

object TestData {
    private val SUCCESS_RESPONSE_FILE_NAME = "success_response.json"
    private val MISSING_KEY_FILE_NAME = "missing_key_response.json"
    private val INVALID_CX_FILE_NAME = "invalid_cx_response.json"
    private val INVALID_KEY_FILE_NAME = "invalid_key_response.json"

    private val moshi = Moshi.Builder().build()
    private val jsonAdapter = moshi.adapter(ApiSearchResult::class.java)

    val successResponseJson = FileReader.readStringFromFile(SUCCESS_RESPONSE_FILE_NAME)

    val successResponse: Response<ApiSearchResult>? = Response.success(
        jsonAdapter.fromJson(
            successResponseJson
        )
    )

    val missingKeyResponseJson = FileReader.readStringFromFile(MISSING_KEY_FILE_NAME)

    val missingKeyResponse: Response<ApiSearchResult>? =
        Response.error(400, missingKeyResponseJson.toResponseBody())

    val invalidCxResponseJson = FileReader.readStringFromFile(INVALID_CX_FILE_NAME)

    val invalidCxResponse: Response<ApiSearchResult>? =
        Response.error(400, invalidCxResponseJson.toResponseBody())

    val invalidKeyResponseJson = FileReader.readStringFromFile(INVALID_KEY_FILE_NAME)

    val invalidKeyResponse: Response<ApiSearchResult>? =
        Response.error(400, invalidKeyResponseJson.toResponseBody())
}
