# Tests du Repository à Implémenter (ÉTAPE 4)

## Tests Unitaires

### Test 1 : saveNotes avec liste vide
- Sauvegarder une liste vide
- Vérifier qu'aucune erreur

### Test 2 : saveNotes avec 1 note
- Créer 1 note
- Sauvegarder
- Charger
- Vérifier que la note est identique

### Test 3 : saveNotes avec plusieurs notes
- Créer 3 notes
- Sauvegarder
- Charger
- Vérifier que les 3 notes sont présentes

### Test 4 : getNotes avec DataStore vide
- DataStore vide (premier lancement)
- Charger
- Vérifier liste vide

### Test 5 : getNotes avec données valides
- Sauvegarder des notes
- Charger
- Vérifier identiques

### Test 6 : clearAllNotes
- Sauvegarder des notes
- Effacer tout
- Charger
- Vérifier liste vide

## Tests d'Intégration

### Test 7 : Cycle complet CRUD
- Ajouter 2 notes
- Modifier 1 note
- Supprimer 1 note
- Vérifier état final

### Test 8 : Persistance après restart
- Ajouter des notes
- Simuler restart (fermer/rouvrir)
- Vérifier notes toujours présentes