package com.example.memoscribe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.memoscribe.model.Note
import com.example.memoscribe.repository.NoteRepository
import com.example.memoscribe.ui.screens.AddEditNoteScreen
import com.example.memoscribe.ui.screens.NoteListScreen
import com.example.memoscribe.ui.theme.MemoScribeTheme
import com.example.memoscribe.viewmodel.NoteViewModel
import com.example.memoscribe.viewmodel.NoteViewModelFactory


/**
 * Activity principale de l'application MemoScribe
 *
 * Gère :
 * - La navigation entre écrans avec Navigation Compose
 * - Les données de test temporaires (avant implémentation du ViewModel)
 * - Le thème Material3
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Créer le Repository avec le contexte de l'application
        val repository = NoteRepository(applicationContext)

        enableEdgeToEdge()
        setContent {
            MemoScribeTheme {
                // Créer le ViewModel avec la Factory
                val viewModel: NoteViewModel = viewModel(
                    factory = NoteViewModelFactory(repository)
                )

                MemoScribeApp(viewModel = viewModel)
            }
        }
    }
}


/**
 * Point d'entrée principal de l'application
 * Configure la navigation et connecte le ViewModel à l'UI
 *
 * @param viewModel ViewModel principal de l'application
 */
@Composable
fun MemoScribeApp(
    viewModel: NoteViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Observer les états du ViewModel
    val notes by viewModel.notes
    val isSelectionMode by viewModel.isSelectionMode
    val selectedNoteIds by viewModel.selectedNoteIds

    NavHost(
        navController = navController,
        startDestination = "notes_list",
        modifier = Modifier.fillMaxSize()
    ) {
        // ===== ÉCRAN 1 : LISTE DES NOTES =====
        composable("notes_list") {
            NoteListScreen(
                notes = notes,
                isSelectionMode = isSelectionMode,
                selectedNoteIds = selectedNoteIds,
                onNoteClick = { noteId ->
                    // Navigation vers l'écran d'édition
                    navController.navigate("add_edit_note/$noteId")
                },
                onNoteLongClick = { noteId ->
                    // Activer le mode sélection
                    viewModel.enableSelectionMode(noteId)
                },
                onToggleNoteSelection = { noteId ->
                    // Toggle la sélection d'une note
                    viewModel.toggleNoteSelection(noteId)
                },
                onAddNoteClick = {
                    // Navigation vers l'écran d'ajout
                    navController.navigate("add_edit_note/null")
                },
                onCancelSelection = {
                    // Désactiver le mode sélection
                    viewModel.disableSelectionMode()
                },
                onDeleteSelected = {
                    // Supprimer les notes sélectionnées
                    val count = viewModel.selectedCount
                    val success = viewModel.deleteSelectedNotes()

                    if (success) {
                        Toast.makeText(
                            context,
                            "$count note(s) supprimée(s)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        // ===== ÉCRAN 2 : AJOUTER/ÉDITER UNE NOTE =====
        composable(
            route = "add_edit_note/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")

            // Récupérer la note si on est en mode édition
            val note = if (noteId != null && noteId != "null") {
                viewModel.getNoteById(noteId)
            } else {
                null
            }

            AddEditNoteScreen(
                noteId = noteId,
                note = note,
                onSaveClick = { title, content ->
                    // Validation et sauvegarde
                    val (isValid, errorMessage) = viewModel.validateNoteInput(title, content)

                    if (!isValid) {
                        // Afficher l'erreur de validation
                        Toast.makeText(
                            context,
                            errorMessage ?: "Erreur de validation",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@AddEditNoteScreen
                    }

                    // Sauvegarder (ajouter ou modifier)
                    val success = if (noteId != null && noteId != "null") {
                        // MODE ÉDITER
                        viewModel.updateNote(noteId, title, content)
                    } else {
                        // MODE AJOUTER
                        viewModel.addNote(title, content)
                    }

                    if (success) {
                        // Afficher confirmation
                        Toast.makeText(
                            context,
                            if (noteId != null && noteId != "null") {
                                context.getString(R.string.note_updated)
                            } else {
                                context.getString(R.string.note_added)
                            },
                            Toast.LENGTH_SHORT
                        ).show()

                        // Retour à la liste
                        navController.navigateUp()
                    } else {
                        // Erreur lors de la sauvegarde
                        Toast.makeText(
                            context,
                            "Erreur lors de la sauvegarde",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onDeleteClick = {
                    // Supprimer la note
                    if (noteId != null && noteId != "null") {
                        val success = viewModel.deleteNote(noteId)

                        if (success) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.note_deleted),
                                Toast.LENGTH_SHORT
                            ).show()

                            // Retour à la liste
                            navController.navigateUp()
                        }
                    }
                },
                onNavigateBack = {
                    // Retour sans sauvegarder
                    navController.navigateUp()
                }
            )
        }
    }
}