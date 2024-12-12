require('dotenv').config();

const Hapi = require('@hapi/hapi');
const routes = require('../server/routes');
const loadModel = require('../service/loadModel'); // Impor fungsi loadModel
const InputError = require('../exceptions/InputError');

// Fungsi untuk mengatur diet type aktif
const setActiveDietType = (server, dietType) => {
    server.app.currentDietType = dietType;
    console.log(`[INFO] Current diet type set to: ${dietType}`);
};

// Penanganan error global
process.on('unhandledRejection', (reason) => {
    console.error('Unhandled Rejection:', reason);
});

process.on('uncaughtException', (error) => {
    console.error('Uncaught Exception:', error);
});

(async () => {
    // Membuat instance server
    const server = Hapi.server({
        port: 3000,
        host: '0.0.0.0',
        routes: {
            cors: { origin: ['*'] }, // Allow all origins
        },
    });

    // Memuat model
    try {
        const model = await loadModel();
        server.app.model = model;
    } catch (error) {
        console.error('Error loading model:', error.message);
        process.exit(1); // Keluar jika model gagal dimuat
    }

    // Mendefinisikan route untuk server
    server.route(routes.map((route) => {
        if (route.path === '/api/cutting') {
            return {
                ...route,
                handler: (req, h) => {
                    setActiveDietType(server, 'Cutting');
                    return route.handler(req, h);
                },
            };
        }
        if (route.path === '/api/bulking') {
            return {
                ...route,
                handler: (req, h) => {
                    setActiveDietType(server, 'Bulking');
                    return route.handler(req, h);
                },
            };
        }
        if (route.path === '/api/maintaining') {
            return {
                ...route,
                handler: (req, h) => {
                    setActiveDietType(server, 'Maintaining');
                    return route.handler(req, h);
                },
            };
        }
        return route;
    }));

    // Global error handling
    server.ext('onPreResponse', (request, h) => {
        const response = request.response;

        if (response instanceof InputError) {
            const newResponse = h.response({
                status: 'fail',
                message: `${response.message} Silakan gunakan foto lain.`,
            });
            newResponse.code(response.statusCode);
            return newResponse;
        }

        if (response.isBoom) {
            const newResponse = h.response({
                status: 'fail',
                message: response.message,
            });
            newResponse.code(response.output.statusCode);
            return newResponse;
        }

        return h.continue;
    });

    // Menjalankan server
    try {
        await server.start();
        console.log(`Server running on ${server.info.uri}`);
    } catch (err) {
        console.error('Failed to start server:', err);
        process.exit(1); // Exit jika server gagal start
    }
})();
