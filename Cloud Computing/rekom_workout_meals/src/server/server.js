require('dotenv').config();

const Hapi = require('@hapi/hapi');
const routes = require('../server/routes');

// Penanganan error global
process.on('unhandledRejection', (reason) => {
    console.error('Unhandled Rejection:', reason);
});

process.on('uncaughtException', (error) => {
    console.error('Uncaught Exception:', error);
});

(async () => {
    const server = Hapi.server({
        port: 3000,
        host: '0.0.0.0',
        routes: {
            cors: {
                origin: ['*'], // Izinkan semua origin
            },
        },
    });

    try {
        await server.start();
        console.log(`Server running on ${server.info.uri}`);
    } catch (err) {
        console.error('Failed to start server:', err);
        process.exit(1); // Keluar jika server gagal dimulai
    }
});

server.route(routes);

// Menangani unhandled rejections secara global
process.on('unhandledRejection', (err) => {
    console.error('Unhandled Rejection:', err);
    process.exit(1);
});

// Jalankan server
init();
