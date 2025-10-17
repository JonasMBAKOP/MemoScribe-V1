package com.example.memoscribe.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.memoscribe.model.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.util.Log

// Extension pour accéder à DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "notes_db")

/**
 * Repository responsable de la persistance des notes avec DataStore.
 * Utilise Gson pour la sérialisation/désérialisation JSON.
 */
class NoteRepository(private val context: Context) {

    private val gson = Gson()
    private val NOTES_KEY = stringPreferencesKey("notes_list")

    companion object {
        private const val TAG = "NoteRepository"
    }

    /**
     * Récupère les notes depuis DataStore sous forme de Flow.
     * Le Flow émet une nouvelle valeur chaque fois que les données changent.
     *
     * @return Flow<List<Note>> - Liste observable des notes
     */
    fun getNotes(): Flow<List<Note>> = context.dataStore.data.map { preferences ->
        val notesJson = preferences[NOTES_KEY] ?: return@map emptyList()
        try {
            // Désérialiser le JSON en List<Note>
            val type = object : TypeToken<List<Note>>() {}.type
            val notes: List<Note> = gson.fromJson(notesJson, type)

            Log.d(TAG, "Notes chargées: ${notes.size} note(s)")
            notes
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors du chargement des notes: ${e.message}")
            emptyList()
        }
    }

    /**
     * Sauvegarde les notes dans DataStore.
     * La fonction est suspend car l'écriture est asynchrone.
     *
     * @param notes Liste des notes à sauvegarder
     */
    suspend fun saveNotes(notes: List<Note>) {
        try {
            context.dataStore.edit { preferences ->
                // Sérialiser la liste en JSON
                val notesJson = gson.toJson(notes)
                preferences[NOTES_KEY] = notesJson

                Log.d(TAG, "Notes sauvegardées: ${notes.size} note(s)")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors de la sauvegarde des notes: ${e.message}")
            throw e
        }
    }

    /**
     * Efface toutes les notes du DataStore.
     * Utilisé pour le reset ou les tests.
     */
    suspend fun clearAllNotes() {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(NOTES_KEY)
                Log.d(TAG, "Toutes les notes ont été effacées")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors de l'effacement des notes: ${e.message}")
            throw e
        }
    }
}