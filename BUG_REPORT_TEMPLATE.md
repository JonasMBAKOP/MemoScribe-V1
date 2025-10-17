# Template - Bug Report

## 🐛 Titre du Bug
[Résumé court et descriptif du bug]

Exemple : "L'app crash lors de la suppression d'une note"

---

## 📋 Informations Générales

**Date de découverte** : [DD/MM/YYYY]

**Découvert par** : [Nom du testeur]

**Environnement** :
- [ ] Émulateur
- [ ] Appareil physique

**Reproductibilité** :
- [ ] Toujours (100%)
- [ ] Souvent (> 50%)
- [ ] Parfois (< 50%)
- [ ] Rarement (une seule fois)

---

## 🔴 Sévérité

Choisir UNE option :

- [ ] **Critique** - App crash, perte de données, fonctionnalité principale cassée
- [ ] **Majeure** - Fonctionnalité importante cassée, mais workaround existe
- [ ] **Mineure** - Bug cosmétique, impacts limités sur l'utilisation
- [ ] **Triviale** - Typo, alignement, couleur incorrecte

---

## 📱 Informations Appareil

**Modèle** : [Ex: Pixel 6, Samsung Galaxy S21]

**Version Android** : [Ex: Android 12]

**Version de l'app** : [Ex: 1.0]

**Résolution écran** : [Ex: 1080x2400]

**Orientation** :
- [ ] Portrait
- [ ] Paysage
- [ ] Les deux

---

## 🔄 Étapes de Reproduction

Décrire **exactement** comment reproduire le bug :

1. [Première action]
2. [Deuxième action]
3. [Troisième action]
4. [etc.]

**Exemple** :
1. Ouvrir l'app
2. Cliquer sur une note dans la liste
3. Cliquer sur le bouton "Supprimer"
4. Confirmer la suppression
5. → **BUG** : L'app crash

---

## ✅ Résultat Attendu

Décrire **ce qui devrait se passer** normalement :

[Description claire du comportement attendu]

**Exemple** :
"La note devrait être supprimée, un toast 'Note supprimée' devrait apparaître, et l'app devrait retourner à la liste des notes."

---

## ❌ Résultat Réel

Décrire **ce qui se passe réellement** :

[Description claire du comportement observé]

**Exemple** :
"L'app crash immédiatement avec un message 'Unfortunately, MemoScribe has stopped'."

---

## 📸 Captures d'Écran / Vidéo

[Ajouter des captures d'écran ou liens vers vidéos]

- Screenshot 1 : [Description]
- Screenshot 2 : [Description]
- Vidéo : [Lien]

**Comment capturer** :
- Screenshot : Power + Volume Down
- Vidéo : Android Studio → Logcat → Screen Record

---

## 📜 Logs (Logcat)
```
[Copier-coller les logs pertinents en Kotlin de Logcat]
```

**Comment obtenir les logs** :
1. Android Studio → Logcat (en bas)
2. Filter par "Error" ou par nom de l'app
3. Copier les lignes avec le timestamp du crash

**Exemple** :
```
2024-10-17 14:23:45.123 E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.example.memoscribe, PID: 12345
    java.lang.NullPointerException: Attempt to invoke virtual method on a null object reference
        at com.example.memoscribe.viewmodel.NoteViewModel.deleteNote(NoteViewModel.kt:45)
```

---

## 🔍 Informations Complémentaires

### Contexte
[Toute information supplémentaire utile]

**Exemples** :
- "Le bug se produit seulement quand il y a plus de 10 notes"
- "Le bug apparaît après avoir fait une rotation d'écran"
- "Le bug n'apparaît pas en mode sombre"

### Fréquence
[À quelle fréquence le bug apparaît-il ?]

### Impact Utilisateur
[Comment ce bug affecte-t-il l'expérience utilisateur ?]

**Exemples** :
- "Empêche totalement de supprimer des notes"
- "Force l'utilisateur à redémarrer l'app"
- "Perte de données possible"

---

## Attachments
[Screenshots, videos, etc.]

---

## 🛠️ Hypothèses / Pistes

[Si vous avez des idées sur la cause du bug]

**Exemples** :
- "Peut-être un problème de null safety dans deleteNote()"
- "Le ViewModel n'est peut-être pas correctement initialisé"
- "Conflit avec la sauvegarde DataStore ?"

---

## ✏️ Solution Proposée (Optionnel)

[Si vous avez une idée de fix]

**Exemple** :
"Ajouter une vérification null avant d'accéder à la note :"
```kotlin
val note = getNoteById(id)
if (note != null) {
    // supprimer
}
```

---

## 👤 Assigné à

**Responsable du fix** : [Nom du développeur]

**Priorité** : 
- [ ] Urgent (fix immédiat)
- [ ] Haute (fix dans les 24h)
- [ ] Moyenne (fix dans la semaine)
- [ ] Basse (fix quand possible)

---

## 📊 Statut

- [ ] **Nouveau** - Bug vient d'être découvert
- [ ] **Confirmé** - Bug reproduit et validé
- [ ] **En cours** - Fix en développement
- [ ] **Résolu** - Fix implémenté et committé
- [ ] **Vérifié** - Fix testé et validé
- [ ] **Fermé** - Bug complètement résolu

---

## 🔗 Liens Associés

**Commit de fix** : [Lien GitHub commit]

**Pull Request** : [Lien PR]

**Issue GitHub** : [Lien issue]

---

## ✅ Vérification du Fix

Une fois le bug corrigé, le testeur doit vérifier :

- [ ] Le bug ne se reproduit plus avec les étapes initiales
- [ ] Les fonctionnalités adjacentes ne sont pas cassées
- [ ] Aucune régression introduite
- [ ] Le fix fonctionne sur différents appareils
- [ ] Le fix fonctionne en portrait ET paysage

---

## 📝 Notes Additionnelles

[Toute autre information pertinente]

---

**Date de création** : [DD/MM/YYYY]

**Dernière mise à jour** : [DD/MM/YYYY]