const handler = require('../handlers/handler');
const accsessValidation = require('../middleware/accsessValidation');

const routes = (server) => {
    // Auth Routes
    server.route([
        {
            method: 'POST',
            path: '/register',
            handler: handler.register,
        },
        {
            method: 'POST',
            path: '/login',
            handler: handler.login,
        },
        {
            method: 'GET',
            path: '/users',
            options: {
                pre: [{ method: accsessValidation }], // Middleware validasi akses
            },
            handler: handler.getAllUsers,
        },
        {
            method: 'DELETE',
            path: '/users/{id}',
            options: {
                pre: [{ method: accsessValidation }], // Middleware validasi akses
            },
            handler: handler.deleteUser,
        },
    ]);

    // Progress Routes
    server.route([
        {
            method: 'POST',
            path: '/api/progress',
            handler: handler.createProgress,
        },
    ]);

    // API Routes (Recommendation)
    server.route([
        {
            method: 'POST',
            path: '/api/recommendation',
            handler: async (request, h) => {
                try {
                    console.log('Received Payload:', request.payload);
    
                    // Panggil handler.getRecommendation dengan seluruh request
                    const recommendation = await handler.getRecommendation(request, h);
    
                    console.log('Generated Recommendation:', recommendation);
    
                    return h.response(recommendation).code(200);
                } catch (error) {
                    console.error('Error during recommendation processing:', error.message);
    
                    return h.response({
                        message: 'Internal server error',
                        error: error.message,
                    }).code(500);
                }
            },
        },
    ]);
    
};

module.exports = routes;
