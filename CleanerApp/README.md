# 🧹 CleanerApp — Android 2026

A production-ready Android application for booking professional cleaning services, built like Swiggy but for cleaners.

---

## 📱 Screenshots Overview

| Splash | Login | Register | User Home | Cleaner Dashboard | Booking |
|--------|-------|----------|-----------|-------------------|---------|
| ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

---

## 🚀 Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Build System | Gradle (Groovy DSL) |
| Architecture | MVVM + Repository Pattern |
| Database | Room (SQLite ORM) |
| DI | Hilt |
| UI | Material 3 + ViewBinding |
| Location | FusedLocationProviderClient |
| Notifications | Firebase Cloud Messaging (FCM) |
| Async | Java ExecutorService |
| State | LiveData |
| Session | SharedPreferences |

---

## 📂 Project Structure

```
CleanerApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/cleanerapp/
│   │   │   ├── CleanerApp.java              ← Application class (Hilt)
│   │   │   ├── data/
│   │   │   │   ├── db/
│   │   │   │   │   ├── AppDatabase.java     ← Room DB + seeder
│   │   │   │   │   ├── dao/                 ← UserDao, CleanerServiceDao, BookingDao, CategoryDao
│   │   │   │   │   └── entities/            ← UserEntity, CleanerServiceEntity, BookingEntity, CategoryEntity
│   │   │   │   └── repository/              ← UserRepository, CleanerRepository, BookingRepository
│   │   │   ├── di/                          ← AppModule, RepositoryModule (Hilt)
│   │   │   ├── model/                       ← AuthResult
│   │   │   ├── ui/
│   │   │   │   ├── auth/                    ← SplashActivity, LoginActivity, RegisterActivity
│   │   │   │   ├── user/                    ← UserHomeActivity, CleanerListAdapter
│   │   │   │   ├── cleaner/                 ← CleanerDashboardActivity, MyServicesAdapter
│   │   │   │   └── booking/                 ← BookingActivity, BookingListAdapter
│   │   │   ├── utils/                       ← SessionManager, ValidationUtils, LocationHelper, CleanerFCMService
│   │   │   └── viewmodel/                   ← AuthViewModel, CleanerViewModel, BookingViewModel
│   │   ├── res/
│   │   │   ├── layout/                      ← All XML layouts
│   │   │   ├── values/                      ← colors, strings, themes
│   │   │   ├── menu/                        ← nav_menu
│   │   │   ├── drawable/                    ← icons, backgrounds
│   │   │   └── mipmap-*/                    ← Launcher icons
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── google-services.json                 ← ⚠️ REPLACE with your Firebase config
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

---

## ⚙️ Setup Instructions

### Step 1 — Open in Android Studio
1. Extract the ZIP
2. Open **Android Studio** (Hedgehog 2023.1.1 or later)
3. `File → Open` → select the `CleanerApp` folder
4. Wait for Gradle sync to complete

### Step 2 — Firebase Setup (Required for FCM notifications)
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project named `CleanerApp`
3. Add an Android app with package name: `com.cleanerapp`
4. Download `google-services.json`
5. **Replace** `app/google-services.json` with your downloaded file

> **Note:** The app works fully without Firebase — only push notifications will be disabled. The placeholder `google-services.json` allows the project to compile.

### Step 3 — Run
- Connect a device (Android 8.0+, API 26+) or start an emulator
- Click ▶️ **Run**

---

## 🗄️ Database Schema

### `users`
| Column | Type | Notes |
|--------|------|-------|
| id | INTEGER PK | Auto-generate |
| name | TEXT | |
| email | TEXT | Unique |
| password | TEXT | |
| phone | TEXT | |
| role | TEXT | `USER` / `CLEANER` |
| created_at | INTEGER | Unix timestamp |

### `cleaner_services`
| Column | Type | Notes |
|--------|------|-------|
| id | INTEGER PK | |
| user_id | INTEGER FK | → users.id |
| cleaner_name | TEXT | |
| service_type | TEXT | From categories |
| description | TEXT | |
| price | REAL | |
| latitude | REAL | |
| longitude | REAL | |
| is_available | INTEGER | 0/1 |
| rating | REAL | Default 4.0 |
| created_at | INTEGER | |

### `bookings`
| Column | Type | Notes |
|--------|------|-------|
| id | INTEGER PK | |
| user_id | INTEGER | |
| cleaner_id | INTEGER | |
| service_id | INTEGER | |
| cleaner_name | TEXT | |
| service_type | TEXT | |
| price | REAL | |
| address | TEXT | |
| status | TEXT | `PENDING` / `CONFIRMED` / `COMPLETED` |
| created_at | INTEGER | |

### `service_categories`
| Column | Type | Notes |
|--------|------|-------|
| id | INTEGER PK | |
| name | TEXT | |
| icon_name | TEXT | |
| is_active | INTEGER | 0/1 |

> Categories are **seeded automatically** on first install:
> Car Cleaning, Home Cleaning, Apartment Cleaning, Office Cleaning, Glass Cleaning, Deep Cleaning, Sofa Cleaning

---

## 👤 User Flows

### User Flow
```
Splash → Login / Register (role: USER) → UserHomeActivity
  → Browse cleaners by category or search
  → Tap "Book Now" → BookingActivity (enter address → confirm)
  → View bookings via nav drawer
```

### Cleaner Flow
```
Splash → Login / Register (role: CLEANER) → CleanerDashboardActivity
  → Add services (type, price, description, auto-location)
  → Toggle availability on/off
  → View and manage incoming bookings (Confirm / Complete)
```

---

## 🔐 Crash Prevention Measures

- ✅ All DB operations run on background threads via `ExecutorService`
- ✅ Input validation before any DB call
- ✅ `try-catch` around all DB operations
- ✅ Null checks on location (`if location != null`)
- ✅ LiveData observes DB — no direct UI thread DB access
- ✅ `getActivity() != null` checked in fragments
- ✅ Hilt DI prevents manual lifecycle mistakes
- ✅ ViewBinding eliminates `NullPointerException` on views
- ✅ `ListAdapter` with `DiffUtil` prevents RecyclerView crashes

---

## 🔔 FCM Notification Flow

```
User books → BookingEntity saved in Room DB
           → CleanerFCMService receives message
           → showNotification() displays in status bar
```

To trigger manually from Firebase Console:
1. Go to Firebase → Cloud Messaging → Send test message
2. Enter cleaner's FCM token (saved in SharedPreferences)

---

## 🌍 Location Handling

- Requests `ACCESS_FINE_LOCATION` at runtime
- Uses `FusedLocationProviderClient.getCurrentLocation()` with `PRIORITY_HIGH_ACCURACY`
- Falls back to `getLastLocation()` if current unavailable
- If permission denied → shows manual input fallback

---

## 🎨 UI Components Used

| Component | Usage |
|-----------|-------|
| `MaterialCardView` | Cleaner items, booking items |
| `TextInputLayout` (Outlined) | All form fields |
| `MaterialButton` | Actions |
| `MaterialButtonToggleGroup` | Role selection |
| `ChipGroup` | Category filters |
| `NavigationView` | Side drawer |
| `ShimmerFrameLayout` | Loading state |
| `RecyclerView` + `ListAdapter` | All lists |

---

## 🔧 Customization Guide

### Add a new service category
```java
// Via DB (recommended)
ServiceCategoryEntity newCategory = new ServiceCategoryEntity("Pool Cleaning", "ic_pool");
db.serviceCategoryDao().insertCategory(newCategory);
// The UI ChipGroup auto-updates via LiveData
```

### Change app package name
1. Refactor → Rename in Android Studio
2. Update `applicationId` in `app/build.gradle`
3. Update `package_name` in `google-services.json`

---

## 📦 Dependencies Summary

```groovy
// Room
androidx.room:room-runtime:2.6.1

// Hilt DI
com.google.dagger:hilt-android:2.50

// Material 3
com.google.android.material:material:1.12.0

// Location
com.google.android.gms:play-services-location:21.3.0

// Firebase
firebase-bom:33.1.2 + firebase-messaging + firebase-analytics

// Shimmer
com.facebook.shimmer:shimmer:0.5.0

// Glide
com.github.bumptech.glide:glide:4.16.0
```

---

## 🛣️ Roadmap / Optional Features

- [ ] In-app chat between user & cleaner
- [ ] UPI / Razorpay payment integration
- [ ] Ratings & reviews system
- [ ] AI-based cleaner recommendation
- [ ] Dark mode support
- [ ] Google Maps distance calculation
- [ ] Cleaner profile photos (Glide + Firebase Storage)
- [ ] OTP-based phone authentication

---

## 📄 License

MIT License — Free to use for personal and commercial projects.

---

**Built with ❤️ using Java + Room + Hilt + Material 3**
