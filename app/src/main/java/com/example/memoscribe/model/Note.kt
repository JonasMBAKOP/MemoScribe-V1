package com.example.memoscribe.model

import java.util.UUID
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modèle représentant une note dans l'application MemoScribe.
 *
 * @property id Identifiant unique généré automatiquement (UUID)
 * @property title Titre de la note (obligatoire)
 * @property content Contenu de la note (obligatoire)
 * @property timestamp Horodatage de CRÉATION (en millisecondes)
 * @property updatedTimestamp Horodatage de DERNIÈRE MODIFICATION (en millisecondes)
 *
 * Exemple d'utilisation:
 * val note = Note(
 *     title = "Ma première note",
 *     content = "Voici le contenu de ma note"
 * )
 *
 * // Éditer une note existante
 * val updatedNote = note.copy(
 *     title = "Nouveau titre",
 *     content = "Nouveau contenu",
 *     updatedTimestamp = System.currentTimeMillis()
 * )
 */

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp : Long = System.currentTimeMillis()
) {

    /**
     * Retourne la date de création formatée au format "dd/MM/yyyy HH:mm"
     */
    fun getFormattedCreatedDate(): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }

    /**
     * Retourne la date de modification formatée au format "dd/MM/yyyy HH:mm"
     */
    fun getFormattedUpdatedDate(): String {
        val date = Date(updatedTimestamp)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }

    // Fonction pour obtenir un aperçu du contenu (les 50 premiers caractères)
    fun getContentPreview(): String {
        return if (content.length > 50) {
            content.substring(0, 50) + "..."
        } else {
            content
        }
    }
}