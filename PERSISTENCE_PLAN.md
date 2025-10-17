# Plan de Persistance - MemoScribe

## Technologie Utilisée
**DataStore Preferences** (Jetpack)

---

## Pourquoi DataStore ?
- ✅ Plus moderne que SharedPreferences
- ✅ Asynchrone par défaut (pas de blocage UI)
- ✅ Type-safe avec Flow
- ✅ Gestion automatique des erreurs
- ✅ Thread-safe
- ✅ Support des coroutines Kotlin

---

## Architecture de Persistance
1. Application → NoteViewModel
2. NoteViewModel → DataStore
3. DataStore → Fichier local (.proto ou JSON)
```
┌─────────────────────────────────┐
│      NoteViewModel              │
│  (Gestion de l'état)            │
└────────────┬────────────────────┘
             │
             │ appelle
             ▼
┌─────────────────────────────────┐
│      NoteRepository             │
│  (Interface avec DataStore)     │
└────────────┬────────────────────┘
             │
             │ utilise
             ▼
┌─────────────────────────────────┐
│         DataStore               │
│  (Stockage physique)            │
└────────────┬────────────────────┘
             │
             │ sauvegarde dans
             ▼
┌─────────────────────────────────┐
│    Fichier sur le disque        │
│  /data/data/com.example.        │
│  memoscribe/files/datastore/    │
│  notes_db.preferences_pb        │
└─────────────────────────────────┘
```

---

## Stratégie de Sérialisation
- Convertir List<Note> en JSON avec Gson
- Stocker dans une clé "notes_list" dans DataStore
- Charger au démarrage de l'app (dans ViewModel init)
- Sauvegarder après chaque opération (add/delete/update)

### Format : JSON avec Gson

**Pourquoi JSON ?**
- ✅ Lisible par humain
- ✅ Facile à déboguer
- ✅ Compatible avec tous les types
- ✅ Gson est simple et fiable

### Exemple de Données Stockées

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "Première note",
    "content": "Ceci est le contenu de ma première note",
    "timestamp": 1697548800000,
    "updatedTimestamp": 1697548800000
  },
  {
    "id": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
    "title": "Deuxième note",
    "content": "Contenu de la deuxième note",
    "timestamp": 1697635200000,
    "updatedTimestamp": 1697635200000
  }
]
```

---

## Flux de Données

### Chargement des Notes (au démarrage)

```
1. App démarre
   ↓
2. NoteViewModel.init() appelle loadNotes()
   ↓
3. loadNotes() appelle repository.getNotes()
   ↓
4. Repository lit depuis DataStore
   ↓
5. Désérialise JSON → List<Note>
   ↓
6. Émet via Flow
   ↓
7. ViewModel reçoit et met à jour _notes
   ↓
8. UI se redessine avec les notes
```

### Sauvegarde des Notes (après modification)

```
1. User ajoute/modifie/supprime une note
   ↓
2. ViewModel appelle addNote/updateNote/deleteNote
   ↓
3. ViewModel met à jour _notes (état en mémoire)
   ↓
4. ViewModel appelle saveNotes()
   ↓
5. saveNotes() appelle repository.saveNotes(_notes.value)
   ↓
6. Repository sérialise List<Note> → JSON
   ↓
7. Repository écrit dans DataStore
   ↓
8. DataStore persiste sur le disque
```

---

## Gestion des Erreurs
- Try-catch sur les opérations DataStore
- Logs pour debug
- Fallback à liste vide si fichier corrompu

### Cas 1 : Fichier Corrompu

**Problème** : Le JSON est invalide ou corrompu

**Solution** :
```kotlin
try {
    val notes: List<Note> = gson.fromJson(notesJson, type)
    notes
} catch (e: Exception) {
    Log.e(TAG, "JSON corrompu, retour liste vide")
    emptyList()
}
```

**Résultat** : L'app affiche une liste vide au lieu de crasher

---

### Cas 2 : Premier Lancement (DataStore Vide)

**Problème** : Aucune note n'existe encore

**Solution** :
```kotlin
val notesJson = preferences[NOTES_KEY] ?: return@map emptyList()
```

**Résultat** : Retourne une liste vide, l'app affiche le message "Aucune note"

---

### Cas 3 : Erreur d'Écriture

**Problème** : Impossible d'écrire dans DataStore (permissions, espace disque)

**Solution** :
```kotlin
try {
    context.dataStore.edit { ... }
} catch (e: Exception) {
    Log.e(TAG, "Erreur sauvegarde")
    throw e  // Relancer pour que ViewModel puisse gérer
}
```

**Résultat** : Le ViewModel peut afficher un message d'erreur à l'utilisateur

---

## Optimisations Performance

### 1. Caching en Mémoire (ViewModel)

Le ViewModel garde les notes en mémoire dans `_notes`.

**Avantages** :
- Pas de lecture DataStore à chaque rendu
- UI ultra-rapide
- DataStore appelé uniquement pour les modifications

### 2. Flow au lieu de Callback

DataStore retourne un `Flow<List<Note>>`.

**Avantages** :
- Réactif : UI se met à jour automatiquement
- Pas de memory leaks
- Annulation automatique

### 3. Suspend Functions

Toutes les écritures sont `suspend`.

**Avantages** :
- Pas de blocage du thread UI
- Coroutines Kotlin = code simple
- Gestion automatique des threads

---

## Dossier de Stockage

### Chemin Complet

```
/data/data/com.example.memoscribe/files/datastore/notes_db.preferences_pb
```

### Accès au Fichier

**Pour déboguer** :
1. Ouvrir Device File Explorer dans Android Studio
2. Naviguer vers `/data/data/com.example.memoscribe/files/datastore/`
3. Télécharger le fichier `notes_db.preferences_pb`

⚠️ **Note** : Le fichier est en format Protocol Buffer (binaire), pas directement lisible

---

## Tests à Implémenter

### Tests du Repository

- [ ] Test saveNotes avec liste vide
- [ ] Test saveNotes avec 1 note
- [ ] Test saveNotes avec plusieurs notes
- [ ] Test getNotes avec DataStore vide
- [ ] Test getNotes avec données valides
- [ ] Test getNotes avec JSON corrompu
- [ ] Test clearAllNotes

### Tests d'Intégration

- [ ] Sauvegarder → Charger → Vérifier identique
- [ ] Ajouter note → Restart app → Note toujours présente
- [ ] Supprimer note → Restart app → Note disparue

---

## Migration Future (Si Nécessaire)

Si on doit migrer vers Room (base de données SQL) :

**Étapes** :
1. Créer entité Room
2. Créer DAO Room
3. Créer fonction de migration DataStore → Room
4. Garder NoteRepository comme interface
5. Changer l'implémentation interne

**Avantage** : Le ViewModel n'a rien à changer !

---

## Sécurité

### Données Non-Sensibles

Les notes ne contiennent pas d'informations sensibles (pas de mots de passe, pas de données bancaires).

### Pas de Chiffrement Nécessaire

DataStore stocke en clair, mais c'est OK pour des notes personnelles.

### Permissions

Aucune permission spéciale requise. DataStore utilise le stockage interne de l'app (privé par défaut).

---

## Commandes Utiles (ADB)

### Voir le Contenu du DataStore

```bash
# Lister les fichiers
adb shell run-as com.example.memoscribe ls -la files/datastore/

# Copier le fichier localement
adb shell run-as com.example.memoscribe cat files/datastore/notes_db.preferences_pb > notes_backup.pb
```

### Effacer les Données (Reset)

```bash
# Effacer toutes les données de l'app
adb shell pm clear com.example.memoscribe
```

---

## Résumé

| Aspect           | Choix                 | Raison                           |
|------------------|-----------------------|----------------------------------|
| **Stockage**     | DataStore Preferences | Moderne, asynchrone, type-safe   |
| **Format**       | JSON (via Gson)       | Lisible, simple, debuggable      |
| **Architecture** | Repository Pattern    | Séparation des responsabilités   |
| **Réactivité**   | Flow                  | UI se met à jour automatiquement |
| **Performance**  | Cache en mémoire      | Lecture DataStore minimale       |
| **Erreurs**      | Try-catch + logs      | Robustesse, pas de crash         |

---

**La persistance est prête pour l'implémentation en ÉTAPE 3 !** 🚀