const tf = require('@tensorflow/tfjs-node');
const { Firestore } = require('@google-cloud/firestore');
const firestore = new Firestore();

// Fungsi untuk mendapatkan dataset resep_makanan berdasarkan diet_type dan meal_time
const getMeals = async (dietType, mealTime) => {
    try {
        const mealsSnapshot = await firestore
            .collection('resep_makanan')
            .where('diet_type', '==', dietType)
            .where('meal_time', '==', mealTime)
            .get();
        
        return mealsSnapshot.docs.map((doc) => doc.data());
    } catch (err) {
        console.error(`Error fetching meals for ${mealTime} (${dietType}):`, err);
        throw err;
    }
};

// Fungsi untuk mendapatkan dataset workout berdasarkan diet_type
const getWorkouts = async (dietType) => {
    try {
        const workoutsSnapshot = await firestore
            .collection('workouts')
            .where('diet_type', '==', dietType)
            .get();

        return workoutsSnapshot.docs.map((doc) => doc.data());
    } catch (err) {
        console.error(`Error fetching workouts for ${dietType}:`, err);
        throw err;
    }
};

// Fungsi utama untuk mendapatkan rekomendasi makanan dan olahraga
const getDietAndWorkout = async (dietType) => {
    try {
        // Mendapatkan rekomendasi makanan berdasarkan waktu makan
        const breakfastMeals = await getMeals(dietType, 'breakfast');
        const lunchMeals = await getMeals(dietType, 'lunch');
        const dinnerMeals = await getMeals(dietType, 'dinner');
        const workouts = await getWorkouts(dietType);

        // Struktur respons
        return {
            status: 'success',
            message: `Recommendations for ${dietType} diet`,
            data: {
                meals: {
                    breakfast: breakfastMeals.map((meal) => ({
                        nama_resep: meal.nama_resep,
                        image_url: meal.image_url,
                        bahan_resep: meal.bahan_resep,
                        url_masak: meal.url_masak,
                        steps: meal.steps,
                        calories: meal.calories,
                        fat: meal.fat,
                        carbohydrates: meal.carbohydrates,
                        protein: meal.protein,
                        diet_type: meal.diet_type,
                    })),
                    lunch: lunchMeals.map((meal) => ({
                        nama_resep: meal.nama_resep,
                        image_url: meal.image_url,
                        bahan_resep: meal.bahan_resep,
                        url_masak: meal.url_masak,
                        steps: meal.steps,
                        calories: meal.calories,
                        fat: meal.fat,
                        carbohydrates: meal.carbohydrates,
                        protein: meal.protein,
                        diet_type: meal.diet_type,
                    })),
                    dinner: dinnerMeals.map((meal) => ({
                        nama_resep: meal.nama_resep,
                        image_url: meal.image_url,
                        bahan_resep: meal.bahan_resep,
                        url_masak: meal.url_masak,
                        steps: meal.steps,
                        calories: meal.calories,
                        fat: meal.fat,
                        carbohydrates: meal.carbohydrates,
                        protein: meal.protein,
                        diet_type: meal.diet_type,
                    })),
                },
                workouts: workouts.map((workout) => ({
                    workout_nama: workout.workout_nama,
                    url_gambar: workout.url_gambar,
                    duration: workout.duration,
                    target_muscles: workout. target_muscles,
                    description: workout.description,
                    equipment_needed:workout. equipment_needed,
                    perform: workout.perform,
                    diet_type: workout.diet_type,
                })),
            },
        };
    } catch (err) {
        console.error(`Error generating recommendations for ${dietType} diet:`, err);
        throw {
            status: 'error',
            message: `Failed to generate recommendations for ${dietType} diet`,
        };
    }
};

module.exports = { getDietAndWorkout };
