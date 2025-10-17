# Fonctions à Implémenter dans NoteViewModel

## Opérations CRUD

### addNote(title: String, content: String): Boolean
- Crée une nouvelle instance de Note
- Valide que le titre et contenu ne sont pas vides
- Ajoute la note à la liste en mémoire
- Appelle saveNotes() pour persister
- Retourne true si succès, false sinon

### deleteNote(noteId: String): Boolean
- Trouve la note par ID
- La supprime de la liste en mémoire
- Appelle saveNotes() pour persister
- Retourne true si succès, false sinon

### updateNote(noteId: String, newTitle: String, newContent: String): Boolean
- Trouve la note par ID
- Met à jour titre et contenu
- Appelle saveNotes() pour persister
- Retourne true si succès, false sinon

### getNoteById(noteId: String): Note?
- Retourne la note si trouvée, null sinon

## Validations

### validateNoteInput(title: String, content: String): Boolean
- Vérifie que le titre n'est pas vide
- Vérifie que le contenu n'est pas vide
- Vérifie que le titre ne dépasse pas 100 caractères
- Vérifie que le contenu ne dépasse pas 5000 caractères