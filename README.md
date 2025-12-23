# FCM - Food Calorie Manager

A comprehensive Android application for tracking meals, monitoring calorie intake, and managing daily nutrition goals. This app helps users maintain a healthy lifestyle by providing detailed nutritional information for their meals.

## 📱 Features

### Core Features
- **Meal Logging**: Add, edit, and delete meals with detailed nutritional information
- **Calorie Tracking**: Monitor daily calorie intake against personalized goals
- **Nutritional Analysis**: View detailed breakdown of calories, proteins, fats, and carbohydrates
- **Date-based Organization**: Track meals by date with an intuitive calendar view
- **Meal Types**: Categorize meals as breakfast, lunch, dinner, or snacks
- **Image Support**: Attach photos to meals using Firebase Storage
- **API Integration**: Automatic nutritional data fetching via CalorieNinjas API
- **Offline Storage**: Local database using Room for persistent data storage
- **Progress Visualization**: Visual progress bars showing calorie consumption vs. goals

### Additional Features
- Anonymous Firebase Authentication
- Internet connectivity check
- Daily nutrition summaries
- Meal detail views with complete nutritional breakdown
- Portion size tracking
- Firebase Storage integration for meal images

## 🛠️ Technology Stack

### Android Framework
- **Language**: Java
- **Min SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 34 (Android 14)
- **Build Tool**: Gradle (Kotlin DSL)

### Libraries & Dependencies

#### Firebase Services
- Firebase Authentication (Anonymous Sign-in)
- Firebase Storage (Image storage)

#### Networking
- Retrofit 2.9.0 (HTTP client)
- Gson 2.8.8 (JSON serialization)
- OkHttp (HTTP interceptor)

#### Database
- Room (Local SQLite database)
- Room Compiler (Annotation processing)

#### UI Components
- AndroidX AppCompat
- Material Design Components
- ConstraintLayout
- RecyclerView

#### Testing
- JUnit (Unit testing)
- Espresso (UI testing)

### External APIs
- **CalorieNinjas API**: Provides nutritional information for food items

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

1. **Android Studio** (Arctic Fox or later recommended)
2. **JDK 8** or higher
3. **Android SDK** with API level 34
4. **Gradle** 7.0 or higher (usually bundled with Android Studio)
5. **Firebase Account** (for Firebase services)
6. **CalorieNinjas API Key** (free tier available - [Get your API key here](https://api-ninjas.com/api/nutrition))

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/risinsilv/FCM.git
cd FCM
```

### 2. Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use an existing one
3. Add an Android app to your Firebase project:
   - Package name: `com.example.fcm`
4. Download the `google-services.json` file
5. Place `google-services.json` in the `app/` directory

6. Enable Firebase Authentication:
   - In Firebase Console, go to Authentication
   - Enable "Anonymous" sign-in method

7. Enable Firebase Storage:
   - In Firebase Console, go to Storage
   - Click "Get Started" and set up Storage
   - Configure security rules as needed

### 3. API Key Configuration

⚠️ **Important Security Note**: The repository currently contains a hardcoded API key. You should replace it with your own key for security and usage tracking.

1. Sign up for a free API key at [API Ninjas - Nutrition API](https://api-ninjas.com/api/nutrition)
2. Open `app/src/main/java/com/example/fcm/ApiClient.java`
3. Replace the existing API key with your actual key:

```java
.header("X-Api-Key", "YOUR_API_KEY_HERE")
```

### 4. Build the Project

#### Using Android Studio:
1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to the cloned FCM directory
4. Wait for Gradle to sync
5. Click "Run" or press `Shift + F10`

#### Using Command Line:

```bash
# Grant execute permission to gradlew
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Run tests
./gradlew test
```

## 📖 Usage

### First Launch
1. The app requires an internet connection for initial setup
2. Firebase will authenticate you anonymously
3. You'll be directed to the main meal tracking screen

### Adding a Meal
1. Click the "Add Meal" button (+ icon)
2. Enter meal details:
   - Meal name
   - Portion size
   - Meal type (Breakfast, Lunch, Dinner, Snack)
3. (Optional) Add a photo of the meal
4. The app will automatically fetch nutritional information from CalorieNinjas API
5. Review and save the meal

### Setting Daily Goals
1. Navigate to the Nutrition screen
2. Click "Edit" to set your daily calorie goal
3. The app will track your progress throughout the day

### Viewing Meal Details
1. Tap on any meal card to view detailed information
2. See complete nutritional breakdown:
   - Calories
   - Proteins
   - Fats
   - Carbohydrates
3. Edit or delete meals as needed

### Tracking Progress
- View daily summaries with progress bars
- Check historical data by selecting different dates
- Monitor nutritional intake trends

## 📁 Project Structure

```
FCM/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/fcm/
│   │   │   │   ├── MainActivity.java          # Splash screen with auth
│   │   │   │   ├── recycleView.java           # Main meal list activity
│   │   │   │   ├── MealDetailActivity.java    # Individual meal details
│   │   │   │   ├── EditMeal.java              # Add/Edit meal screen
│   │   │   │   ├── Nutrition.java             # Daily nutrition summary
│   │   │   │   ├── MealSummaryActivity.java   # Meal statistics
│   │   │   │   ├── Meal.java                  # Meal entity model
│   │   │   │   ├── MealDAO.java               # Database access object
│   │   │   │   ├── MealDataBase.java          # Room database config
│   │   │   │   ├── DailyIntakeDataBase.java   # Daily intake tracking
│   │   │   │   ├── ApiClient.java             # Retrofit client setup
│   │   │   │   ├── CalorieNinjasApiService.java # API service interface
│   │   │   │   ├── NutritionResponse.java     # API response model
│   │   │   │   ├── NutritionItem.java         # Nutrition data model
│   │   │   │   ├── ImageStorage.java          # Firebase Storage handler
│   │   │   │   ├── DateUtils.java             # Date formatting utilities
│   │   │   │   └── recycleViewDateAdapter.java # RecyclerView adapter
│   │   │   ├── res/
│   │   │   │   ├── layout/                    # UI layouts
│   │   │   │   ├── drawable/                  # Images and graphics
│   │   │   │   ├── values/                    # Strings, colors, themes
│   │   │   │   └── mipmap-*/                  # App icons
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/                       # Instrumentation tests
│   │   └── test/                              # Unit tests
│   ├── build.gradle.kts                       # App-level build config
│   └── google-services.json                   # Firebase config (not in repo)
├── gradle/                                     # Gradle wrapper files
├── build.gradle.kts                           # Project-level build config
├── settings.gradle.kts                        # Project settings
├── gradle.properties                          # Gradle properties
└── README.md                                  # This file
```

## 🗄️ Database Schema

### Meal Table
The app uses Room database with the following schema:

**Table Name**: `Meal`

| Column | Type | Description |
|--------|------|-------------|
| id | Long (PK) | Auto-generated unique identifier |
| date | String | Date of the meal (format: YYYY-MM-DD) |
| meal_name | String | Name of the meal/food item |
| portion_size | Double | Serving size in grams |
| meal_type | String | Type: Breakfast, Lunch, Dinner, Snack |
| calories | Double | Total calories |
| fats | Double | Total fats in grams |
| proteins | Double | Total proteins in grams |
| carbohydrates | Double | Total carbohydrates in grams |
| image | String | Firebase Storage URL for meal photo |

### Daily Intake Table
Tracks daily calorie goals and progress.

## 🌐 API Integration

### CalorieNinjas API

The app integrates with CalorieNinjas API (via API Ninjas) to fetch nutritional information:

- **Base URL**: `https://api.calorieninjas.com/v1/`
- **Endpoint**: `nutrition`
- **Full URL**: `GET https://api.calorieninjas.com/v1/nutrition`
- **Method**: GET
- **Authentication**: API Key in header (`X-Api-Key`)

**Example Request**:
```
GET https://api.calorieninjas.com/v1/nutrition?query=apple
```

**Response includes**:
- Calories
- Protein
- Fat (total, saturated)
- Carbohydrates
- Sugar
- Fiber
- And more nutritional data

### Firebase Services

#### Authentication
- Anonymous authentication for seamless user experience
- No registration required

#### Storage
- Stores meal images in Firebase Cloud Storage
- Automatic image compression
- Secure access with Firebase Authentication

## 🏗️ Building for Production

### Generate Signed APK

1. In Android Studio: `Build` → `Generate Signed Bundle/APK`
2. Select `APK`
3. Create or select your keystore
4. Choose release build variant
5. Click `Finish`

### Release Build via Command Line

```bash
./gradlew assembleRelease
```

The APK will be generated at: `app/build/outputs/apk/release/app-release.apk`

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

## 🔒 Security Notes

⚠️ **CRITICAL SECURITY WARNING**: The repository currently contains a hardcoded API key in `ApiClient.java`. This is a security risk! For production:

1. **Remove hardcoded API keys** from source code
2. **Use BuildConfig** or environment variables
3. **Add API key to `.gitignore`** or use a secure configuration method
4. **Implement proper Firebase Security Rules** for Storage

### Recommended: Using BuildConfig for API Keys

In `app/build.gradle.kts`:
```kotlin
buildTypes {
    release {
        buildConfigField("String", "API_KEY", "\"${System.getenv("CALORIE_NINJA_API_KEY")}\"")
    }
}
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards
- Follow Java coding conventions
- Add comments for complex logic
- Write meaningful commit messages
- Test your changes before submitting

## 📝 License

This project is available for educational and personal use. Please check with the repository owner for commercial use permissions.

## 👥 Authors

- **risinsilv** - [GitHub Profile](https://github.com/risinsilv)

## 🙏 Acknowledgments

- [API Ninjas - Nutrition API](https://api-ninjas.com/api/nutrition) for providing the nutrition data service
- [Firebase](https://firebase.google.com) for backend services
- Android community for excellent libraries and tools

## 📞 Support

For issues, questions, or suggestions:
- Open an issue on [GitHub Issues](https://github.com/risinsilv/FCM/issues)
- Contact the repository owner

## 🗺️ Roadmap

Potential future enhancements:
- [ ] User accounts with Firebase Authentication
- [ ] Cloud sync across devices
- [ ] Weekly/Monthly nutrition reports
- [ ] Custom meal recipes
- [ ] Barcode scanning for packaged foods
- [ ] Water intake tracking
- [ ] Exercise logging
- [ ] Integration with fitness trackers
- [ ] Social features (share meals, challenges)
- [ ] Dark mode support
- [ ] Multi-language support

---

**Made with ❤️ for healthier living**
