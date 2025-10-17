package com.example.memoscribe.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.memoscribe.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class NoteRepositoryTest {

    private lateinit var repository: NoteRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        repository = NoteRepository(context)
    }

    @Test
    fun testSaveAndLoad() = runBlocking {
        val note = Note(title = "Test", content = "Contenu")
        repository.saveNotes(listOf(note))

        val loadedNotes = repository.getNotes().first()
        assertEquals(1, loadedNotes.size)
        assertEquals("Test", loadedNotes[0].title)
    }
}