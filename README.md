# MemoScribe - Application "Bloc-Notes Simple" avec Jetpack Compose

![Status](https://img.shields.io/badge/Status-En%20D%C3%A9veloppement-yellow)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Kotlin-purple)

## 📝 Description
MemoScribe est une application Android simple et efficace pour prendre des notes avec persistance locale.
Vous pouvez créer, éditer, supprimer et gérer vos notes facilement avec une interface moderne Material3.

**Projet Académique** : INF355 - Développement d'applications mobiles

---

## ✨ Fonctionnalités
- ✅ Ajouter des notes avec titre et contenu
- ✅ Afficher liste de toutes les notes
- ✅ Éditer les notes existantes
- ✅ Supprimer des notes
- ✅ Persistance locale avec DataStore
- ✅ Affichage des dates (création et dernière modification)
- ✅ Interface Material3 moderne et responsive
- ✅ Validation des entrées utilisateur
- ✅ Mode sombre/clair automatique

---

## 🛠️ Technologies Utilisées
- Kotlin
- Jetpack Compose
- DataStore
- Gson
- Material3

| Technologie         | Version | Usage              |
|---------------------|---------|--------------------|
| **Kotlin**          | 1.9+    | Langage principal  |
| **Jetpack Compose** | 1.5+    | Framework UI       |
| **Material3**       | Latest  | Design system      |
| **DataStore**       | 1.0+    | Persistance locale |
| **Gson**            | 2.10+   | Sérialisation JSON |
| **ViewModel**       | 2.6+    | Architecture MVVM  |
| **Coroutines**      | 1.7+    | Asynchrone         |

---

## 🏗️ Architecture
- **Pattern**: MVVM (Model-View-ViewModel)
- **Persistance**: DataStore Preferences
- **UI Framework**: Jetpack Compose
- **Sérialisation**: Gson

```
┌─────────────────────────────────┐
│         View (UI)               │
│  • NoteListScreen               │
│  • AddEditNoteScreen            │
│  • NoteCard                     │
└────────────┬────────────────────┘
             │
             ▼
┌─────────────────────────────────┐
│       ViewModel                 │
│  • NoteViewModel                │
│  • Gestion d'état               │
└────────────┬────────────────────┘
             │
             ▼
┌─────────────────────────────────┐
│       Repository                │
│  • NoteRepository               │
│  • DataStore access             │
└────────────┬────────────────────┘
             │
             ▼
┌─────────────────────────────────┐
│         Model                   │
│  • Note (data class)            │
└─────────────────────────────────┘
```

Voir [ARCHITECTURE.md](ARCHITECTURE.md) pour plus de détails.

---

## 📦 Installation & Setup

### Prérequis
- Android Studio Arctic Fox ou plus récent
- JDK 11 ou supérieur
- Android SDK 24+ (Android 7.0)
- Émulateur Android ou appareil physique

### Étapes d'Installation

1. **Cloner le dépôt**
```bash
git clone https://github.com/JonasMBAKOP/MemoScribe.git
cd MemoScribe
```

2. **Ouvrir dans Android Studio**
- File → Open
- Sélectionner le dossier `MemoScribe`

3. **Sync Gradle**
- Android Studio synchronisera automatiquement
- Attendre la fin du téléchargement des dépendances

4. **Build & Run**
- Cliquer sur le bouton ▶️ (Run)
- Sélectionner un émulateur ou appareil
- L'app se lance !

---

## 🎯 Utilisation

### Ajouter une Note
1. Cliquer sur le bouton **+** (FloatingActionButton) en bas à droite
2. Entrer un **titre** et un **contenu**
3. Cliquer sur **Valider**
4. La note apparaît dans la liste

### Éditer une Note
1. Cliquer sur la **note** dans la liste
2. Modifier le **titre** et/ou le **contenu**
3. Cliquer sur **Valider**
4. Les changements sont sauvegardés

### Supprimer une Note
1. Cliquer sur la **note** (mode édition)
2. Cliquer sur le bouton **Supprimer**
3. Confirmer la suppression
4. La note disparaît de la liste

### Persistance
- Toutes les notes sont **automatiquement sauvegardées**
- Fermer et rouvrir l'app : **les notes sont toujours là** ✅

---

## 📂 Structure du Projet

```
MemoScribe/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/example/memoscribe/
│   │   │   │   ├── model/
│   │   │   │   │   └── Note.kt
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── NoteViewModel.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── NoteRepository.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── NoteListScreen.kt
│   │   │   │   │   │   └── AddEditNoteScreen.kt
│   │   │   │   │   ├── components/
│   │   │   │   │   │   └── NoteCard.kt
│   │   │   │   │   └── theme/
│   │   │   │   │       ├── Color.kt
│   │   │   │   │       ├── Type.kt
│   │   │   │   │       └── Theme.kt
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   │       └── values/
│   │   │           └── strings.xml
│   │   └── test/
│   │       └── kotlin/
│   │           └── NoteTest.kt
│   └── build.gradle.kts
├── ARCHITECTURE.md
├── DESIGN_GUIDE.md
├── TEST_PLAN.md
└── README.md
```

---

## 👥 Équipe de Développement
- Chef de Projet / Architecte
- Développeur UI/UX
- Développeur Logique Métier 1
- Développeur Logique Métier 2
- Testeur / Documentaliste

| Rôle                             | Responsabilités                      |
|----------------------------------|--------------------------------------|
| **Chef de Projet / Architecte**  | Architecture MVVM, Git, coordination |
| **Développeur UI/UX**            | Interface Compose, thème Material3   |
| **Développeur Logique Métier 1** | Modèle Note, opérations CRUD         |
| **Développeur Logique Métier 2** | Persistance DataStore, Repository    |
| **Testeur / Documentaliste**     | Tests, documentation, qualité        |

---

## 🧪 Tests

### Exécuter les Tests Unitaires

```bash
# Tous les tests
./gradlew test

# Tests spécifiques
./gradlew test --tests NoteTest
```

### Coverage des Tests

Actuellement : **~40%** (Model tests uniquement)

Objectif : **> 80%** (après ÉTAPE 3)

Voir [TEST_PLAN.md](TEST_PLAN.md) pour plus de détails.

---

## 📈 Roadmap

### ✅ ÉTAPE 1 : Planification (Complétée)
- Architecture MVVM définie
- Thème Material3 créé
- Modèle Note implémenté
- Repository DataStore créé
- Documentation complète

### ⏳ ÉTAPE 2 : Développement UI (En cours)
- Créer NoteListScreen
- Créer AddEditNoteScreen
- Créer NoteCard
- Navigation entre écrans

### ⏳ ÉTAPE 3 : Logique Métier (À venir)
- Implémenter NoteViewModel
- Connecter UI au ViewModel
- Implémenter persistance
- Tests unitaires complets

### ⏳ ÉTAPE 4 : Tests et Finalisation (À venir)
- Tests d'intégration
- Tests manuels
- Correction de bugs
- Préparation présentation

---

## 🐛 Bugs Connus

Aucun pour le moment (ÉTAPE 1 complétée).

---

## 📄 Documentation

- [ARCHITECTURE.md](ARCHITECTURE.md) - Architecture détaillée du projet
- [DESIGN_GUIDE.md](DESIGN_GUIDE.md) - Guide de design UI/UX
- [TEST_PLAN.md](TEST_PLAN.md) - Plan de tests complet
- [VIEWMODEL_SPECS.md](VIEWMODEL_SPECS.md) - Spécifications du ViewModel
- [PERSISTENCE_PLAN.md](PERSISTENCE_PLAN.md) - Plan de persistance

---

## 🤝 Contribution

Ce projet est académique. Pour contribuer :

1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'feat: Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

---

## 📜 Licence

MIT License - Voir [LICENSE](LICENSE) pour plus de détails.

---

## 📧 Contact

**Projet Académique** - INF355

GitHub : [https://github.com/JonasMBAKOP/MemoScribe](https://github.com/JonasMBAKOP/MemoScribe)

---

## 🙏 Remerciements

- Professeur du cours INF355
- Équipe de développement
- Communauté Jetpack Compose
- Documentation Android officielle

---

**Fait avec ❤️ en Kotlin**