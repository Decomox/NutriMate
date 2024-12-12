const admin = require('firebase-admin');

const tf = require('@tensorflow/tfjs-node');
const InputError = require('../exceptions/InputError');

if (admin.apps.length === 0) {
    admin.initializeApp({
        credential: admin.credential.applicationDefault(),
        databaseURL: 'https://intricate-gamma-443612-g6-default-rtdb.asia-southeast1.firebasedatabase.app/' // Ganti dengan URL Realtime Database Anda
    });
}

const db = admin.database();

async function predictClassification(model, image) {
    try {
        const tensor = tf.node
            .decodeJpeg(image)
            .resizeNearestNeighbor([150, 150])
            .expandDims()
            .toFloat()
 
        const classes = ['Bawang Bombai','Daging Ayam','Daging Sapi','Daun Bawang','Kubis Merah','Telur','Terong','Timun','Tomat','Wortel'];
 
        const prediction = model.predict(tensor);
        const score = await prediction.data();
        const confidenceScore = Math.max(...score) * 100;
 
        const classResult = tf.argMax(prediction, 1).dataSync()[0];
        const label = classes[classResult];
  
        return { confidenceScore, label };
    } catch (error) {
        throw new InputError(`Terjadi kesalahan input: ${error.message}`)
    }
}

let currentDietType = null; 

const setDietType = (dietType) => {
    currentDietType = dietType; // Set dietType yang aktif
    console.info(`[INFO] Current dietType set to: ${currentDietType}`);
};

const getMeals = async (dietType = currentDietType, selectedIngredients = []) => {
    try {
        if (!dietType) {
            console.warn(`[WARNING] No diet type specified. Please set the diet type before fetching meals.`);
            return [];
        }

        console.info(`[INFO] Fetching meals for dietType: ${dietType}`);
        const snapshot = await db.ref('meals').once('value');
        const mealsData = snapshot.val();

        if (!mealsData) {
            console.warn('No meals data found in the database.');
            return [];
        }

        const normalizedSelectedIngredients = selectedIngredients.map(ingredient => ingredient.toLowerCase());

        const filteredMeals = Object.keys(mealsData)
            .map(key => mealsData[key])
            .filter(meal =>
                meal.diet_type &&
                meal.diet_type.toLowerCase() === dietType.toLowerCase() &&
                normalizedSelectedIngredients.every(selectedIngredient =>
                    meal.bahan_resep_pilihan
                        ?.toLowerCase()
                        ?.split(',')
                        .map(bahan => bahan.trim())
                        .includes(selectedIngredient)
                )
            );

        if (filteredMeals.length === 0) {
            console.warn(`[INFO] No meals found for diet type: ${dietType} with selected ingredients: ${selectedIngredients}`);
        } else {
            console.info(`[INFO] Found ${filteredMeals.length} meals for diet type: ${dietType}`);
        }

        return filteredMeals;
    } catch (err) {
        console.error(`Error fetching meals for dietType: ${dietType}`, err.message);
        throw err;
    }
};

const getWorkouts = async (dietType = null) => {
    try {
        const snapshot = await db.ref('workouts').once('value');
        const workoutsData = snapshot.val();

        if (!workoutsData) {
            console.warn('No workouts data found in the database.');
            return [];
        }

        const filteredWorkouts = Object.keys(workoutsData)
            .map(key => workoutsData[key])
            .filter(workout => workout.diet_type && workout.diet_type.toLowerCase() === dietType.toLowerCase()); // Filter berdasarkan diet_type


        if (filteredWorkouts.length === 0) {
            console.warn(`No workouts found for diet type: ${dietType}`);
        } else {
            console.info(`[INFO] Found workouts for diet type: ${dietType}:`);
        }

        return filteredWorkouts;
    } catch (err) {
        console.error(`Error fetching workouts for ${dietType}:`, err.message);
        throw err;
    }
};


module.exports = { getMeals, setDietType, getWorkouts, predictClassification };
