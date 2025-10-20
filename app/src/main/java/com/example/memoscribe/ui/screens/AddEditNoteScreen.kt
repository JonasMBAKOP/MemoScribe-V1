package com.example.memoscribe.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.memoscribe.R
import com.example.memoscribe.model.Note
import com.example.memoscribe.ui.theme.Error
import com.example.memoscribe.ui.theme.MemoScribeTheme

/**
 * Écran unifié pour ajouter ou éditer une note.
 *
 * Mode AJOUTER (noteId == null) :
 * - Champs vides
 * - 2 boutons : Annuler, Valider
 * - Pas de bouton Supprimer
 * - Pas d'affichage des dates
 *
 * Mode ÉDITER (noteId != null) :
 * - Champs pré-remplis
 * - 3 boutons : Annuler, Valider, Supprimer
 * - Affichage des dates (création + modification)
 *
 * @param noteId ID de la note (null = mode AJOUTER)
 * @param note Données de la note à éditer (null en mode AJOUTER)
 * @param onSaveClick Callback sauvegarde (title, content)
 * @param onDeleteClick Callback suppression
 * @param onNavigateBack Callback retour
 * @param modifier Modificateur optionnel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    noteId: String?,
    note: Note? = null,
    onSaveClick: (title: String, content: String) -> Unit,
    onDeleteClick: () -> Unit = {},
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // État local pour les champs de texte
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Déterminer le mode
    val isEditMode = noteId != null && noteId != "null"

    // Validation
    val isTitleValid = title.isNotBlank()
    val isContentValid = content.isNotBlank()
    val canSave = isTitleValid && isContentValid

    // Gérer le bouton retour système
    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) {
                            stringResource(R.string.title_edit_note)
                        } else {
                            stringResource(R.string.title_add_note)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.button_cancel)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Affichage des dates (seulement en mode ÉDITER)
            if (isEditMode && note != null) {
                DateInfoCard(note = note)
            }

            // Champ Titre
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.label_title)) },
                placeholder = { Text(stringResource(R.string.hint_title)) },
                singleLine = true,
                isError = title.isNotBlank() && title.length > 100,
                supportingText = {
                    if (title.isNotBlank() && title.length > 100) {
                        Text(
                            text = stringResource(R.string.error_title_too_long),
                            color = Error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Champ Contenu
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(R.string.label_content)) },
                placeholder = { Text(stringResource(R.string.hint_content)) },
                minLines = 8,
                maxLines = 15,
                isError = content.isNotBlank() && content.length > 5000,
                supportingText = {
                    if (content.isNotBlank() && content.length > 5000) {
                        Text(
                            text = stringResource(R.string.error_content_too_long),
                            color = Error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Boutons d'action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bouton Annuler
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.button_cancel))
                }

                // Bouton Valider
                Button(
                    onClick = {
                        if (canSave) {
                            onSaveClick(title.trim(), content.trim())
                        }
                    },
                    enabled = canSave,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.button_save))
                }
            }

            // Bouton Supprimer (seulement en mode ÉDITER)
            if (isEditMode) {
                TextButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.button_delete),
                        color = Error
                    )
                }
            }
        }
    }

    // Dialog de confirmation de suppression
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(text = stringResource(R.string.confirm_delete))
            },
            text = {
                Text(text = "Cette action est irréversible.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.button_confirm),
                        color = Error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = stringResource(R.string.button_cancel))
                }
            }
        )
    }
}

/**
 * Carte affichant les dates de création et modification
 */
@Composable
private fun DateInfoCard(
    note: Note,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "📅 Créée le : ${note.getFormattedCreatedDate()}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = "✏️ Modifiée le : ${note.getFormattedUpdatedDate()}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true, name = "Mode AJOUTER")
@Composable
fun AddEditNoteScreenAddModePreview() {
    MemoScribeTheme {
        AddEditNoteScreen(
            noteId = null,
            note = null,
            onSaveClick = { _, _ -> },
            onDeleteClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Mode ÉDITER")
@Composable
fun AddEditNoteScreenEditModePreview() {
    MemoScribeTheme {
        AddEditNoteScreen(
            noteId = "123",
            note = Note(
                title = "Courses de la semaine",
                content = "Acheter du lait, des œufs, du pain, du fromage et des fruits pour toute la semaine."
            ),
            onSaveClick = { _, _ -> },
            onDeleteClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Mode ÉDITER - Contenu Long")
@Composable
fun AddEditNoteScreenLongContentPreview() {
    MemoScribeTheme {
        AddEditNoteScreen(
            noteId = "123",
            note = Note(
                title = "Réunion projet",
                content = """
                    Points à discuter lors de la réunion :
                    1. Budget alloué pour le projet
                    2. Planning et deadlines importantes
                    3. Constitution de l'équipe
                    4. Objectifs à court et long terme
                    5. Risques potentiels
                    6. Stratégie de communication
                    7. Outils à utiliser
                    8. Prochaines étapes
                """.trimIndent()
            ),
            onSaveClick = { _, _ -> },
            onDeleteClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Validation Erreurs")
@Composable
fun AddEditNoteScreenErrorsPreview() {
    MemoScribeTheme {
        var title by remember { mutableStateOf("a".repeat(110)) }
        var content by remember { mutableStateOf("Contenu valide") }

        AddEditNoteScreen(
            noteId = null,
            note = null,
            onSaveClick = { _, _ -> },
            onDeleteClick = {},
            onNavigateBack = {}
        )
    }
}