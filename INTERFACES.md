# Contrats d'Interface - DataStore

## Interface NoteRepository

### Méthodes à Implémenter

#### getNotes(): Flow<List<Note>>
- Retourne un Flow de notes
- Flow = observable qui réagit aux changements
- Lecture depuis DataStore
- Desérialisation JSON → List<Note>

#### saveNotes(notes: List<Note>): suspend
- Prend une liste de notes
- Sérialisation List<Note> → JSON
- Écriture dans DataStore
- Fonction suspend (asynchrone)

## Gestion des Erreurs

- JSONParseException → log + retourner liste vide
- IOException → log + relancer exception
- DataStore timeout → implémenter retry logic

## Performance

- Caching en mémoire dans ViewModel
- Pas d'accès DataStore à chaque rendu
- Batch operations quand possible