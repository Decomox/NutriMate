const handlers = require('../server/handler');

const routes = [
    {
        method: 'POST',
        path: '/api/cutting',
        handler: handlers.handleCuttingDiet,
    },
    {
        method: 'POST',
        path: '/api/bulking',
        handler: handlers.handleBulkingDiet,
    },
    {
        method: 'POST',
        path: '/api/maintaining',
        handler: handlers.handleMaintainingDiet,
    },
];

module.exports = routes;
