package com.example.memoscribe.model

import com.example.memoscribe.model.Note
import org.junit.Test
import org.junit.Assert.*

/**
 * Tests unitaires pour la classe Note
 */
class NoteTest {

    @Test
    fun testNoteCreation() {
        val note = Note(title = "Test", content = "Contenu test")
        assertNotNull(note.id)
        assertEquals("Test", note.title)
        assertEquals("Contenu test", note.content)
        assertTrue(note.timestamp > 0)
        assertTrue(note.updatedTimestamp > 0)
    }

    @Test
    fun testNoteTimestamps() {
        val beforeCreation = System.currentTimeMillis()
        val note = Note(title = "Test", content = "Contenu")
        val afterCreation = System.currentTimeMillis()

        assertTrue(note.timestamp in beforeCreation..afterCreation)
        assertTrue(note.updatedTimestamp in beforeCreation..afterCreation)
    }

    @Test
    fun testGetContentPreview() {
        // Contenu court (moins de 50 caractères)
        val shortNote = Note(title = "Test", content = "Court")
        assertEquals("Court", shortNote.getContentPreview())

        // Contenu long (plus de 50 caractères)
        val longContent = "a".repeat(100)
        val longNote = Note(title = "Test", content = longContent)
        val preview = longNote.getContentPreview()

        assertTrue(preview.length <= 53) // 50 + "..."
        assertTrue(preview.endsWith("..."))
    }

    @Test
    fun testGetFormattedDates() {
        val note = Note(title = "Test", content = "Contenu")
        val createdDate = note.getFormattedCreatedDate()
        val updatedDate = note.getFormattedUpdatedDate()

        assertTrue(createdDate.isNotEmpty())
        assertTrue(updatedDate.isNotEmpty()) 
        assertTrue(createdDate.contains("/"))
        assertTrue(updatedDate.contains("/"))
        assertTrue(createdDate.contains(":"))
        assertTrue(updatedDate.contains(":"))
    }

    @Test
    fun testNoteCopy() {
        val originalNote = Note(
            title = "Titre original",
            content = "Contenu original"
        )

        // Simuler une modification
        Thread.sleep(10) // Attendre un peu pour que le timestamp change
        val updatedNote = originalNote.copy(
            title = "Nouveau titre",
            content = "Nouveau contenu",
            updatedTimestamp = System.currentTimeMillis()
        )

        // Vérifier que l'ID reste le même
        assertEquals(originalNote.id, updatedNote.id)

        // Vérifier que le timestamp de création reste le même
        assertEquals(originalNote.timestamp, updatedNote.timestamp)

        // Vérifier que le contenu a changé
        assertEquals("Nouveau titre", updatedNote.title)
        assertEquals("Nouveau contenu", updatedNote.content)

        // Vérifier que le timestamp de modification a changé
        assertTrue(updatedNote.updatedTimestamp > originalNote.updatedTimestamp)
    }
}