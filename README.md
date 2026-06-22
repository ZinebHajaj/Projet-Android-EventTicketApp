<img width="1878" height="757" alt="image" src="https://github.com/user-attachments/assets/c0a7603c-007e-47c5-8ae3-bef6f8bb8ab0" />Je vais créer un fichier README.md complet pour ton projet **EventTicketApp** basé sur tout ce qu'on a développé ensemble. Voici le contenu :

---

```markdown
# Projet-Android-EventTicketApp

## 👥 Membres de l'équipe

| Nom       | Prénom     | 
|-----------|------------|
  Hajaj        Zineb
  EL Mellas   Manar

---

## 📋 Description du projet

**EventTicketApp** est une application mobile Android de billetterie événementielle permettant aux
utilisateurs de découvrir, réserver et gérer des billets pour des événements (concerts, sports, théâtre, ateliers).
L'application intègre un système de paiement sécurisé (Stripe, PayPal), une gestion multi-devises (MAD, EUR, USD)
et un espace organisateur pour la création et la gestion d'événements.

---

## ✨ Fonctionnalités principales

### 🔐 Authentification & Profil
- Inscription / Connexion classique (email + mot de passe)
- Connexion via **Google Sign-In** (SSO)
- Connexion via **Facebook Sign-In** (SSO)
- Gestion du profil utilisateur

### 🏠 Accueil & Navigation
- Écran d'accueil avec événements en vedette
- Barre de navigation inférieure (Accueil, Événements, Favoris, Billets, Profil)
- Recherche d'événements par catégorie et mot-clé

### 🎫 Réservation & Paiement
- Sélection de sièges par catégorie (VIP, Standard, Économique)
- Sélection de créneaux horaires (Ateliers)
- Gestion des devises (MAD, EUR, USD) avec conversion automatique
- Paiement via **Stripe** (carte bancaire)
- Paiement via **PayPal**
- Génération de QR Code pour les billets

### 📱 Espace Organisateur
- Création d'événements (titre, ville, date, prix, catégorie, image)
- Modification d'événements existants
- Suppression d'événements avec confirmation
- Visualisation de la liste des événements créés

### 🎨 Design & UX
- Design System unifié avec thème orange
- Interface adaptative (responsive)
- Animations et transitions fluides

---

## 🏗️ Architecture du projet

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                    │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────────┐  │
│  │  Auth   │ │  Home   │ │ Booking │ │  Organizer  │  │
│  │ Screens │ │ Screens │ │ Screens │ │   Screens   │  │
│  └────┬────┘ └────┬────┘ └────┬────┘ └──────┬──────┘  │
│       │            │            │               │         │
│  ┌────┴────────────┴────────────┴───────────────┴───┐  │
│  │              UI LAYER (Jetpack Compose)           │  │
│  │         ViewModels (Hilt) + StateFlow (MVI)        │  │
│  └─────────────────────┬───────────────────────────────┘  │
│                        │                                 │
│  ┌─────────────────────┴───────────────────────────────┐  │
│  │              DOMAIN LAYER (Use Cases)               │  │
│  └─────────────────────┬───────────────────────────────┘  │
│                        │                                 │
│  ┌─────────────────────┴───────────────────────────────┐  │
│  │              DATA LAYER                           │  │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐  │  │
│  │  │  API    │ │  Room   │ │ Firestore│ │Storage │  │  │
│  │  │Retrofit │ │  Cache  │ │  (NoSQL) │ │(Images)│  │  │
│  │  └─────────┘ └─────────┘ └─────────┘ └─────────┘  │  │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────────────────┐  │  │
│  │  │  Auth   │ │ Ticket  │ │   EventRepository   │  │  │
│  │  │Repository│ │Repository│ │  (CRUD + Firebase)  │  │  │
│  │  └─────────┘ └─────────┘ └─────────────────────┘  │  │
│  └─────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

---

## 🛠️ Technologies utilisées

| Couche | Technologie |
|--------|-------------|
| **Langage** | Kotlin |
| **UI** | Jetpack Compose |
| **Architecture** | MVI (Model-View-Intent) |
| **Injection de dépendances** | Hilt |
| **Base de données locale** | Room |
| **Stockage local** | DataStore (préférences) |
| **API REST** | Retrofit + Gson |
| **Backend** | Firebase (Auth, Firestore, Storage) |
| **Paiement** | Stripe SDK, PayPal WebView |
| **SSO** | Google Sign-In, Facebook Login |
| **Images** | Coil |
| **QR Code** | ZXing |

---

## 📁 Structure du projet

```
com.dcc.eventticketapp/
├── data/
│   ├── Api/              # Retrofit API (EventApi)
│   ├── Database/         # Room (EventDatabase, EventDao)
│   ├── DI/               # Hilt Modules (DatabaseModule, FirebaseModule)
│   ├── Entities/         # Modèles de données (EventModel, User, TicketModel, SeatTier)
│   ├── Preferences/     # DataStore (CurrencyPreference)
│   └── Repository/      # Repositories (AuthRepository, EventRepository, TicketRepository)
├── ui/
│   ├── auth/            # Login, Register, SSO (Google, Facebook)
│   ├── booking/         # Booking flow (Seat selection, Payment, Confirmation)
│   ├── category/        # Category filter
│   ├── eventDetail/     # Event details screen
│   ├── events/          # Events list with search
│   ├── favorites/       # Favorites (collègue)
│   ├── home/            # Home screen with BottomNavBar
│   ├── navigation/      # AppNavigation (NavHost)
│   ├── organizer/       # Organizer space (CRUD events)
│   ├── profile/         # Profile screen (collègue)
│   ├── splash/          # Splash screen with animation
│   ├── theme/           # Colors, Typography, Theme
│   └── ticket/          # Tickets list with QR Code
├── utils/
│   ├── CurrencyConverter.kt   # MAD/EUR/USD conversion
│   └── QrCodeGenerator.kt     # QR Code generation
├── MainActivity.kt
└── MyApplication.kt      # Hilt + Stripe initialization
```

---

## 📦 Dépendances

Ajoutez dans `build.gradle.kts` (Module: app) :

```kotlin
dependencies {
    // Android Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // Compose
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    
    // Stripe
    implementation("com.stripe:stripe-android:20.36.0")
    
    // Facebook Login
    implementation("com.facebook.android:facebook-login:16.3.0")
    
    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Coil (Images)
    implementation("io.coil-kt:coil-compose:2.6.0")
    
    // ZXing (QR Code)
    implementation("com.google.zxing:core:3.5.2")
}
```

---

## 🚀 Installation et exécution

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/ZinebHajaj/Projet-Android-EventTicketApp.git
   ```

2. **Ouvrir dans Android Studio**
   - File → Open → Sélectionner le dossier du projet

3. **Synchroniser Gradle**
   - Cliquer sur "Sync Now" dans la barre d'outils

4. **Configurer Firebase**
   - Ajouter `google-services.json` dans `app/`
   - Activer Authentication, Firestore, Storage

5. **Configurer Stripe**
   - Remplacer la clé publique dans `MyApplication.kt`

6. **Lancer l'application**
   - Sélectionner un émulateur ou appareil
   - Cliquer sur "Run"

---

## 📸 Captures d'écran

### Écran de connexion
![Login Screen](screenshots/login.png)

### Écran d'accueil
![Home Screen](screenshots/home.png)

### Détail d'événement
![Event Detail](screenshots/event_detail.png)

### Réservation de sièges
![Booking Screen](screenshots/booking.png)

### Paiement Stripe
![Payment Screen](screenshots/payment.png)

### Confirmation
![Confirmation Screen](screenshots/confirmation.png)

### QR Code Billet
![Ticket QR](screenshots/ticket_qr.png)

### Espace Organisateur
![Organizer Home](screenshots/organizer_home.png)

### Création d'événement
![Create Event](screenshots/create_event.png)

### Liste des événements
![My Events](screenshots/my_events.png)

---

## 🎯 Points forts du projet

| Exigence | Implémentation |
|----------|---------------|
| **Architecture MVI** | ✅ Intent → ViewModel → StateFlow → UI |
| **Hilt DI** | ✅ Injection dans tous les ViewModels et Repositories |
| **Room + Firestore** | ✅ Cache local + Backend cloud |
| **SSO Google/Facebook** | ✅ OAuth2 avec Firebase |
| **Multi-devises** | ✅ MAD/EUR/USD avec conversion |
| **Paiement Stripe/PayPal** | ✅ Intégration complète |
| **QR Code** | ✅ Génération et affichage |
| **Organisateur** | ✅ CRUD événements avec Firebase |
| **Design System** | ✅ Thème unifié avec Material3 |

---

## 📝 Notes

- L'application nécessite une connexion Internet pour Firebase et les paiements
- Les images des événements sont stockées en URI locale (amélioration possible : Firebase Storage)
- La gestion des places est synchronisée via Firestore pour éviter l'overbooking

---

## Captures d'écran trello:
<img width="1918" height="847" alt="image" src="https://github.com/user-attachments/assets/5910afc3-e153-4244-be6a-012aa3a2a191" />
<img width="1870" height="762" alt="image" src="https://github.com/user-attachments/assets/06dcd9b4-390a-4ad3-be96-14731852b8fb" />
<img width="1902" height="833" alt="image" src="https://github.com/user-attachments/assets/94e67238-2c63-449a-b29d-26bcef5d1f80" />
<img width="1906" height="860" alt="image" src="https://github.com/user-attachments/assets/eccaa565-2046-4f98-bee8-95650547e10b" />
<img width="1890" height="858" alt="image" src="https://github.com/user-attachments/assets/65dfaedd-185d-4f1f-bb59-b2de722aacf9" />
<img width="1905" height="850" alt="image" src="https://github.com/user-attachments/assets/aa4d3355-1d75-46d3-a0f8-6fdd149da97f" />


## 📚 Ressources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Stripe Android SDK](https://stripe.com/docs/payments/accept-a-payment?platform=android)
- [Hilt Documentation](https://developer.android.com/training/dependency-injection/hilt-android)

---

Projet réalisé dans le cadre du module **Développement Mobile** - 22/06/2026
```
