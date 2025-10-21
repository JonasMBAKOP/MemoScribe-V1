package com.example.memoscribe.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memoscribe.model.Note
import com.example.memoscribe.repository.NoteRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


/**
 * ViewModel principal de l'application MemoScribe.
 * Gère tout l'état et la logique métier.
 *
 * Responsabilités :
 * - Gérer la liste des notes
 * - Opérations CRUD (Create, Read, Update, Delete)
 * - Mode sélection multiple
 * - Validation des entrées
 * - Persistance via Repository
 *
 * @param repository Repository pour la persistance DataStore
 */
class NoteViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    companion object {
        private const val TAG = "NoteViewModel"
        const val MAX_TITLE_LENGTH = 100
        const val MAX_CONTENT_LENGTH = 5000
    }

    // ==================== ÉTATS ====================

    /**
     * Liste de toutes les notes
     */
    private val _notes = mutableStateOf<List<Note>>(emptyList())
    val notes: State<List<Note>> = _notes

    /**
     * Indique si le mode sélection est actif
     */
    private val _isSelectionMode = mutableStateOf(false)
    val isSelectionMode: State<Boolean> = _isSelectionMode

    /**
     * Set des IDs des notes sélectionnées
     */
    private val _selectedNoteIds = mutableStateOf<Set<String>>(emptySet())
    val selectedNoteIds: State<Set<String>> = _selectedNoteIds

    /**
     * Nombre de notes sélectionnées
     */
    val selectedCount: Int
        get() = _selectedNoteIds.value.size

    // ==================== INITIALISATION ====================

    init {
        loadNotes()
    }

    // ==================== OPÉRATIONS CRUD ====================

    /**
     * Ajoute une nouvelle note.
     *
     * @param title Titre de la note
     * @param content Contenu de la note
     * @return true si l'ajout a réussi, false sinon
     */
    fun addNote(title: String, content: String): Boolean {
        // Validation
        val (isValid, errorMessage) = validateNoteInput(title, content)
        if (!isValid) {
            Log.w(TAG, "Validation échouée : $errorMessage")
            return false
        }

        return try {
            // Créer la nouvelle note
            val newNote = Note(
                title = title.trim(),
                content = content.trim()
            )

            // Ajouter à la liste
            _notes.value = _notes.value + newNote

            // Sauvegarder
            saveNotes()

            Log.d(TAG, "Note ajoutée : ${newNote.id}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors de l'ajout : ${e.message}")
            false
        }
    }

    /**
     * Met à jour une note existante.
     *
     * @param noteId ID de la note à modifier
     * @param newTitle Nouveau titre
     * @param newContent Nouveau contenu
     * @return true si la mise à jour a réussi, false sinon
     */
    fun updateNote(noteId: String, newTitle: String, newContent: String): Boolean {
        // Validation
        val (isValid, errorMessage) = validateNoteInput(newTitle, newContent)
        if (!isValid) {
            Log.w(TAG, "Validation échouée : $errorMessage")
            return false
        }

        return try {
            // Trouver et mettre à jour la note
            _notes.value = _notes.value.map { note ->
                if (note.id == noteId) {
                    note.copy(
                        title = newTitle.trim(),
                        content = newContent.trim(),
                        updatedTimestamp = System.currentTimeMillis()
                    )
                } else {
                    note
                }
            }

            // Sauvegarder
            saveNotes()

            Log.d(TAG, "Note mise à jour : $noteId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors de la mise à jour : ${e.message}")
            false
        }
    }

    /**
     * Supprime une note.
     *
     * @param noteId ID de la note à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    fun deleteNote(noteId: String): Boolean {
        return try {
            // Filtrer pour exclure la note
            _notes.value = _notes.value.filter { it.id != noteId }

            // Sauvegarder
            saveNotes()

            Log.d(TAG, "Note supprimée : $noteId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors de la suppression : ${e.message}")
            false
        }
    }

    /**
     * Récupère une note par son ID.
     *
     * @param noteId ID de la note recherchée
     * @return La note si trouvée, null sinon
     */
    fun getNoteById(noteId: String): Note? {
        return _notes.value.find { it.id == noteId }
    }

    // ==================== MODE SÉLECTION MULTIPLE ====================

    /**
     * Active le mode sélection et sélectionne automatiquement la note spécifiée.
     * Appelé lors d'un long-press sur une note.
     *
     * @param noteId ID de la note qui déclenche le mode sélection
     */
    fun enableSelectionMode(noteId: String) {
        _isSelectionMode.value = true
        _selectedNoteIds.value = setOf(noteId)
        Log.d(TAG, "Mode sélection activé avec note : $noteId")
    }

    /**
     * Toggle la sélection d'une note.
     * Si toutes les notes sont désélectionnées, désactive le mode sélection.
     *
     * @param noteId ID de la note à toggle
     */
    fun toggleNoteSelection(noteId: String) {
        val currentSelection = _selectedNoteIds.value.toMutableSet()

        if (noteId in currentSelection) {
            // Désélectionner
            currentSelection.remove(noteId)
            Log.d(TAG, "Note désélectionnée : $noteId")
        } else {
            // Sélectionner
            currentSelection.add(noteId)
            Log.d(TAG, "Note sélectionnée : $noteId")
        }

        _selectedNoteIds.value = currentSelection

        // Si plus aucune note sélectionnée, désactiver le mode
        if (currentSelection.isEmpty()) {
            disableSelectionMode()
        }
    }

    /**
     * Désactive le mode sélection et réinitialise la sélection.
     */
    fun disableSelectionMode() {
        _isSelectionMode.value = false
        _selectedNoteIds.value = emptySet()
        Log.d(TAG, "Mode sélection désactivé")
    }

    /**
     * Supprime toutes les notes sélectionnées.
     * Désactive automatiquement le mode sélection après suppression.
     *
     * @return true si la suppression a réussi, false sinon
     */
    fun deleteSelectedNotes(): Boolean {
        return try {
            val selectedIds = _selectedNoteIds.value
            val countBefore = _notes.value.size

            // Filtrer pour garder seulement les notes non sélectionnées
            _notes.value = _notes.value.filter { it.id !in selectedIds }

            val countAfter = _notes.value.size
            val deletedCount = countBefore - countAfter

            // Sauvegarder
            saveNotes()

            // Désactiver le mode sélection
            disableSelectionMode()

            Log.d(TAG, "Notes supprimées : $deletedCount")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erreur lors de la suppression multiple : ${e.message}")
            false
        }
    }

    /**
     * Sélectionne toutes les notes.
     * Utile pour un bouton "Tout sélectionner" (fonctionnalité bonus).
     */
    fun selectAllNotes() {
        if (_notes.value.isNotEmpty()) {
            _isSelectionMode.value = true
            _selectedNoteIds.value = _notes.value.map { it.id }.toSet()
            Log.d(TAG, "Toutes les notes sélectionnées : ${_notes.value.size}")
        }
    }

    // ==================== VALIDATION ====================

    /**
     * Valide les entrées utilisateur (titre et contenu).
     *
     * @param title Titre à valider
     * @param content Contenu à valider
     * @return Pair<Boolean, String?> - (isValid, errorMessage)
     */
    fun validateNoteInput(title: String, content: String): Pair<Boolean, String?> {
        return when {
            title.isBlank() -> Pair(false, "Le titre ne peut pas être vide")
            content.isBlank() -> Pair(false, "Le contenu ne peut pas être vide")
            title.length > MAX_TITLE_LENGTH -> Pair(false, "Le titre ne doit pas dépasser $MAX_TITLE_LENGTH caractères")
            content.length > MAX_CONTENT_LENGTH -> Pair(false, "Le contenu ne doit pas dépasser $MAX_CONTENT_LENGTH caractères")
            else -> Pair(true, null)
        }
    }

    // ==================== PERSISTANCE ====================

    /**
     * Charge les notes depuis le Repository (DataStore).
     * Appelé automatiquement lors de l'initialisation du ViewModel.
     */
    private fun loadNotes() {
        viewModelScope.launch {
            try {
                repository.getNotes().collect { loadedNotes ->
                    _notes.value = loadedNotes
                    Log.d(TAG, "Notes chargées : ${loadedNotes.size}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erreur lors du chargement : ${e.message}")
                _notes.value = emptyList()
            }
        }
    }

    /**
     * Sauvegarde les notes dans le Repository (DataStore).
     * Appelé automatiquement après chaque opération CRUD.
     */
    private fun saveNotes() {
        viewModelScope.launch {
            try {
                repository.saveNotes(_notes.value)
                Log.d(TAG, "Notes sauvegardées : ${_notes.value.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Erreur lors de la sauvegarde : ${e.message}")
            }
        }
    }

    /**
     * Efface toutes les notes (pour reset complet).
     * Utilisé pour les tests ou fonctionnalité de reset.
     */
    fun clearAllNotes() {
        viewModelScope.launch {
            try {
                _notes.value = emptyList()
                repository.saveNotes(emptyList())
                disableSelectionMode()
                Log.d(TAG, "Toutes les notes effacées")
            } catch (e: Exception) {
                Log.e(TAG, "Erreur lors de l'effacement : ${e.message}")
            }
        }
    }
}