const inferenceService = require('../service/inferenceService');

// Handler for Cutting Diet
const handleCuttingDiet = async (req, h) => {
    try {
        const { mealType } = req.query; // e.g., 'Breakfast', 'Lunch', 'Dinner'
        const recipes = await inferenceService.getRecipes('Cutting', mealType);
        const workouts = await inferenceService.getWorkouts('Cutting');
        return h.response({ recipes, workouts }).code(200);
    } catch (err) {
        console.error('Error in Cutting Diet Handler:', err);
        return h.response({ error: 'Failed to process Cutting Diet' }).code(500);
    }
};

// Handler for Bulking Diet
const handleBulkingDiet = async (req, h) => {
    try {
        const { mealType } = req.query; // e.g., 'Breakfast', 'Lunch', 'Dinner'
        const recipes = await inferenceService.getRecipes('Bulking', mealType);
        const workouts = await inferenceService.getWorkouts('Bulking');
        return h.response({ recipes, workouts }).code(200);
    } catch (err) {
        console.error('Error in Bulking Diet Handler:', err);
        return h.response({ error: 'Failed to process Bulking Diet' }).code(500);
    }
};

// Handler for Maintaining Diet
const handleMaintainingDiet = async (req, h) => {
    try {
        const { mealType } = req.query; // e.g., 'Breakfast', 'Lunch', 'Dinner'
        const recipes = await inferenceService.getRecipes('Maintaining', mealType);
        const workouts = await inferenceService.getWorkouts('Maintaining');
        return h.response({ recipes, workouts }).code(200);
    } catch (err) {
        console.error('Error in Maintaining Diet Handler:', err);
        return h.response({ error: 'Failed to process Maintaining Diet' }).code(500);
    }
};

module.exports = {
    handleCuttingDiet,
    handleBulkingDiet,
    handleMaintainingDiet,
};
