package hr.hofman.customgooglesearch.data.utils

import java.io.IOException
import java.io.InputStreamReader

object FileReader {
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = javaClass.classLoader!!.getResource(fileName).openStream()
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach { line ->
                builder.append(line)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}
