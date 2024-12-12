const crypto = require('crypto');
const inferenceService = require('../service/inferenceService');
const { predictClassification } = inferenceService;

const postPredictHandler = async (request, h) => {
    try {
        const { image } = request.payload;
        const { model, currentDietType } = request.server.app; // Ambil currentDietType dari server state

        if (!image) {
            return h.response({
                status: 'fail',
                message: 'Image is required for prediction.',
            }).code(400);
        }

        if (!currentDietType) {
            return h.response({
                status: 'fail',
                message: 'No active diet type found. Please call one of the diet APIs first.',
            }).code(400);
        }

        const { confidenceScore, label } = await predictClassification(model, image);

        console.log(`[INFO] Predicted label: ${label}, active diet type: ${currentDietType}`);

        const meals = await inferenceService.getMeals(currentDietType.toLowerCase(), [label]);

        const id = crypto.randomUUID();
        const createdAt = new Date().toISOString();

        const data = {
            id,
            result: label,
            confidenceScore,
            createdAt,
            meals,
        };

        const message = confidenceScore > 99
            ? 'Model is predicted successfully.'
            : `Prediction confidence is below threshold (Score: ${confidenceScore.toFixed(2)}%). Please try a clearer picture.`;

        return h.response({
            status: 'success',
            message,
            data,
        }).code(201);
    } catch (err) {
        console.error('[ERROR] Prediction failed:', err.message);
        return h.response({
            status: 'error',
            message: 'Failed to process prediction.',
        }).code(500);
    }
};

const handleDietWithWorkouts = async (req, h, dietType) => {
    try {
        console.log(`[INFO] Fetching data for dietType: ${dietType}`);

        const [meals, workouts] = await Promise.all([
            inferenceService.getMeals(dietType),
            inferenceService.getWorkouts(dietType),
        ]);

        if ((!meals || meals.length === 0) && (!workouts || workouts.length === 0)) {
            return h.response({
                status: 'fail',
                message: `No meals or workouts found for diet type: ${dietType}`,
            }).code(404);
        }

        return h.response({
            status: 'success',
            data: { meals, workouts },
        }).code(200);
    } catch (err) {
        console.error(`[ERROR] Failed to process ${dietType} Diet:`, err.message);
        return h.response({
            status: 'error',
            message: `Failed to process ${dietType} Diet.`,
        }).code(500);
    }
};

const handleMealsOrWorkouts = async (req, h, dietType, dataType) => {
    try {
        console.log(`[INFO] Fetching ${dataType} for dietType: ${dietType}`);

        const data = dataType === 'meals'
            ? await inferenceService.getMeals(dietType)
            : await inferenceService.getWorkouts(dietType);

        if (!data || data.length === 0) {
            return h.response({
                status: 'fail',
                message: `No ${dataType} found for diet type: ${dietType}`,
            }).code(404);
        }

        return h.response({
            status: 'success',
            data: { [dataType]: data },
        }).code(200);
    } catch (err) {
        console.error(`[ERROR] Failed to fetch ${dataType} for ${dietType}:`, err.message);
        return h.response({
            status: 'error',
            message: `Failed to fetch ${dataType} for ${dietType}.`,
        }).code(500);
    }
};

const handleCutting = (req, h) => handleDietWithWorkouts(req, h, 'Cutting');
const handleBulking = (req, h) => handleDietWithWorkouts(req, h, 'Bulking');
const handleMaintaining = (req, h) => handleDietWithWorkouts(req, h, 'Maintaining');

const handleMealsByType = (req, h) => {
    const { dietType } = req.params;
    return handleMealsOrWorkouts(req, h, dietType, 'meals');
};

const handleWorkoutsByType = (req, h) => {
    const { dietType } = req.params;
    return handleMealsOrWorkouts(req, h, dietType, 'workouts');
};

module.exports = {
    postPredictHandler,
    handleCutting,
    handleBulking,
    handleMaintaining,
    handleMealsByType,
    handleWorkoutsByType,
};
