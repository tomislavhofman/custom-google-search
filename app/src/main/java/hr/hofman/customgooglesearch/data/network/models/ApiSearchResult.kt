package hr.hofman.customgooglesearch.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSearchResult(
    val kind: String,
    val url: Url,
    val queries: Queries,
    val context: Context,
    val searchInformation: SearchInformation,
    val items: List<Item>
) {
    @JsonClass(generateAdapter = true)
    data class Url(val type: String, val template: String)

    @JsonClass(generateAdapter = true)
    data class Queries(val request: Array<Request>, val nextPage: Array<Request>) {
        @JsonClass(generateAdapter = true)
        data class Request(
            val title: String,
            val totalResults: String,
            val searchTerms: String,
            val count: Int,
            val startIndex: Int,
            val inputEncoding: String,
            val outputEncoding: String,
            val safe: String,
            val cx: String
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Queries

            if (!request.contentEquals(other.request)) return false
            if (!nextPage.contentEquals(other.nextPage)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = request.contentHashCode()
            result = 31 * result + nextPage.contentHashCode()
            return result
        }
    }

    @JsonClass(generateAdapter = true)
    data class Context(val title: String)

    @JsonClass(generateAdapter = true)
    data class SearchInformation(
        val searchTime: Float,
        val formattedSearchTime: String,
        val totalResults: String,
        val formattedTotalResults: String
    )

    @JsonClass(generateAdapter = true)
    data class Item(
        val kind: String,
        val title: String,
        val htmlTitle: String,
        val link: String,
        val displayLink: String,
        val snippet: String,
        val htmlSnippet: String,
        val cacheId: String,
        val formattedUrl: String,
        val htmlFormattedUrl: String,
        val pagemap: PageMap
    ) {
        @JsonClass(generateAdapter = true)
        data class PageMap(
            @Json(name = "cse_thumbnail") val cseThumbnail: Array<CseThumbnail>?,
            val metatags: Array<Metatags>,
            @Json(name = "cse_image") val cseImage: Array<CseImage>?
        ) {
            @JsonClass(generateAdapter = true)
            data class CseThumbnail(val src: String, val width: String, val height: String)

            @JsonClass(generateAdapter = true)
            data class Metatags(val referrer: String, @Json(name = "og:image") val ogImage: String?)

            @JsonClass(generateAdapter = true)
            data class CseImage(val src: String)

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as PageMap

                if (cseThumbnail != null) {
                    if (other.cseThumbnail == null) return false
                    if (!cseThumbnail.contentEquals(other.cseThumbnail)) return false
                } else if (other.cseThumbnail != null) return false
                if (!metatags.contentEquals(other.metatags)) return false
                if (cseImage != null) {
                    if (other.cseImage == null) return false
                    if (!cseImage.contentEquals(other.cseImage)) return false
                } else if (other.cseImage != null) return false

                return true
            }

            override fun hashCode(): Int {
                var result = cseThumbnail?.contentHashCode() ?: 0
                result = 31 * result + metatags.contentHashCode()
                result = 31 * result + (cseImage?.contentHashCode() ?: 0)
                return result
            }
        }
    }
}
