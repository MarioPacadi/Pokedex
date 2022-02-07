package hr.algebra.pokedex.handler

import android.content.Context
import android.util.Log
import hr.algebra.pokedex.factory.createGetHttpUrlConnection
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.nio.file.Files
import java.nio.file.Paths

fun downloadImageAndStore(context: Context, url: String, filename: String): String? {

    // Nebula
    //"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
    val ext = url.substring(url.lastIndexOf(".")) // .png
    val file: File = createFile(context, filename, ext)

    try {
        val con: HttpURLConnection = createGetHttpUrlConnection(url)
        Files.copy(con.inputStream, Paths.get(file.toURI()))
        return file.absolutePath
    } catch (e: Exception) {
        Log.e("DOWNLOAD IMAGE", e.message+" $url - $ext", e)
    }

    return null
}


fun createFile(context: Context, filename: String, ext: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, File.separator + filename + ext)
    if (file.exists()) {
        file.delete()
    }
    return file
}

fun getImageUrl(index : String): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
}
