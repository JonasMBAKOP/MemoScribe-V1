package com.example.memoscribe.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.memoscribe.R
import com.example.memoscribe.model.Note
import com.example.memoscribe.ui.components.NoteCard
import com.example.memoscribe.ui.theme.MemoScribeTheme
import com.example.memoscribe.ui.theme.Error

/**
 * Écran principal affichant la liste de toutes les notes.
 *
 * Fonctionnalités :
 * - Affiche les notes dans une LazyColumn scrollable
 * - FloatingActionButton pour ajouter une note
 * - Mode sélection multiple (long-press)
 * - Suppression multiple avec confirmation
 * - Empty state quand aucune note
 *
 * @param notes Liste des notes à afficher
 * @param isSelectionMode Indique si le mode sélection est actif
 * @param selectedNoteIds Set des IDs des notes sélectionnées
 * @param onNoteClick Callback clic sur une note (édition)
 * @param onNoteLongClick Callback long-press (active sélection)
 * @param onToggleNoteSelection Callback toggle sélection d'une note
 * @param onAddNoteClick Callback clic sur FAB (ajout)
 * @param onCancelSelection Callback annulation du mode sélection
 * @param onDeleteSelected Callback suppression des notes sélectionnées
 * @param modifier Modificateur optionnel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    notes: List<Note>,
    isSelectionMode: Boolean = false,
    selectedNoteIds: Set<String> = emptySet(),
    onNoteClick: (String) -> Unit,
    onNoteLongClick: (String) -> Unit,
    onToggleNoteSelection: (String) -> Unit = {},
    onAddNoteClick: () -> Unit,
    onCancelSelection: () -> Unit = {},
    onDeleteSelected: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val selectedCount = selectedNoteIds.size

    // Gérer le bouton retour système en mode sélection
    BackHandler(enabled = isSelectionMode) {
        onCancelSelection()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isSelectionMode) {
                            "$selectedCount sélectionnée(s)"
                        } else {
                            stringResource(R.string.title_notes_list)
                        }
                    )
                },
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = onCancelSelection) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Annuler sélection"
                            )
                        }
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Supprimer les notes sélectionnées"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSelectionMode) Error else MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            // FAB caché en mode sélection
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = onAddNoteClick,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.button_add_note),
                        tint = Color.White
                    )
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->

        // Contenu principal
        if (notes.isEmpty()) {
            // État vide
            EmptyNotesList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            // Liste des notes
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(
                    items = notes,
                    key = { note -> note.id }
                ) { note ->
                    NoteCard(
                        note = note,
                        isSelectionMode = isSelectionMode,
                        isSelected = note.id in selectedNoteIds,
                        onClick = {
                            if (isSelectionMode) {
                                onToggleNoteSelection(note.id)
                            } else {
                                onNoteClick(note.id)
                            }
                        },
                        onLongClick = {
                            if (!isSelectionMode) {
                                onNoteLongClick(note.id)
                            }
                        }
                    )
                }
            }
        }
    }

    // Dialog de confirmation de suppression
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            noteCount = selectedCount,
            onConfirm = {
                showDeleteDialog = false
                onDeleteSelected()
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}

/**
 * Composant affichant l'état vide (aucune note)
 */
@Composable
private fun EmptyNotesList(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "📝",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.empty_notes_message),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

/**
 * Dialog de confirmation avant suppression multiple
 */
@Composable
private fun DeleteConfirmationDialog(
    noteCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Supprimer les notes ?")
        },
        text = {
            Text(
                text = "Êtes-vous sûr de vouloir supprimer $noteCount note(s) ? " +
                        "Cette action est irréversible."
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(R.string.button_delete),
                    color = Error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.button_cancel))
            }
        }
    )
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true, name = "Avec Notes")
@Composable
fun NoteListScreenPreview() {
    MemoScribeTheme {
        NoteListScreen(
            notes = listOf(
                Note(
                    title = "Courses de la semaine",
                    content = "Acheter du lait, des œufs, du pain, du fromage et des fruits pour toute la semaine."
                ),
                Note(
                    title = "Réunion projet",
                    content = "Points à discuter : budget, planning, équipe, objectifs, deadlines..."
                ),
                Note(
                    title = "Idées vacances",
                    content = "Destinations possibles : Bali, Tokyo, New York, Paris, Rome, Londres..."
                )
            ),
            onNoteClick = {},
            onNoteLongClick = {},
            onAddNoteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "État Vide")
@Composable
fun NoteListScreenEmptyPreview() {
    MemoScribeTheme {
        NoteListScreen(
            notes = emptyList(),
            onNoteClick = {},
            onNoteLongClick = {},
            onAddNoteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Mode Sélection")
@Composable
fun NoteListScreenSelectionModePreview() {
    MemoScribeTheme {
        NoteListScreen(
            notes = listOf(
                Note(
                    id = "1",
                    title = "Note sélectionnée 1",
                    content = "Contenu de la première note sélectionnée."
                ),
                Note(
                    id = "2",
                    title = "Note sélectionnée 2",
                    content = "Contenu de la deuxième note sélectionnée."
                ),
                Note(
                    id = "3",
                    title = "Note non sélectionnée",
                    content = "Cette note n'est pas sélectionnée."
                )
            ),
            isSelectionMode = true,
            selectedNoteIds = setOf("1", "2"),
            onNoteClick = {},
            onNoteLongClick = {},
            onToggleNoteSelection = {},
            onAddNoteClick = {},
            onCancelSelection = {},
            onDeleteSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Beaucoup de Notes")
@Composable
fun NoteListScreenManyNotesPreview() {
    MemoScribeTheme {
        NoteListScreen(
            notes = List(10) { index ->
                Note(
                    title = "Note ${index + 1}",
                    content = "Ceci est le contenu de la note numéro ${index + 1} pour tester le scrolling."
                )
            },
            onNoteClick = {},
            onNoteLongClick = {},
            onAddNoteClick = {}
        )
    }
}