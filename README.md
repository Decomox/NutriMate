# NutriMate - Personalized Nutrition & Exercise Guidance App

NutriMate is a mobile application designed to empower users to achieve their health goals through tailored nutrition and exercise recommendations. By leveraging advanced machine learning algorithms, the app provides personalized meal plans, workout routines, and progress tracking to encourage sustainable lifestyle changes. The app particularly focuses on addressing the health needs of vulnerable communities by making personalized health resources accessible and actionable.

---

## Features

- **Personalized Meal Recommendations**  
  Get daily meal plans tailored to your dietary preferences, health conditions, and nutritional goals.

- **Ingredient Recognition**  
  Use image recognition to identify food ingredients and generate recipe ideas based on what you have at home.

- **Exercise Recommendations**  
  Receive workout routines customized to your fitness level and goals.

- **Progress Tracking**  
  Monitor your progress towards your health and fitness objectives with detailed analytics.

- **User-Friendly Interface**  
  Intuitive and accessible design to ensure usability for a wide range of users.

---

## Technology Stack

- **Mobile Development**:  
  - Kotlin, Jetpack Compose, Android Studio
  - Room Database for local storage  
  - Retrofit for API integration  

- **Backend**:  
  - Firebase Authentication & Firestore  
  - Node.js with Express.js for custom APIs  

- **Machine Learning**:  
  - TensorFlow/Keras, PyTorch for model development  
  - TensorFlow Lite (TFLite) for mobile integration  

- **Cloud Infrastructure**:  
  - Firebase for hosting and cloud storage  
  - Google Cloud Vision API for image recognition  

---

## Installation and Setup

### Prerequisites
- Android Studio installed on your system
- Firebase project configured (refer to `firebase-config.json`)
- Node.js and npm installed for backend setup

### Steps to Run the Project

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/NutriMate.git
   cd NutriMate
