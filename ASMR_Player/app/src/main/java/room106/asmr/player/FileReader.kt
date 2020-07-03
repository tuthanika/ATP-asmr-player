package room106.asmr.player

import android.content.Context
import java.io.*

class FileReader {

    private val FAVORITES_FILE_NAME = "favorites.json"

    fun readFavoriteMixesList(context: Context): String? {
        return read(context, FAVORITES_FILE_NAME)
    }

    fun rewriteFavoriteMixesList(context: Context, jsonString: String) {
        write(context, FAVORITES_FILE_NAME, jsonString)
    }

    private fun read(context: Context, fileName: String): String? {
        return try {
            val fis: FileInputStream = context.openFileInput(fileName)
            val isr = InputStreamReader(fis)
            val bufferedReader = BufferedReader(isr)
            val sb = StringBuilder()

            var line: String? = null
            while (bufferedReader.readLine().also { line = it } != null) {
                sb.append(line)
            }
            sb.toString()
        } catch (fileNotFound: FileNotFoundException) {
            null
        } catch (ioException: IOException) {
            null
        }
    }

    private fun write(
        context: Context,
        fileName: String,
        jsonString: String?
    ): Boolean {
        return try {
            val fos =
                context.openFileOutput(fileName, Context.MODE_PRIVATE)
            if (jsonString != null) {
                fos.write(jsonString.toByteArray())
            }
            fos.close()
            true
        } catch (fileNotFound: FileNotFoundException) {
            false
        } catch (ioException: IOException) {
            false
        }
    }
}