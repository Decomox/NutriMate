const predictClassification = require('../services/inferenceService');
const crypto = require('crypto');

async function postPredictHandler(request, h) {
    const { image } = request.payload;
    const { model } = request.server.app;

    try {
        const { confidenceScore, label } = await predictClassification(model, image);
        const id = crypto.randomUUID();
        const createdAt = new Date().toISOString();

        const data = {
            id,
            result: label,
            confidenceScore,
            createdAt,
        };

        const response = h.response({
            status: 'success',
            message: confidenceScore >= 50 
                ? 'Prediction successful.' 
                : 'Prediction confidence is below the acceptable threshold.',
            data,
        });
        response.code(201);
        return response;
    } catch (error) {
        const response = h.response({
            status: 'fail',
            message: error.message,
        });
        response.code(500);
        return response;
    }
}

module.exports = postPredictHandler;
