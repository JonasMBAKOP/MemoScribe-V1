# Checklist de Qualité du Code - MemoScribe

## 📊 Code Coverage

- [ ] **80%+** des fonctions testées
- [ ] Tous les tests unitaires passent ✅
- [ ] Tous les tests d'intégration passent ✅
- [ ] Aucun test en rouge ❌
- [ ] Coverage report généré

**Commande** :
```bash
./gradlew testDebugUnitTestCoverage
```

---

## 🎨 Conventions de Code Kotlin

### Nommage
- [ ] **PascalCase** pour les classes (`Note`, `NoteViewModel`)
- [ ] **camelCase** pour les fonctions (`addNote`, `deleteNote`)
- [ ] **camelCase** pour les variables (`noteTitle`, `noteList`)
- [ ] **UPPER_SNAKE_CASE** pour les constantes (`MAX_TITLE_LENGTH`)
- [ ] Pas de noms abrégés incompréhensibles (`n` → `note`)

### Structure
- [ ] Pas de code mort (unused imports, variables, fonctions)
- [ ] Pas d'erreurs Lint
- [ ] Pas de warnings Lint (ou justifiés)
- [ ] Indentation cohérente (4 espaces)
- [ ] Lignes < 120 caractères

### Best Practices Kotlin
- [ ] Utilisation de `data class` pour les modèles
- [ ] Immutabilité préférée (`val` vs `var`)
- [ ] Null safety (`?.`, `?:`, `!!` justifié)
- [ ] Smart casts utilisés
- [ ] Extension functions quand approprié
- [ ] Sealed classes pour états (optionnel)

**Commande Lint** :
```bash
./gradlew lint
```

---

## 📚 Documentation
### Fichiers de Documentation
- [x] **README.md** complet avec instructions
- [x] **ARCHITECTURE.md** détaillé
- [x] **DESIGN_GUIDE.md** pour UI/UX
- [x] **TEST_PLAN.md** complet
- [x] **VIEWMODEL_SPECS.md** pour spécifications
- [x] **PERSISTENCE_PLAN.md** pour DataStore

### Commentaires dans le Code
- [ ] Commentaires **KDoc** sur toutes les classes publiques
- [ ] Commentaires **KDoc** sur toutes les fonctions publiques
- [ ] Commentaires inline sur logique complexe
- [ ] Pas de commentaires évidents (éviter `// incrémente i`)
- [ ] Commentaires à jour (pas de code commenté obsolète)

**Exemple KDoc** :
```kotlin
/**
 * Description de la fonction
 * 
 * @param param1 Description du paramètre
 * @return Description du retour
 */
fun maFonction(param1: String): Boolean { }
```

---

## 🏗️ Architecture & Design

### MVVM Pattern
- [ ] Séparation claire Model/View/ViewModel
- [ ] View ne contient PAS de logique métier
- [ ] ViewModel ne contient PAS de références à la View
- [ ] Model est indépendant de l'UI
- [ ] Repository pattern implémenté

### Compose Best Practices
- [ ] Composables réutilisables
- [ ] State hoisting appliqué
- [ ] Pas de side effects dans `@Composable` (sauf `LaunchedEffect`)
- [ ] `remember` et `rememberSaveable` utilisés correctement
- [ ] Pas de logique métier dans les Composables

### Gestion d'État
- [ ] Single Source of Truth (SSOT)
- [ ] StateFlow ou MutableState utilisés
- [ ] Pas de duplication d'état
- [ ] État minimal (pas de valeurs dérivées stockées)

---

## Tests Manuels (à Étape 4)
- [ ] Add note fonctionne
- [ ] Delete note fonctionne
- [ ] Edit note fonctionne
- [ ] Persist fonctionne (restart app)
- [ ] UI responsive
- [ ] Pas de crashes

## 🧪 Tests

### Tests Unitaires
- [ ] Tous les tests passent
- [ ] Coverage > 80%
- [ ] Tests pour cas nominaux
- [ ] Tests pour edge cases
- [ ] Tests pour gestion d'erreurs
- [ ] Tests rapides (< 1s par test)

### Tests d'Intégration
- [ ] Cycle CRUD complet testé
- [ ] Persistance testée
- [ ] Navigation testée

### Tests Manuels
- [ ] App testée sur émulateur
- [ ] App testée sur appareil physique
- [ ] Testée en portrait ET paysage
- [ ] Testée avec différentes tailles d'écran
- [ ] Testée avec mode sombre ET clair

---

## ⚡ Performance

### Temps de Réponse
- [ ] App load time **< 2 secondes**
- [ ] Ajout note **< 500ms**
- [ ] Modification note **< 500ms**
- [ ] Suppression note **< 500ms**
- [ ] Chargement liste **< 1 seconde**

### Ressources
- [ ] Pas de **memory leaks** (vérifier avec Profiler)
- [ ] Utilisation RAM **< 100 MB** en moyenne
- [ ] Pas de **battery drain** excessif
- [ ] Scrolling **fluide** (60 fps)
- [ ] Pas de **ANR** (Application Not Responding)

**Outils** :
- Android Profiler (Memory, CPU)
- LeakCanary (memory leaks)
- Layout Inspector

---

## 🔒 Sécurité

### Données
- [ ] Pas de données sensibles **loggées**
- [ ] Input validation **OK** (titre, contenu)
- [ ] Pas d'injection possible (SQL, code)
- [ ] DataStore utilisé correctement

### Permissions
- [ ] **Aucune permission** inutile dans `AndroidManifest.xml`
- [ ] Permissions justifiées et documentées

### Code
- [ ] Pas de **hardcoded passwords** ou tokens
- [ ] Pas de **TODO** critiques non résolus
- [ ] Pas de `System.out.println()` (utiliser `Log`)

---

## 🎯 UI/UX

### Design
- [ ] Respect du **Material3** design
- [ ] Palette de couleurs **cohérente**
- [ ] Typographie **cohérente**
- [ ] Spacing **cohérent** (8dp, 16dp, 24dp)
- [ ] Thème sombre ET clair **implémentés**

### Utilisabilité
- [ ] Navigation **intuitive**
- [ ] Boutons **accessibles** (min 48x48 dp)
- [ ] Texte **lisible** (contraste WCAG AA)
- [ ] Feedback utilisateur (Toasts, messages)
- [ ] Empty state **géré** ("Aucune note")
- [ ] Loading state **géré** (si applicable)
- [ ] Error state **géré** (messages clairs)

### Accessibilité
- [ ] Content descriptions sur **toutes les icônes**
- [ ] Support **TalkBack** (lecteur d'écran)
- [ ] Contraste minimum **4.5:1** (texte normal)
- [ ] Contraste minimum **3:1** (texte large)
- [ ] Taille de police **respectée** (paramètres système)

---

## 🔧 Git & Versioning

### Repository
- [ ] Historique de commits **clair**
- [ ] Messages de commit **descriptifs** (`feat:`, `fix:`, `docs:`)
- [ ] Pas de gros commits (< 500 lignes)
- [ ] Pas de fichiers sensibles committés (`.env`, `secrets`)

### Structure
- [ ] `.gitignore` **correctement configuré**
- [ ] Pas de fichiers de build committés (`/build/`, `.gradle/`)
- [ ] Pas de fichiers IDE committés (`.idea/`, `*.iml`)
- [ ] Branches organisées (si applicable)

---

## 📱 Compatibilité

### Versions Android
- [ ] **minSdk 24** (Android 7.0) respecté
- [ ] **targetSdk 34** (Android 14) respecté
- [ ] Testée sur **Android 7** minimum
- [ ] Testée sur **Android 14** (dernière version)

### Appareils
- [ ] Testée sur **petit écran** (5 pouces)
- [ ] Testée sur **grand écran** (6.5 pouces)
- [ ] Testée en **portrait**
- [ ] Testée en **paysage**
- [ ] Rotation écran **préserve l'état**

---

## ✅ Checklist Finale avant Remise

### Code
- [ ] Tous les fichiers créés et committés
- [ ] Code compilé sans erreurs
- [ ] Code exécuté sans crashes
- [ ] Tous les tests passent

### Documentation
- [ ] README.md à jour
- [ ] ARCHITECTURE.md complet
- [ ] Commentaires KDoc présents
- [ ] Pas de TODOs critiques

### Tests
- [ ] Tests unitaires > 80% coverage
- [ ] Tests manuels effectués
- [ ] App testée sur au moins 2 appareils

### Performance
- [ ] Pas de memory leaks
- [ ] Temps de réponse acceptables
- [ ] Scrolling fluide

### UI/UX
- [ ] Design cohérent
- [ ] Navigation intuitive
- [ ] Accessibilité respectée

### Git
- [ ] Dernier commit poussé
- [ ] Repository propre (pas de fichiers indésirables)
- [ ] README accessible sur GitHub

---

## 📊 Score de Qualité

| Critère            | Points Max | Points Obtenus |
|--------------------|------------|----------------|
| Code Coverage      | 20         | -              |
| Conventions Kotlin | 15         | -              |
| Documentation      | 15         | -              |
| Architecture       | 15         | -              |
| Tests              | 15         | -              |
| Performance        | 10         | -              |
| UI/UX              | 10         | -              |
| **TOTAL**          | **100**    | **-**          |

**Objectif** : > 80/100

---

## 🚀 Amélioration Continue

### Actions Possibles
- [ ] Ajouter plus de tests
- [ ] Améliorer la documentation
- [ ] Optimiser les performances
- [ ] Refactoring si code complexe
- [ ] Ajouter animations UI
- [ ] Implémenter dark/light theme toggle
