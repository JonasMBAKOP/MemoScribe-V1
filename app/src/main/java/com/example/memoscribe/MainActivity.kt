package com.example.memoscribe

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.memoscribe.model.Note
import com.example.memoscribe.ui.screens.AddEditNoteScreen
import com.example.memoscribe.ui.screens.NoteListScreen
import com.example.memoscribe.ui.theme.MemoScribeTheme


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
        enableEdgeToEdge()
        setContent {
            MemoScribeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )

                    MemoScribeApp()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

/**
 * Point d'entrée principal de l'application
 * Configure la navigation et gère l'état temporaire
 */
@Composable
fun MemoScribeApp() {
    val navController = rememberNavController()

    // ===== DONNÉES DE TEST (TEMPORAIRE) =====
    // Sera remplacé par le ViewModel en ÉTAPE 3
    var testNotes by remember {
        mutableStateOf(
            listOf(
                Note(
                    id = "1",
                    title = "Courses de la semaine",
                    content = "Acheter du lait, des œufs, du pain, du fromage et des fruits pour toute la semaine."
                ),
                Note(
                    id = "2",
                    title = "Réunion projet",
                    content = "Points à discuter : budget, planning, équipe, objectifs, deadlines et prochaines étapes."
                ),
                Note(
                    id = "3",
                    title = "Idées vacances",
                    content = "Destinations possibles : Bali, Tokyo, New York, Paris, Rome, Londres, Barcelone."
                )
            )
        )
    }

    // État pour le mode sélection (TEMPORAIRE)
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedNoteIds by remember { mutableStateOf(setOf<String>()) }

    NavHost(
        navController = navController,
        startDestination = "notes_list",
        modifier = Modifier.fillMaxSize()
    ) {
        // ===== ÉCRAN 1 : LISTE DES NOTES =====
        composable("notes_list") {
            NoteListScreen(
                notes = testNotes,
                isSelectionMode = isSelectionMode,
                selectedNoteIds = selectedNoteIds,
                onNoteClick = { noteId ->
                    // Navigation vers l'écran d'édition
                    navController.navigate("add_edit_note/$noteId")
                },
                onNoteLongClick = { noteId ->
                    // Activer le mode sélection
                    isSelectionMode = true
                    selectedNoteIds = setOf(noteId)
                },
                onToggleNoteSelection = { noteId ->
                    // Toggle la sélection
                    selectedNoteIds = if (noteId in selectedNoteIds) {
                        selectedNoteIds - noteId
                    } else {
                        selectedNoteIds + noteId
                    }

                    // Si plus aucune note sélectionnée, désactiver le mode
                    if (selectedNoteIds.isEmpty()) {
                        isSelectionMode = false
                    }
                },
                onAddNoteClick = {
                    // Navigation vers l'écran d'ajout
                    navController.navigate("add_edit_note/null")
                },
                onCancelSelection = {
                    // Désactiver le mode sélection
                    isSelectionMode = false
                    selectedNoteIds = emptySet()
                },
                onDeleteSelected = {
                    // Supprimer les notes sélectionnées (TEMPORAIRE)
                    testNotes = testNotes.filter { it.id !in selectedNoteIds }
                    isSelectionMode = false
                    selectedNoteIds = emptySet()
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
                testNotes.find { it.id == noteId }
            } else {
                null
            }

            AddEditNoteScreen(
                noteId = noteId,
                note = note,
                onSaveClick = { title, content ->
                    if (noteId != null && noteId != "null") {
                        // MODE ÉDITER : Mettre à jour la note existante
                        testNotes = testNotes.map { existingNote ->
                            if (existingNote.id == noteId) {
                                existingNote.copy(
                                    title = title,
                                    content = content,
                                    updatedTimestamp = System.currentTimeMillis()
                                )
                            } else {
                                existingNote
                            }
                        }
                    } else {
                        // MODE AJOUTER : Créer une nouvelle note
                        val newNote = Note(
                            title = title,
                            content = content
                        )
                        testNotes = testNotes + newNote
                    }

                    // Retour à la liste
                    navController.navigateUp()
                },
                onDeleteClick = {
                    // Supprimer la note (TEMPORAIRE)
                    if (noteId != null) {
                        testNotes = testNotes.filter { it.id != noteId }
                    }

                    // Retour à la liste
                    navController.navigateUp()
                },
                onNavigateBack = {
                    // Retour sans sauvegarder
                    navController.navigateUp()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MemoScribeTheme {
        Greeting("Android")
    }
}

/**
 * Preview de l'application complète
 * Note : Les previews de navigation sont limitées, mieux vaut tester sur émulateur
 */
@Preview(showBackground = true)
@Composable
fun MemoScribeAppPreview() {
    MemoScribeTheme {
        MemoScribeApp()
    }
}
