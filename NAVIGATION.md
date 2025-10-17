# Flux de Navigation - MemoScribe

## Écrans Principaux

### 1️⃣ NoteListScreen (Écran d'accueil)
- Affiche liste de toutes les notes
- FloatingActionButton en bas-droit (ajouter)
- Clic sur NoteCard → naviguer vers AddEditNoteScreen (ÉDITER)

### 2️⃣ AddEditNoteScreen (Écran unifié ajouter/éditer)
- MODE AJOUTER :
    - Champs vides
    - Titre header : "Ajouter une note"
    - 2 boutons : Annuler, Valider
    - PAS de bouton Supprimer
    - PAS d'affichage des dates

- MODE ÉDITER :
    - Champs pré-remplis avec données existantes
    - Titre header : "Éditer la note"
    - 3 boutons : Annuler, Valider, Supprimer
    - Affichage des dates (création + modification)
    - Bouton Retour pour revenir à liste

## Transitions
NoteListScreen
↓
[FAB (+)] → AddEditNoteScreen (AJOUTER mode)
[Note Card] → AddEditNoteScreen (ÉDITER mode)
AddEditNoteScreen
↓
[Annuler] → NoteListScreen
[Valider] → NoteListScreen + Toast "Sauvegardé"
[Supprimer] → Dialog confirmation → NoteListScreen + Toast "Supprimé"

## Paramètres de Navigation
````kotlin
// AddEditNoteScreen peut recevoir :
// - noteId: String? (null = mode AJOUTER, non-null = mode ÉDITER)

// Ou utiliser une Sealed Class
sealed class NavigationEvent {
object AddNote : NavigationEvent()
data class EditNote(val noteId: String) : NavigationEvent()
}
````