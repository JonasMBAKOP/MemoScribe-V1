# Spécifications : Sélection et Suppression Multiple

## 📋 Vue d'Ensemble

Cette fonctionnalité permet à l'utilisateur de sélectionner plusieurs notes à la fois et de les supprimer en une seule opération.

**Avantages** :
- ✅ Gain de temps (supprimer 10 notes d'un coup)
- ✅ Meilleure UX (pas besoin d'ouvrir chaque note)
- ✅ Fonctionnalité attendue dans les apps modernes

---

## 🎯 User Stories

### US1 : Activer le Mode Sélection
**En tant qu'** utilisateur  
**Je veux** pouvoir activer un mode de sélection multiple  
**Afin de** sélectionner plusieurs notes à la fois

**Critères d'acceptation** :
- [ ] Long-press sur une note active le mode sélection
- [ ] La note long-pressée est automatiquement sélectionnée
- [ ] Le TopAppBar change de couleur (devient rouge)
- [ ] Des checkboxes apparaissent sur toutes les notes
- [ ] Un compteur s'affiche : "1 sélectionnée"

---

### US2 : Sélectionner/Désélectionner des Notes
**En tant qu'** utilisateur  
**Je veux** pouvoir sélectionner et désélectionner des notes  
**Afin de** choisir exactement celles que je veux supprimer

**Critères d'acceptation** :
- [ ] Clic sur une note toggle sa sélection
- [ ] Une note sélectionnée a une checkbox cochée (✓)
- [ ] Une note sélectionnée a une bordure violette
- [ ] Le compteur se met à jour : "2 sélectionnées", "3 sélectionnées"...
- [ ] Si toutes les notes sont désélectionnées, le mode sélection se désactive

---

### US3 : Supprimer les Notes Sélectionnées
**En tant qu'** utilisateur  
**Je veux** pouvoir supprimer toutes les notes sélectionnées d'un coup  
**Afin de** nettoyer rapidement ma liste

**Critères d'acceptation** :
- [ ] Icône corbeille visible dans le TopAppBar
- [ ] Clic sur la corbeille → Dialog de confirmation
- [ ] Dialog affiche le nombre de notes à supprimer
- [ ] Bouton "Supprimer" → Suppression effective + désactivation mode sélection
- [ ] Bouton "Annuler" → Retour au mode sélection (notes toujours sélectionnées)

---

### US4 : Annuler le Mode Sélection
**En tant qu'** utilisateur  
**Je veux** pouvoir quitter le mode sélection sans supprimer  
**Afin de** revenir à la vue normale

**Critères d'acceptation** :
- [ ] Icône ✕ visible dans le TopAppBar
- [ ] Clic sur ✕ → Désactivation du mode sélection
- [ ] Retour à l'état normal (checkboxes disparaissent, TopAppBar redevient violet)
- [ ] Bouton retour système → Désactivation du mode sélection

---

## 🏗️ Architecture

### États à Gérer dans le ViewModel

```kotlin
class NoteViewModel : ViewModel() {
    
    // État existant
    private val _notes = mutableStateOf<List<Note>>(emptyList())
    val notes: State<List<Note>> = _notes
    
    // ✨ NOUVEAUX ÉTATS pour la sélection multiple
    
    // Indique si le mode sélection est actif
    private val _isSelectionMode = mutableStateOf(false)
    val isSelectionMode: State<Boolean> = _isSelectionMode
    
    // Liste des IDs des notes sélectionnées
    private val _selectedNoteIds = mutableStateOf<Set<String>>(emptySet())
    val selectedNoteIds: State<Set<String>> = _selectedNoteIds
    
    // Nombre de notes sélectionnées (pour l'affichage)
    val selectedCount: Int
        get() = _selectedNoteIds.value.size
}
```

---

## 🔧 Fonctions à Implémenter

### 1. Activer le Mode Sélection

```kotlin
/**
 * Active le mode sélection et sélectionne automatiquement la note spécifiée
 * Appelée lors d'un long-press sur une note
 * 
 * @param noteId ID de la note qui a déclenché le mode sélection
 */
fun enableSelectionMode(noteId: String) {
    _isSelectionMode.value = true
    _selectedNoteIds.value = setOf(noteId)
}
```

**Exemple d'utilisation** :
```kotlin
// Dans NoteCard
Modifier.combinedClickable(
    onClick = { 
        if (isSelectionMode) {
            viewModel.toggleNoteSelection(note.id)
        } else {
            onNoteClick(note.id)
        }
    },
    onLongClick = {
        if (!isSelectionMode) {
            viewModel.enableSelectionMode(note.id)
        }
    }
)
```

---

### 2. Toggle Sélection d'une Note

```kotlin
/**
 * Sélectionne ou désélectionne une note
 * Si toutes les notes sont désélectionnées, désactive le mode sélection
 * 
 * @param noteId ID de la note à toggle
 */
fun toggleNoteSelection(noteId: String) {
    val currentSelection = _selectedNoteIds.value.toMutableSet()
    
    if (noteId in currentSelection) {
        // Désélectionner
        currentSelection.remove(noteId)
    } else {
        // Sélectionner
        currentSelection.add(noteId)
    }
    
    _selectedNoteIds.value = currentSelection
    
    // Si plus aucune note sélectionnée, désactiver le mode
    if (currentSelection.isEmpty()) {
        disableSelectionMode()
    }
}
```

**Exemple d'utilisation** :
```kotlin
// Dans NoteCard (mode sélection actif)
onClick = { viewModel.toggleNoteSelection(note.id) }
```

---

### 3. Désactiver le Mode Sélection

```kotlin
/**
 * Désactive le mode sélection et réinitialise la sélection
 * Appelée quand l'utilisateur clique sur ✕ ou appuie sur retour
 */
fun disableSelectionMode() {
    _isSelectionMode.value = false
    _selectedNoteIds.value = emptySet()
}
```

**Exemple d'utilisation** :
```kotlin
// Dans NoteListScreen (TopAppBar)
IconButton(onClick = { viewModel.disableSelectionMode() }) {
    Icon(Icons.Default.Close, "Annuler sélection")
}

// Ou avec BackHandler
BackHandler(enabled = isSelectionMode) {
    viewModel.disableSelectionMode()
}
```

---

### 4. Supprimer les Notes Sélectionnées

```kotlin
/**
 * Supprime toutes les notes sélectionnées
 * Désactive le mode sélection après suppression
 * 
 * @return Boolean - true si succès, false si erreur
 */
suspend fun deleteSelectedNotes(): Boolean {
    return try {
        val selectedIds = _selectedNoteIds.value
        
        // Filtrer pour garder seulement les notes non sélectionnées
        val remainingNotes = _notes.value.filter { it.id !in selectedIds }
        
        // Mettre à jour la liste
        _notes.value = remainingNotes
        
        // Sauvegarder
        saveNotes()
        
        // Désactiver le mode sélection
        disableSelectionMode()
        
        Log.d("NoteViewModel", "Suppression de ${selectedIds.size} notes réussie")
        true
    } catch (e: Exception) {
        Log.e("NoteViewModel", "Erreur suppression multiple: ${e.message}")
        false
    }
}
```

**Exemple d'utilisation** :
```kotlin
// Dans NoteListScreen (après confirmation dialog)
scope.launch {
    val success = viewModel.deleteSelectedNotes()
    if (success) {
        // Toast : "X notes supprimées"
    }
}
```

---

### 5. Sélectionner Toutes les Notes (Bonus)

```kotlin
/**
 * Sélectionne toutes les notes de la liste
 * Utile pour "Tout sélectionner"
 */
fun selectAllNotes() {
    _isSelectionMode.value = true
    _selectedNoteIds.value = _notes.value.map { it.id }.toSet()
}
```

**Exemple d'utilisation** :
```kotlin
// Dans NoteListScreen (menu overflow)
DropdownMenuItem(
    text = { Text("Tout sélectionner") },
    onClick = { viewModel.selectAllNotes() }
)
```

---

## 🎨 Composants UI à Modifier

### 1. NoteCard - Ajouter la Checkbox

```kotlin
@Composable
fun NoteCard(
    note: Note,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .then(
                // Bordure violette si sélectionnée
                if (isSelected) Modifier.border(3.dp, Primary, RoundedCornerShape(8.dp))
                else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = NoteCardBackground
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ✨ CHECKBOX (visible seulement en mode sélection)
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = null, // géré par onClick
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            
            // Contenu de la note
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.getContentPreview(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Modifiée le : ${note.getFormattedUpdatedDate()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
```

---

### 2. NoteListScreen - TopAppBar Dynamique

```kotlin
@Composable
fun NoteListScreen(
    notes: List<Note>,
    isSelectionMode: Boolean,
    selectedCount: Int,
    selectedNoteIds: Set<String>,
    onNoteClick: (String) -> Unit,
    onNoteLongClick: (String) -> Unit,
    onAddNoteClick: () -> Unit,
    onCancelSelection: () -> Unit,
    onDeleteSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (isSelectionMode) "$selectedCount sélectionnée(s)"
                        else stringResource(R.string.title_notes_list)
                    )
                },
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = onCancelSelection) {
                            Icon(Icons.Default.Close, "Annuler")
                        }
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, "Supprimer")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSelectionMode) Error else Primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            // FAB caché en mode sélection
            if (!isSelectionMode) {
                FloatingActionButton(onClick = onAddNoteClick) {
                    Icon(Icons.Default.Add, "Ajouter")
                }
            }
        }
    ) { padding ->
        // Liste des notes
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(notes) { note ->
                NoteCard(
                    note = note,
                    isSelectionMode = isSelectionMode,
                    isSelected = note.id in selectedNoteIds,
                    onClick = {
                        if (isSelectionMode) {
                            // Toggle sélection
                            // viewModel.toggleNoteSelection(note.id)
                        } else {
                            // Navigation vers édition
                            onNoteClick(note.id)
                        }
                    },
                    onLongClick = {
                        if (!isSelectionMode) {
                            // Activer mode sélection
                            // viewModel.enableSelectionMode(note.id)
                            onNoteLongClick(note.id)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    
    // ✨ DIALOG DE CONFIRMATION
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer les notes ?") },
            text = { 
                Text("Êtes-vous sûr de vouloir supprimer $selectedCount note(s) ? Cette action est irréversible.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteSelected()
                    }
                ) {
                    Text("Supprimer", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}
```

---

### 3. MainActivity - Gestion du BackHandler

```kotlin
@Composable
fun NoteListScreenWrapper(viewModel: NoteViewModel, ...) {
    val isSelectionMode by viewModel.isSelectionMode
    
    // Gérer le bouton retour système
    BackHandler(enabled = isSelectionMode) {
        viewModel.disableSelectionMode()
    }
    
    NoteListScreen(
        notes = viewModel.notes.value,
        isSelectionMode = isSelectionMode,
        selectedCount = viewModel.selectedCount,
        selectedNoteIds = viewModel.selectedNoteIds.value,
        onNoteClick = { /* ... */ },
        onNoteLongClick = { viewModel.enableSelectionMode(it) },
        onAddNoteClick = { /* ... */ },
        onCancelSelection = { viewModel.disableSelectionMode() },
        onDeleteSelected = { 
            scope.launch {
                viewModel.deleteSelectedNotes()
            }
        }
    )
}
```

---

## 🧪 Tests à Implémenter

### Tests Unitaires (NoteViewModel)

```kotlin
class NoteViewModelSelectionTest {
    
    private lateinit var viewModel: NoteViewModel
    
    @Before
    fun setup() {
        viewModel = NoteViewModel()
        // Ajouter des notes de test
        viewModel.addNote("Note 1", "Contenu 1")
        viewModel.addNote("Note 2", "Contenu 2")
        viewModel.addNote("Note 3", "Contenu 3")
    }
    
    @Test
    fun `enableSelectionMode active le mode et sélectionne la note`() {
        val noteId = viewModel.notes.value[0].id
        
        viewModel.enableSelectionMode(noteId)
        
        assertTrue(viewModel.isSelectionMode.value)
        assertTrue(noteId in viewModel.selectedNoteIds.value)
        assertEquals(1, viewModel.selectedCount)
    }
    
    @Test
    fun `toggleNoteSelection ajoute et retire correctement`() {
        val noteId = viewModel.notes.value[0].id
        viewModel.enableSelectionMode(noteId)
        
        // Désélectionner
        viewModel.toggleNoteSelection(noteId)
        assertFalse(viewModel.isSelectionMode.value) // Mode désactivé car 0 sélection
        
        // Réactiver et sélectionner 2 notes
        viewModel.enableSelectionMode(noteId)
        val noteId2 = viewModel.notes.value[1].id
        viewModel.toggleNoteSelection(noteId2)
        
        assertEquals(2, viewModel.selectedCount)
    }
    
    @Test
    fun `deleteSelectedNotes supprime les bonnes notes`() = runTest {
        val note1Id = viewModel.notes.value[0].id
        val note2Id = viewModel.notes.value[1].id
        
        viewModel.enableSelectionMode(note1Id)
        viewModel.toggleNoteSelection(note2Id)
        
        val initialCount = viewModel.notes.value.size
        viewModel.deleteSelectedNotes()
        
        assertEquals(initialCount - 2, viewModel.notes.value.size)
        assertFalse(viewModel.isSelectionMode.value)
    }
    
    @Test
    fun `selectAllNotes sélectionne toutes les notes`() {
        viewModel.selectAllNotes()
        
        assertTrue(viewModel.isSelectionMode.value)
        assertEquals(3, viewModel.selectedCount)
    }
}
```

---

### Tests Manuels (UI)

| Test | Action | Résultat Attendu |
|------|--------|------------------|
| **Activation mode** | Long-press sur une note | Mode sélection actif, note sélectionnée, TopAppBar rouge |
| **Sélection multiple** | Cliquer sur 2 autres notes | 3 notes sélectionnées, compteur = "3 sélectionnées" |
| **Désélection** | Re-cliquer sur une note sélectionnée | Note désélectionnée, compteur diminue |
| **Annulation** | Cliquer sur ✕ | Retour mode normal, checkboxes disparaissent |
| **Suppression** | Cliquer sur corbeille → Confirmer | Dialog → Notes supprimées, mode désactivé |
| **Annulation suppression** | Cliquer sur corbeille → Annuler | Dialog se ferme, mode sélection toujours actif |
| **Bouton retour** | Appuyer sur retour système | Mode sélection désactivé |
| **Rotation écran** | Tourner l'écran en mode sélection | Sélection préservée (si ViewModel correct) |

---

## 📊 Diagramme d'États

```
┌─────────────────┐
│  Mode Normal    │ ← État initial
└────────┬────────┘
         │
         │ Long-press sur note
         ▼
┌─────────────────┐
│ Mode Sélection  │
│ (1 note)        │
└────────┬────────┘
         │
         ├─→ Clic autre note → Mode Sélection (2 notes)
         │
         ├─→ Clic note sélectionnée → Mode Sélection (0 note) → Mode Normal
         │
         ├─→ Clic ✕ → Mode Normal
         │
         └─→ Clic 🗑️ → Dialog → Confirmer → Mode Normal
                                └→ Annuler → Mode Sélection (inchangé)
```

---

## ⚡ Optimisations

### Performance

1. **Utiliser Set au lieu de List** pour `selectedNoteIds`
    - Recherche O(1) au lieu de O(n)
    - `noteId in selectedNoteIds` est instantané

2. **Éviter les recompositions inutiles**
   ```kotlin
   // ✅ Bon
   val isSelected = remember(note.id, selectedNoteIds) {
       note.id in selectedNoteIds
   }
   
   // ❌ Mauvais
   val isSelected = note.id in viewModel.selectedNoteIds.value
   ```

3. **Batch operations**
    - Supprimer toutes les notes en une seule opération DataStore

---

## 🎯 Critères de Succès

L'implémentation est réussie si :

- [ ] Long-press active le mode sélection
- [ ] Checkboxes apparaissent en mode sélection
- [ ] TopAppBar change de couleur
- [ ] Compteur fonctionne correctement
- [ ] Sélection/désélection fonctionne au clic
- [ ] Icône corbeille visible et fonctionnelle
- [ ] Dialog de confirmation s'affiche
- [ ] Suppression effective fonctionne
- [ ] Mode se désactive après suppression
- [ ] Bouton ✕ désactive le mode
- [ ] Bouton retour système désactive le mode
- [ ] Tous les tests unitaires passent
- [ ] Aucun crash en cas d'utilisation intensive

---

## 📚 Ressources

### Documentation Jetpack Compose
- [Modifier.combinedClickable](https://developer.android.com/reference/kotlin/androidx/compose/foundation/package-summary#combinedClickable)
- [BackHandler](https://developer.android.com/jetpack/compose/libraries#activity)
- [AlertDialog](https://developer.android.com/jetpack/compose/components/dialog)
- [Checkbox](https://developer.android.com/jetpack/compose/components/checkbox)

### Best Practices
- Utiliser `remember` pour éviter recompositions
- Utiliser `derivedStateOf` pour états calculés
- Tester tous les edge cases

---

**Cette fonctionnalité sera implémentée en ÉTAPE 3 avec le reste de la logique métier.**