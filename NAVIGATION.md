# Flux de Navigation - MemoScribe

## Écrans Principaux de l'application

### 1️⃣ NoteListScreen (Écran d'accueil)
- Ecran d'accueiprincipal
- Point f'entrée de l'app
- Affiche liste de toutes les notes
- FloatingActionButton en bas-droit (ajouter)
- Clic sur NoteCard → naviguer vers AddEditNoteScreen (ÉDITER)

### 2️⃣ AddEditNoteScreen (Écran unifié ajouter/éditer)
- Paramètre optionnel : noteId
- Si noteId = null → Mode AJOUTER
- Si noteId = défini → Mode ÉDITER
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
MainActivity
↓
NoteListScreen (écran principal)
↓
[Clic FAB (+)] → AddEditNoteScreen (mode AJOUTER)
[Clic Note Card] → AddEditNoteScreen (mode ÉDITER)
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

## Navigation avec Compose

Utiliser `androidx.navigation:navigation-compose`
```kotlin
NavHost(navController, startDestination = "notes_list") {
    composable("notes_list") {
        NoteListScreen(...)
    }
    composable("add_edit_note/{noteId}") { backStackEntry ->
        val noteId = backStackEntry.arguments?.getString("noteId")
        AddEditNoteScreen(noteId = noteId, ...)
    }
}
```

## Actions de Navigation

| Action        | Depuis            | Vers              | Paramètre        |
|---------------|-------------------|-------------------|------------------|
| Clic FAB      | NoteListScreen    | AddEditNoteScreen | noteId = null    |
| Clic Note     | NoteListScreen    | AddEditNoteScreen | noteId = note.id |
| Valider       | AddEditNoteScreen | NoteListScreen    | -                |
| Annuler       | AddEditNoteScreen | NoteListScreen    | -                |
| Supprimer     | AddEditNoteScreen | NoteListScreen    | -                |
| Bouton Retour | AddEditNoteScreen | NoteListScreen    | -                |