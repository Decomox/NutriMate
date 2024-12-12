const { server, Server } = require('@hapi/hapi');
const handlers = require('../server/handler');
const inferenceService = require('../service/inferenceService'); 

const setActiveDietType = (server, dietType) => {
    server.app.currentDietType = dietType;
    console.log(`[INFO] Current diet type set to: ${dietType}`);
};

const handleDietType = (dietType) => {
  return async (req, h) => {
    setActiveDietType(req.server, dietType);
    const meals = await inferenceService.getMeals(dietType);
    const workouts = await inferenceService.getWorkouts(dietType);
    return { status: 'success', dietType, meals, workouts };
  };
};

const routes = [
  {
    method: 'GET',
    path: '/api/cutting',
    handler: handleDietType('Cutting'),
  },
  {
    method: 'GET',
    path: '/api/bulking',
    handler: handleDietType('Bulking'),
  },
  {
    method: 'GET',
    path: '/api/maintaining',
    handler: handleDietType('Maintaining'),
  },
  {
    method: 'GET',
    path: '/api/{dietType}/meals',
    handler: handlers.handleMealsByType,
  },
  {
    method: 'GET',
    path: '/api/{dietType}/workouts',
    handler: handlers.handleWorkoutsByType,
  },
  {
    path: '/predict',
    method: 'POST',
    handler: handlers.postPredictHandler,
    options: {
      payload: {
        allow: 'multipart/form-data',
        multipart: true,
        maxBytes: 10485760, // 10 MB
        parse: true,
      },
    },
  },
];

module.exports = routes;
