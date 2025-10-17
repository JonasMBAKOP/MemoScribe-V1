# Spécifications du NoteViewModel

## Vue d'Ensemble
Le `NoteViewModel` est responsable de la gestion de l'état des notes et de toutes les opérations CRUD (Create, Read, Update, Delete).

## État Géré

### Properties
```kotlin
private val _notes = mutableStateOf<List<Note>>(emptyList())
val notes: State<List<Note>> = _notes

private val _selectedNote = mutableStateOf<Note?>(null)
val selectedNote: State<Note?> = _selectedNote
```

## Fonctions à Implémenter

### 1. addNote(title: String, content: String): Boolean

**Description** : Crée une nouvelle note et l'ajoute à la liste

**Paramètres** :
- `title`: String - Le titre de la note
- `content`: String - Le contenu de la note

**Retour** : Boolean
- `true` si la note a été ajoutée avec succès
- `false` si la validation a échoué

**Logique** :
1. Valider que le titre n'est pas vide
2. Valider que le contenu n'est pas vide
3. Valider que le titre ne dépasse pas 100 caractères
4. Valider que le contenu ne dépasse pas 5000 caractères
5. Créer une nouvelle instance de Note
6. Ajouter la note à la liste `_notes`
7. Appeler `saveNotes()` pour persister
8. Retourner `true`

**Exemple d'utilisation** :
```kotlin
val success = viewModel.addNote("Titre", "Contenu")
if (success) {
    // Note ajoutée avec succès
}
```

---

### 2. updateNote(noteId: String, newTitle: String, newContent: String): Boolean

**Description** : Met à jour une note existante

**Paramètres** :
- `noteId`: String - L'ID de la note à modifier
- `newTitle`: String - Le nouveau titre
- `newContent`: String - Le nouveau contenu

**Retour** : Boolean
- `true` si la note a été mise à jour avec succès
- `false` si la note n'existe pas ou si la validation a échoué

**Logique** :
1. Valider les entrées (comme addNote)
2. Trouver la note par ID dans la liste
3. Si la note existe :
    - Créer une copie avec `.copy()`
    - Mettre à jour `title` et `content`
    - Mettre à jour `updatedTimestamp = System.currentTimeMillis()`
    - **NE PAS** modifier `id` et `timestamp` (création)
    - Remplacer l'ancienne note par la nouvelle dans la liste
    - Appeler `saveNotes()` pour persister
    - Retourner `true`
4. Sinon, retourner `false`

**Exemple d'utilisation** :
```kotlin
val success = viewModel.updateNote("uuid-123", "Nouveau titre", "Nouveau contenu")
if (success) {
    // Note mise à jour avec succès
}
```

---

### 3. deleteNote(noteId: String): Boolean

**Description** : Supprime une note de la liste

**Paramètres** :
- `noteId`: String - L'ID de la note à supprimer

**Retour** : Boolean
- `true` si la note a été supprimée avec succès
- `false` si la note n'existe pas

**Logique** :
1. Vérifier que la note existe dans la liste
2. Filtrer la liste pour exclure la note avec cet ID
3. Mettre à jour `_notes`
4. Appeler `saveNotes()` pour persister
5. Retourner `true`

**Exemple d'utilisation** :
```kotlin
val success = viewModel.deleteNote("uuid-123")
if (success) {
    // Note supprimée avec succès
}
```

---

### 4. getNoteById(noteId: String): Note?

**Description** : Récupère une note par son ID

**Paramètres** :
- `noteId`: String - L'ID de la note recherchée

**Retour** : Note?
- La note si elle existe
- `null` si elle n'existe pas

**Logique** :
1. Chercher dans la liste `_notes.value`
2. Retourner la première note avec l'ID correspondant
3. Si aucune note trouvée, retourner `null`

**Exemple d'utilisation** :
```kotlin
val note = viewModel.getNoteById("uuid-123")
if (note != null) {
    // Note trouvée, pré-remplir les champs
}
```

---

### 5. loadNotes(): Suspend Function

**Description** : Charge les notes depuis DataStore au démarrage de l'app

**Logique** :
1. Appeler `repository.getNotes()`
2. Collecter le Flow
3. Mettre à jour `_notes`

**Appelée dans** : `init {}` du ViewModel

---

### 6. saveNotes(): Suspend Function

**Description** : Sauvegarde les notes dans DataStore

**Logique** :
1. Appeler `repository.saveNotes(_notes.value)`

**Appelée après** : Chaque opération add/update/delete

---

## Validations

### validateNoteInput(title: String, content: String): Pair<Boolean, String?>

**Description** : Valide les entrées utilisateur

**Paramètres** :
- `title`: String - Le titre à valider
- `content`: String - Le contenu à valider

**Retour** : Pair<Boolean, String?>
- `Pair(true, null)` si valide
- `Pair(false, "message d'erreur")` si invalide

**Règles de validation** :
1. Titre ne doit pas être vide ou que des espaces
2. Contenu ne doit pas être vide ou que des espaces
3. Titre ne doit pas dépasser 100 caractères
4. Contenu ne doit pas dépasser 5000 caractères

**Messages d'erreur** :
- "Le titre ne peut pas être vide"
- "Le contenu ne peut pas être vide"
- "Le titre ne doit pas dépasser 100 caractères"
- "Le contenu ne doit pas dépasser 5000 caractères"

---

## Constantes

```kotlin
companion object {
    const val MAX_TITLE_LENGTH = 100
    const val MAX_CONTENT_LENGTH = 5000
}
```

---

## Gestion des Erreurs

Toutes les fonctions doivent gérer les erreurs avec des try-catch :
```kotlin
try {
    // Logique
} catch (e: Exception) {
    Log.e("NoteViewModel", "Erreur: ${e.message}")
    return false
}
```

---

## Tests à Créer (Étape 3)

- [ ] Test addNote avec données valides
- [ ] Test addNote avec titre vide
- [ ] Test addNote avec contenu vide
- [ ] Test updateNote avec données valides
- [ ] Test updateNote avec note inexistante
- [ ] Test deleteNote avec note existante
- [ ] Test deleteNote avec note inexistante
- [ ] Test getNoteById avec ID valide
- [ ] Test getNoteById avec ID invalide
- [ ] Test validateNoteInput avec toutes les règles