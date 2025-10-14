package com.example.memoscribe.viewmodel

class NoteViewModel : ViewModel() {
    private val _notes = mutableStateOf<List<Note>>(emptyList())
    val notes: State<List<Note>> = _notes

    init {
        loadNotesFromDataStore() // Charger au démarrage
    }

    fun addNote(title: String, content: String) {
        val newNote = Note(title = title, content = content)
        _notes.value = _notes.value + newNote
        saveNotesToDataStore()
    }

    fun deleteNote(noteId: String) {
        _notes.value = _notes.value.filter { it.id != noteId }
        saveNotesToDataStore()
    }

    private fun loadNotesFromDataStore() {
        // Logique de chargement (DataStore/SharedPreferences)
    }

    private fun saveNotesToDataStore() {
        // Logique de sauvegarde
    }
}