# Plan de Navigation - MemoScribe

## 📱 Écrans Principaux de l'Application

### 1. NoteListScreen (Écran d'accueil)
- **Type** : Écran d'accueil principal
- **Rôle** : Point d'entrée de l'application
- **Contenu** : Liste de toutes les notes avec LazyColumn
- **Actions** :
  - Clic sur FAB (FloatingActionButton) en bas à droite → Navigation vers AddEditNoteScreen (mode AJOUTER)
  - Clic sur une note (NoteCard) → Navigation vers AddEditNoteScreen (mode ÉDITER)
  - Long-press sur note (NoteCard) → Active le mode sélection multiple

### 2. AddEditNoteScreen (Écran unifié ajouter/éditer)
- **Type** : Écran secondaire
- **Rôle** : Ajouter ou éditer une note
- **Paramètre** : `noteId` (String optionnel)
  - Si `noteId == "null"` ou absent → Mode AJOUTER
  - Si `noteId == UUID valide` → Mode ÉDITER
- **MODE AJOUTER** :
    - Champs vides
    - Titre header : "Ajouter une note"
    - 2 boutons : Annuler, Valider
    - PAS de bouton Supprimer
    - PAS d'affichage des dates
- **MODE ÉDITER** :
    - Champs pré-remplis avec données existantes
    - Titre header : "Éditer la note"
    - 3 boutons : Annuler, Valider, Supprimer
    - Affichage des dates (création + modification)
    - Bouton Retour pour revenir à liste
- **Actions** :
    - Bouton Valider → Sauvegarde + Retour à NoteListScreen
    - Bouton Annuler → Retour à NoteListScreen sans sauvegarder
    - Bouton Supprimer (mode ÉDITER seulement) → Suppression + Retour à NoteListScreen
    - Bouton système Retour → Retour à NoteListScreen sans sauvegarder

---

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

---

## 🔄 Flux de Navigation

```
┌─────────────────────────────────────────┐
│         MainActivity                    │
│  (Point d'entrée de l'application)      │
└────────────────┬────────────────────────┘
                 │
                 │ Lance au démarrage
                 ▼
┌─────────────────────────────────────────┐
│       NoteListScreen                    │
│  Route: "notes_list"                    │
│                                         │
│  [Affiche la liste des notes]          │
│                                         │
│  Actions utilisateur possibles:         │
│  • Clic FAB (+)                         │
│  • Clic sur une note                    │
│  • Long-press sur une note              │
└────────────────┬────────────────────────┘
                 │
                 ├──[Clic FAB]──────────────────────┐
                 │                                   │
                 │                                   ▼
                 │                    ┌──────────────────────────┐
                 │                    │  AddEditNoteScreen       │
                 │                    │  Route: add_edit_note/null│
                 │                    │  Mode: AJOUTER           │
                 │                    │  • Champs vides          │
                 │                    │  • 2 boutons             │
                 │                    └──────────┬───────────────┘
                 │                               │
                 │                               │[Valider/Annuler]
                 │                               │
                 ├──[Clic Note]─────────────────┼──────┐
                 │                               │      │
                 │                               │      ▼
                 │                               │   ┌──────────────────────────┐
                 │                               │   │  AddEditNoteScreen       │
                 │                               │   │  Route: add_edit_note/{id}│
                 │                               │   │  Mode: ÉDITER            │
                 │                               │   │  • Champs pré-remplis    │
                 │                               │   │  • 3 boutons             │
                 │                               │   └──────────┬───────────────┘
                 │                               │              │
                 │                               │              │[Valider/Annuler/Suppr]
                 │                               │              │
                 ├──[Long-press]─────────────────┼──────────────┘
                 │ (Mode sélection activé)       │
                 │ Reste sur NoteListScreen      │
                 │                               │
                 └───────────────────────────────┴────── Retour NoteListScreen
```

---

## 🛠️ Implémentation avec Navigation Compose

### Dépendance Nécessaire

```gradle
dependencies {
    implementation("androidx.navigation:navigation-compose:2.7.5")
}
```

### Structure du NavHost

```kotlin
@Composable
fun MemoScribeApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "notes_list"
    ) {
        // Écran 1 : Liste des notes
        composable("notes_list") {
            NoteListScreen(
                onNoteClick = { noteId ->
                    navController.navigate("add_edit_note/$noteId")
                },
                onAddNoteClick = {
                    navController.navigate("add_edit_note/null")
                }
            )
        }
        
        // Écran 2 : Ajouter/Éditer une note
        composable(
            route = "add_edit_note/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            
            AddEditNoteScreen(
                noteId = noteId,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
```

---

## 📊 Tableau des Actions de Navigation

| Action Utilisateur          | Depuis            | Vers                       | Paramètre          | Type Navigation |
|-----------------------------|-------------------|----------------------------|--------------------|-----------------|
| Clic FAB (+)                | NoteListScreen    | AddEditNoteScreen          | `noteId = null`    | `navigate()`    |
| Clic sur Note               | NoteListScreen    | AddEditNoteScreen          | `noteId = note.id` | `navigate()`    |
| Long-press Note             | NoteListScreen    | NoteListScreen             | -                  | Reste sur écran |
| Bouton Valider              | AddEditNoteScreen | NoteListScreen             | -                  | `navigateUp()`  |
| Bouton Annuler              | AddEditNoteScreen | NoteListScreen             | -                  | `navigateUp()`  |
| Bouton Supprimer            | AddEditNoteScreen | NoteListScreen             | -                  | `navigateUp()`  |
| Bouton système Retour       | AddEditNoteScreen | NoteListScreen             | -                  | `navigateUp()`  |
| Icône Corbeille (sélection) | NoteListScreen    | Dialog puis NoteListScreen | -                  | Dialog local    |

---

## 🔐 Gestion du Bouton Retour Système

### Dans AddEditNoteScreen

```kotlin
@Composable
fun AddEditNoteScreen(...) {
    // Gérer le bouton retour système
    BackHandler {
        onNavigateBack()
    }
    
    // UI...
}
```

### Dans NoteListScreen (Mode Sélection)

```kotlin
@Composable
fun NoteListScreen(...) {
    val isSelectionMode by viewModel.isSelectionMode
    
    // Si mode sélection actif, le bouton retour désactive le mode
    BackHandler(enabled = isSelectionMode) {
        viewModel.disableSelectionMode()
    }
    
    // UI...
}
```

---

## 🎯 Gestion des Paramètres de Navigation

### Mode AJOUTER
```kotlin
// Navigation
navController.navigate("add_edit_note/null")

// Récupération
val noteId = backStackEntry.arguments?.getString("noteId")
val isAddMode = noteId == null || noteId == "null"
```

### Mode ÉDITER
```kotlin
// Navigation avec ID réel
navController.navigate("add_edit_note/${note.id}")

// Récupération
val noteId = backStackEntry.arguments?.getString("noteId")
val isEditMode = noteId != null && noteId != "null"
```

---

## 🧪 Scénarios de Test

### Test 1 : Navigation FAB → Ajout
1. Lancer l'app → NoteListScreen s'affiche
2. Cliquer sur FAB (+)
3. ✅ AddEditNoteScreen s'ouvre en mode AJOUTER
4. ✅ Champs vides
5. ✅ 2 boutons visibles (Annuler, Valider)

### Test 2 : Navigation Note → Édition
1. Sur NoteListScreen avec des notes
2. Cliquer sur une note
3. ✅ AddEditNoteScreen s'ouvre en mode ÉDITER
4. ✅ Champs pré-remplis avec les données de la note
5. ✅ 3 boutons visibles (Annuler, Valider, Supprimer)
6. ✅ Dates affichées

### Test 3 : Retour depuis AddEditNoteScreen
1. Ouvrir AddEditNoteScreen (n'importe quel mode)
2. Cliquer sur Annuler
3. ✅ Retour à NoteListScreen
4. ✅ Aucune modification sauvegardée

### Test 4 : Bouton Retour Système
1. Ouvrir AddEditNoteScreen
2. Appuyer sur le bouton retour système
3. ✅ Retour à NoteListScreen
4. ✅ Même comportement que bouton Annuler

### Test 5 : Mode Sélection
1. Long-press sur une note
2. ✅ Mode sélection activé
3. ✅ Reste sur NoteListScreen
4. ✅ TopAppBar change de couleur
5. Appuyer sur bouton retour système
6. ✅ Mode sélection désactivé
7. ✅ Retour à l'état normal

---

## 📝 Notes d'Implémentation

### Deep Links (Optionnel)
Pour ouvrir directement une note depuis une notification :
```kotlin
composable(
    route = "add_edit_note/{noteId}",
    deepLinks = listOf(
        navDeepLink { uriPattern = "memoscribe://note/{noteId}" }
    )
)
```

### Animations de Transition (Optionnel)
```kotlin
composable(
    route = "add_edit_note/{noteId}",
    enterTransition = {
        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
    },
    exitTransition = {
        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
    }
)
```

### Sauvegarde de l'État lors de la Navigation
Le ViewModel survit à la navigation, donc l'état est préservé automatiquement.

---

## 🐛 Problèmes Potentiels et Solutions

### Problème 1 : Double navigation rapide
**Symptôme** : User clique 2 fois rapidement sur FAB, 2 écrans s'ouvrent

**Solution** : Désactiver le bouton après le premier clic
```kotlin
var isNavigating by remember { mutableStateOf(false) }

FloatingActionButton(
    onClick = {
        if (!isNavigating) {
            isNavigating = true
            onAddNoteClick()
        }
    }
)
```

### Problème 2 : Back stack se remplit
**Symptôme** : Besoin d'appuyer plusieurs fois sur retour pour quitter

**Solution** : Utiliser `popUpTo` pour nettoyer le back stack
```kotlin
navController.navigate("notes_list") {
    popUpTo("notes_list") { inclusive = true }
}
```

### Problème 3 : Paramètre noteId null non géré
**Symptôme** : Crash si noteId est vraiment null

**Solution** : Vérifier et convertir
```kotlin
val noteId = backStackEntry.arguments?.getString("noteId")
    ?.takeIf { it != "null" }
```

---

## ✅ Checklist d'Implémentation

- [ ] Dépendance navigation-compose ajoutée
- [ ] NavHost créé avec 2 routes
- [ ] Navigation FAB → AddEditNoteScreen fonctionne
- [ ] Navigation Note → AddEditNoteScreen fonctionne
- [ ] Paramètre noteId correctement passé
- [ ] Mode AJOUTER détecté (noteId == null)
- [ ] Mode ÉDITER détecté (noteId != null)
- [ ] Bouton Retour système géré
- [ ] BackHandler pour mode sélection implémenté
- [ ] Aucun crash lors des navigations
- [ ] Tests manuels effectués

---

**Ce plan sera suivi lors de l'implémentation en Phase 3 et Phase 4.**