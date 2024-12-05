require('dotenv').config();

const Hapi = require('@hapi/hapi');
const routes = require('../server/routes');
const loadModel = require('../services/loadModel');
const InputError = require('../exceptions/InputError');

process.on('unhandledRejection', (reason) => {
    console.error('Unhandled Rejection:', reason);
});

process.on('uncaughtException', (error) => {
    console.error('Uncaught Exception:', error);
});

(async () => {
    const server = Hapi.server({
        port: process.env.PORT || 3000,
        host: process.env.HOST || '0.0.0.0',
        routes: {
            cors: {
                origin: ['*'],
            },
        },
    });

    try {
        const model = await loadModel(); // Load model dari URL
        server.app.model = model; // Simpan model ke server.app
        console.log('Model loaded successfully.');
    } catch (error) {
        console.error('Error loading model:', error.message);
        process.exit(1);
    }

    server.route(routes);

    server.ext('onPreResponse', (request, h) => {
        const response = request.response;

        if (response instanceof InputError) {
            const newResponse = h.response({
                status: 'fail',
                message: `${response.message} Please try another image.`,
            });
            newResponse.code(response.statusCode || 400);
            return newResponse;
        }

        if (response.isBoom) {
            const newResponse = h.response({
                status: 'fail',
                message: response.message,
            });
            newResponse.code(response.output.statusCode || 500);
            return newResponse;
        }

        return h.continue;
    });

    await server.start();
    console.log(`Server running on ${server.info.uri}`);
})();
