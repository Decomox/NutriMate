const Hapi = require('@hapi/hapi');
const routes = require('./src/routes/routes');
require('dotenv').config();

(async () => {
    try {
        const server = Hapi.server({
            port: process.env.PORT || 4000,
            host: '0.0.0.0',
            routes: {
                cors: {
                    origin: ['*'], // Mengizinkan semua domain
                },
            },
        });

        // Daftarkan semua route ke server
        routes(server);

        // Mulai server
        await server.start();
        console.log('Server running on %s', server.info.uri);
    } catch (err) {
        console.error('Error starting server:', err);
        process.exit(1);
    }
})();
