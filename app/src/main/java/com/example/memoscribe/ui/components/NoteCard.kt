package com.example.memoscribe.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.memoscribe.model.Note
import com.example.memoscribe.ui.theme.MemoScribeTheme
import com.example.memoscribe.ui.theme.NoteCardBackground
import com.example.memoscribe.ui.theme.Primary

/**
 * Composant réutilisable pour afficher une note dans la liste.
 *
 * Fonctionnalités :
 * - Affiche le titre, l'aperçu du contenu et la date de modification
 * - Supporte le mode sélection avec checkbox
 * - Cliquable (onClick) et long-cliquable (onLongClick)
 * - Bordure violette quand sélectionnée
 *
 * @param note La note à afficher
 * @param isSelectionMode Indique si le mode sélection est actif
 * @param isSelected Indique si cette note est sélectionnée
 * @param onClick Action au clic normal
 * @param onLongClick Action au long-press
 * @param modifier Modificateur optionnel
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    note: Note,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = NoteCardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = if (isSelected) BorderStroke(3.dp, Primary) else null,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox (visible seulement en mode sélection)
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = null, // Géré par le onClick du Card
                    colors = CheckboxDefaults.colors(
                        checkedColor = Primary
                    )
                )
            }

            // Contenu de la note
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Titre
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Aperçu du contenu
                Text(
                    text = note.getContentPreview(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                // Date de modification
                Text(
                    text = "Modifiée le : ${note.getFormattedUpdatedDate()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true, name = "Mode Normal")
@Composable
fun NoteCardPreview() {
    MemoScribeTheme {
        NoteCard(
            note = Note(
                title = "Courses de la semaine",
                content = "Acheter du lait, des œufs, du pain, du fromage et des fruits pour toute la semaine."
            ),
            onClick = {},
            onLongClick = {}
        )
    }
}

/*
@Preview(showBackground = true, name="Mode Sombre", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NoteCardDarkPreview() {
    MemoScribeTheme {
        NoteCard(
            note = Note(title = "Mode sombre", content = "Test du mode sombre"),
            onClick = {}
        )
    }
}
*/

@Preview(showBackground = true, name = "Contenu Court")
@Composable
fun NoteCardShortContentPreview() {
    MemoScribeTheme {
        NoteCard(
            note = Note(
                title = "Note courte",
                content = "Contenu court"
            ),
            onClick = {},
            onLongClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Titre Long")
@Composable
fun NoteCardLongTitlePreview() {
    MemoScribeTheme {
        NoteCard(
            note = Note(
                title = "Ceci est un très très très long titre qui pourrait potentiellement dépasser une ligne",
                content = "Contenu de la note avec un titre très long pour tester l'affichage."
            ),
            onClick = {},
            onLongClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Mode Sélection - Non Sélectionnée")
@Composable
fun NoteCardSelectionModePreview() {
    MemoScribeTheme {
        NoteCard(
            note = Note(
                title = "Note en mode sélection",
                content = "Cette note est en mode sélection mais pas sélectionnée."
            ),
            isSelectionMode = true,
            isSelected = false,
            onClick = {},
            onLongClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Mode Sélection - Sélectionnée")
@Composable
fun NoteCardSelectedPreview() {
    MemoScribeTheme {
        NoteCard(
            note = Note(
                title = "Note sélectionnée",
                content = "Cette note est sélectionnée avec bordure violette et checkbox cochée."
            ),
            isSelectionMode = true,
            isSelected = true,
            onClick = {},
            onLongClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Liste de 3 Notes")
@Composable
fun NoteCardListPreview() {
    MemoScribeTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NoteCard(
                note = Note(
                    title = "Réunion projet",
                    content = "Points à discuter : budget, planning, équipe, objectifs..."
                ),
                onClick = {}
            )
            NoteCard(
                note = Note(
                    title = "Idées vacances",
                    content = "Destinations possibles : Bali, Tokyo, New York, Paris..."
                ),
                onClick = {}
            )
            NoteCard(
                note = Note(
                    title = "Liste de courses",
                    content = "Lait, pain, œufs, fromage, fruits, légumes..."
                ),
                onClick = {}
            )
        }
    }
}