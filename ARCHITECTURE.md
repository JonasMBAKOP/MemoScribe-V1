# Architecture du Projet MemoScribe

## Objectif
Application de prise de notes simple avec persistance locale.

## Pattern Architectural
MVVM (Model-View-ViewModel)

## Couches

### 1. Model (model/)
- Note.kt : représente une note avec id, title, content, timestamp, updatedTimestamp
- Fonctions : getFormattedCreatedDate(), getFormattedUpdatedDate(), getContentPreview()

### 2. ViewModel (viewmodel/)
- NoteViewModel : gère l'état des notes et les opérations CRUD
  - État : notes (List<Note>), selectedNote (Note?)
  - Opérations :
    - addNote(title, content)
    - updateNote(id, newTitle, newContent)
    - deleteNote(id)
    - getNoteById(id)
    - loadNotes()
    - saveNotes()

### 3. View (ui/)
- NoteListScreen : affiche la liste des notes avec FAB
- AddEditNoteScreen : formulaire d'ajout/édition (écran unifié)
  - Mode AJOUTER : champs vides, 2 boutons
  - Mode ÉDITER : champs pré-remplis, 3 boutons, affiche dates
- NoteCard : composant réutilisable pour afficher une note dans la liste

### 4. Persistance (repository/)
- NoteRepository : interface avec DataStore
    - getNotes() : retourne Flow<List<Note>>
    - saveNotes() : sauvegarde les notes

## Flux de Données
1. User clique sur bouton → View appelle ViewModel
2. ViewModel traite l'action → met à jour State
3. State change → View se redessine avec nouvelles données
4. ViewModel sauvegarde dans DataStore → données persistées

Plus explicitement :
1. User interagit avec View
2. View appelle fonction du ViewModel
3. ViewModel met à jour l'état (StateFlow)
4. ViewModel appelle Repository pour persister
5. View se redessine automatiquement avec nouvel état

## Conventions de Nommage
- Classes : PascalCase (NoteViewModel, NoteCard)
- Fonctions : camelCase (addNote, deleteNote)
- Variables : camelCase (noteTitle, noteList)
- Constantes : UPPER_SNAKE_CASE (MAX_TITLE_LENGTH)
- Fichiers UI : nom du composable + ".kt"

## Structure des Dossiers
````
src/main/
├── java(ou kotlin)/com/example/memoscribe/
│   ├── model/
│   │   └── Note.kt
│   ├── viewmodel/
│   │   └── NoteViewModel.kt
│   ├── repository/
│   │   └── NoteRepository.kt
│   ├── ui/
│   │   ├── screens/
│   │   │   ├── NoteListScreen.kt
│   │   │   └── AddEditNoteScreen.kt
│   │   ├── components/
│   │   │   └── NoteCard.kt
│   │   └── theme/
│   │       ├── Color.kt
│   │       ├── Type.kt
│   │       └── Theme.kt
│   └── MainActivity.kt
├── res/
│   └── values/
│       └── strings.xml
````

## Différences Ajouter vs Éditer

### Mode AJOUTER

* noteId = null
* Titre : "Ajouter une note"
* Champs vides 
* Boutons : Annuler, Valider 
* PAS de Supprimer 
* PAS d'affichage des dates

### Mode ÉDITER

* noteId = défini 
* Titre : "Éditer la note"
* Champs pré-remplis 
* Boutons : Annuler, Valider, Supprimer 
* Affiche dates création ET modification