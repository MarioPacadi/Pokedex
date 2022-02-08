package hr.algebra.pokedex

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.pokedex.dao.PokedexRepository
import hr.algebra.pokedex.dao.getPokemonRepository
import hr.algebra.pokedex.model.Pokemon
import java.lang.IllegalArgumentException

private const val AUTHORITY = "hr.algebra.pokedex.api.provider"
private const val PATH = "pokemons"
const val ITEMS = 151
private const val ITEM_ID = 20

val POKEDEX_PROVIDER_URI = Uri.parse("content://$AUTHORITY/$PATH")!!

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, ITEMS)
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)
    this
}

class PokedexProvider : ContentProvider() {

    private lateinit var pokedexRepository: PokedexRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return pokedexRepository.delete(selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return pokedexRepository.delete("${Pokemon::_id.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = pokedexRepository.insert(values)
        return ContentUris.withAppendedId(POKEDEX_PROVIDER_URI, id)
    }

    override fun onCreate(): Boolean {
        pokedexRepository = getPokemonRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor?  = pokedexRepository.query(projection, selection, selectionArgs, sortOrder)

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return pokedexRepository.update(values, selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return pokedexRepository.update(values, "${Pokemon::_id.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }
}