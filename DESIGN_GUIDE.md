# Guide de Design - MemoScribe

## Palette de Couleurs
- Primary: #6200EE (violet)
- Secondary: #03DAC6 (cyan)
- Background: #FFFFFF (blanc)
- Surface: #F5F5F5 (gris clair)
- NoteCardBackground: #FFFACD (jaune clair)

## Typographie
- Headlines: 24sp Bold
- Titles: 18sp SemiBold
- Body: 14sp Regular
- Labels: 12sp Medium

## Spacing
- Small: 8dp
- Medium: 16dp
- Large: 24dp

## Composants Principaux
- NoteCard: 
    - Surface (avec titre) avec élevation,
    - Titre en gras (titleMedium),
    - Contenu preview (bodyMedium), 
    - Dates (créée + modifiée) (labelSmall),
    - Background: NoteCardBackground,
    - Padding: 16dp,
    - Corner radius: 8dp
- FloatingActionButton: 
    - Icône "+" (Add) pour ajouter note,
    - Couleur: Primary,
    - Position: bas-droit de l'écran,
    - Élévation: 6dp
- TextField: Pour titre et contenu
    - Outline style,
    - Label flottant,
    - Couleur: Primary,
    - Corner radius: 4dp
- Button: Valider/Annuler/Supprimer/Éditer
    - Filled pour action principale (Valider)
    - Outlined pour action secondaire (Annuler)
    - Text pour action destructive (Supprimer)

## Comportement
- Liste des Notes :
    - Les notes s'affichent en LazyColumn (scrollable),
    - Espacement entre notes: 8dp,
    - Animation smooth au scroll,
    - Pull to refresh (optionnel)
- Navigation :
    - Clic sur note → Édition (pré-remplissage des champs),
    - FAB → Ajout (champs vides),
    - Bouton retour → Retour à liste,
    - Bouton Supprimer en mode édition (optionnel car ça dépend de chacun de nous)
    - Swipe/Long-press → Suppression
    - Transitions smooth (fluides) entre écrans
- Etats :
    - Empty state: Message centré + icône,
    - Loading state: CircularProgressIndicator centré,
    - Error state: Message d'erreur + bouton retry

## Distinction Ajouter vs Éditer
- AJOUTER : 
    - Champs vides, 
    - Titre header : "Ajouter une note"
    - 2 boutons : Annuler (Outlined), Valider (Filled), 
    - PAS de bouton Supprimer,
    - PAS d'affichage des dates
- ÉDITER : 
    - Champs pré-remplis, 
    - Titre header : "Éditer la note",
    - 3 boutons : Annuler (Outlined), Valider (Filled), Supprimer (Text/Red), 
    - Affiche "Créée le : DD/MM/YYYY HH:MM",
    - Affiche "Modifiée le : DD/MM/YYYY HH:MM"

## Accessibilité
- Contraste minimum: 4.5:1 (texte normal)
- Contraste minimum: 3:1 (texte large)
- Taille minimum boutons: 48x48 dp 
- Content descriptions sur icônes 
- Support TalkBack

## Responsive
- Supporte orientation portrait et paysage
- Adaptation automatique des espacements
- Texte wrappable (pas de overflow)