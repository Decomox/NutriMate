const { Firestore } = require('@google-cloud/firestore');

// Pastikan Anda memiliki credential Google Cloud yang valid
const firestore = new Firestore({
    projectId: 'intricate-gamma-443612-g6', // Ganti dengan Project ID Anda
});

module.exports = firestore;
