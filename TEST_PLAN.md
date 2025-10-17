# Plan de Tests - MemoScribe

## Tests Unitaires (à implémenter en Étape 3-4)

### Model Tests (Note.kt)
- ✅ Note creation avec UUID auto-généré
- ✅ Content preview generation (troncature à 50 chars)
- ✅ Date formatting (création)
- ✅ Date formatting (modification)
- ✅ Copie d'une note existante

### ViewModel Tests (NoteViewModel.kt)
- ✅ Add note functionality
- ✅ Delete note functionality
- ✅ Update note functionality (garde timestamp original)
- ✅ Get notes list
- ✅ Get note by ID 
- ✅ Validate input (titre vide, contenu vide)
- ✅ Validate input (titre trop long, contenu trop long)

### Repository Tests (NoteRepository.kt)
- ✅ Save notes to DataStore
- ✅ Load notes from DataStore
- ✅ Handle corrupted data
- ✅ Empty DataStore on first launch

## Tests d'Intégration
- ✅ Full CRUD cycle (add → update → delete)
- ✅ Persistence across app restart
- ✅ Data consistency after operations
- ✅ Multiple notes management

## Tests d'Utilisabilité (Manuel - Étape 4)
- ✅ Add note workflow
- ✅ Delete note workflow
- ✅ Edit note workflow
- ✅ App restart persistence
- ✅ Empty state display
- ✅ Long content handling

### Scénario 1 : Ajout de Note
- ✅ FAB fonctionne (ouvre AddEditNoteScreen)
- ✅ Mode AJOUTER détecté (champs vides)
- ✅ Validation titre vide → erreur affichée
- ✅ Validation contenu vide → erreur affichée
- ✅ Entrée valide + Valider → note ajoutée à liste
- ✅ Toast "Note créée" apparaît
- ✅ Retour automatique à liste

### Scénario 2 : Édition de Note
- ✅ Clic sur NoteCard → ouvre AddEditNoteScreen
- ✅ Mode ÉDITER détecté (champs pré-remplis)
- ✅ Dates affichées (création + modification)
- ✅ Bouton Supprimer visible
- ✅ Modification + Valider → note mise à jour
- ✅ Date de modification changée
- ✅ Toast "Note modifiée" apparaît
- ✅ Retour automatique à liste

### Scénario 3 : Suppression de Note
- ✅ Mode édition → Clic Supprimer
- ✅ Dialog de confirmation apparaît
- ✅ Confirmer → note supprimée
- ✅ Toast "Note supprimée" apparaît
- ✅ Retour automatique à liste

### Scénario 4 : Persistance
- ✅ Ajouter 3 notes
- ✅ Fermer l'app
- ✅ Rouvrir l'app
- ✅ Les 3 notes sont toujours présentes
- ✅ Les dates sont préservées


## Tests Limites (Edge Cases)
- ✅ Very long title (100+ chars)
- ✅ Very long content (5000+ chars)
- ✅ Empty DataStore on first launch
- ✅ Special characters in notes
- ✅ Unicode characters
- ✅ Rapid add/delete operations

### Validation des Entrées
- [ ] Titre très long (100+ caractères)
- [ ] Contenu très long (5000+ caractères)
- [ ] Titre avec uniquement des espaces
- [ ] Contenu avec uniquement des espaces
- [ ] Caractères spéciaux dans titre/contenu (@#$%^&*)
- [ ] Caractères Unicode (émojis 😀, accents éàç)
- [ ] Note avec titre identique à une autre

### Opérations Rapides
- [ ] Rapid add/delete operations (10 notes en 5 secondes)
- [ ] Ajouter note pendant le chargement
- [ ] Supprimer note pendant la sauvegarde
- [ ] Rotation écran pendant l'édition (préserve données?)

### États Extrêmes
- [ ] DataStore vide au premier lancement
- [ ] 0 notes dans la liste (empty state)
- [ ] 100 notes dans la liste (performance)
- [ ] Note avec contenu vide mais titre présent
- [ ] Note créée puis immédiatement supprimée


## Tests d'Interface

### Responsive Design
- [ ] UI responsive sur petit écran (5 pouces)
- [ ] UI responsive sur grand écran (6.5 pouces)
- [ ] UI responsive en orientation portrait
- [ ] UI responsive en orientation paysage
- [ ] Scrolling fluide dans LazyColumn (pas de lag)

### Navigation
- [ ] Transitions smooth entre écrans
- [ ] Bouton retour fonctionne correctement
- [ ] FAB toujours visible et cliquable
- [ ] Pas de double-clic accidentel

### Accessibilité
- [ ] Texte lisible (contraste suffisant)
- [ ] Boutons accessibles (minimum 48x48 dp)
- [ ] Content descriptions sur icônes
- [ ] Support TalkBack (lecteur d'écran)
- [ ] Taille de police respectée (paramètres système)

---

## Tests de Performance

### Temps de Réponse
- [ ] App load time < 2 secondes
- [ ] Ajout note < 500ms
- [ ] Update note < 500ms
- [ ] Delete note < 500ms
- [ ] Chargement liste de 50 notes < 1 seconde

### Ressources
- [ ] Pas de memory leaks (profiler)
- [ ] Utilisation RAM < 100 MB
- [ ] Pas de battery drain excessif
- [ ] Pas de lag lors du scroll
- [ ] App fonctionne en arrière-plan sans crash

---

## Tests de Stabilité

### Crash Prevention
- [ ] App ne crash pas au lancement
- [ ] App ne crash pas avec données corrompues
- [ ] App ne crash pas sans connexion Internet (pas nécessaire)
- [ ] App ne crash pas lors de rotation écran
- [ ] App ne crash pas avec mémoire faible

### Récupération d'Erreur
- [ ] Erreur DataStore → message utilisateur
- [ ] Validation échouée → message clair
- [ ] Note non trouvée → message d'erreur
- [ ] Timeout sauvegarde → retry automatique

---

## Matrice de Tests

| Test          | Priorité | Statut    | Responsable   | Date |
|---------------|----------|-----------|---------------|------|
| Note creation | Haute    | ✅ Passé   | Dev Logique 1 | -    |
| Add note      | Haute    | ⏳ À faire | Dev Logique 1 | -    |
| Update note   | Haute    | ⏳ À faire | Dev Logique 1 | -    |
| Delete note   | Haute    | ⏳ À faire | Dev Logique 1 | -    |
| Persistance   | Haute    | ⏳ À faire | Dev Logique 2 | -    |
| UI Navigation | Moyenne  | ⏳ À faire | Dev UI/UX     | -    |
| Performance   | Moyenne  | ⏳ À faire | Testeur       | -    |
| Edge cases    | Basse    | ⏳ À faire | Testeur       | -    |

---

## Outils de Test

### Tests Unitaires
- JUnit 4
- AssertJ (optionnel)
- Mockito (pour mocker Repository)

### Tests d'Interface
- Android Espresso
- Compose UI Testing
- Tests manuels

### Tests de Performance
- Android Profiler (CPU, Memory, Network)
- LeakCanary (memory leaks)

---

## Critères d'Acceptation

Pour que l'app soit considérée comme "prête" :

- ✅ **Tous les tests unitaires passent** (100%)
- ✅ **Tous les scénarios utilisateur fonctionnent** (ajout, édition, suppression, persistance)
- ✅ **Aucun crash** dans les cas normaux
- ✅ **Performance acceptable** (< 2s load, < 500ms operations)
- ✅ **UI responsive** sur différentes tailles d'écran
- ✅ **Code coverage > 80%**

---

## Planning des Tests

### ÉTAPE 3 (Développement Logique)
- Implémenter tests unitaires ViewModel
- Implémenter tests unitaires Repository

### ÉTAPE 4 (Tests et Finalisation)
- Exécuter tous les tests manuels
- Tester sur émulateur ET appareil physique
- Tester tous les edge cases
- Mesurer performance
- Corriger bugs trouvés

---

## Rapport de Bugs

Pour chaque bug trouvé, utiliser le template `BUG_REPORT_TEMPLATE.md`.

---

**Ce plan sera mis à jour au fur et à mesure de l'avancement du projet.**