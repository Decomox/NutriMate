const admin = require('firebase-admin');
const { Storage } = require('@google-cloud/storage');
const path = require('path');

const firestore = admin.firestore();
const storage = new Storage();
const bucketName = 'your-bucket-name'; // Ganti dengan nama bucket Anda

/**
 * Unggah CSV ke Firestore dan simpan URL gambar
 * @param {Buffer} fileBuffer - File CSV dalam bentuk buffer
 */
const uploadCsvToFirestore = async (fileBuffer) => {
    const data = await csvHandler.parseCsv(fileBuffer);

    for (const item of data) {
        // Proses gambar dan unggah ke Google Cloud Storage
        if (item.image_url) {
            const imagePath = `images/${Date.now()}-${path.basename(item.image_url)}`;
            const [uploadedFile] = await storage.bucket(bucketName).upload(item.image_url, { destination: imagePath });
            item.image_url = uploadedFile.metadata.mediaLink;
        }

        // Simpan data ke Firestore
        const docRef = firestore.collection('meals').doc();
        await docRef.set(item);
    }
};

/**
 * Ambil data dari Firestore berdasarkan koleksi dan filter
 * @param {string} collection - Nama koleksi Firestore
 * @param {string} field - Nama field untuk filter
 * @param {string} value - Nilai field untuk filter
 * @returns {Array} Daftar dokumen yang cocok
 */
const getDataFromFirestore = async (collection, field, value) => {
    try {
        const snapshot = await firestore.collection(collection).where(field, '==', value).get();
        if (snapshot.empty) return [];
        return snapshot.docs.map((doc) => doc.data());
    } catch (err) {
        console.error(`Error fetching data from Firestore (${collection}):`, err);
        throw err;
    }
};

/**
 * Ambil data makanan dan workout dari Firestore
 * @param {string} dietType - Tipe diet (Cutting, Bulking, Maintaining)
 * @returns {Object} Data makanan dan workout
 */
const getDietAndWorkout = async (dietType) => {
    try {
        const meals = await getDataFromFirestore('meals', 'diet_type', dietType);
        const workouts = await getDataFromFirestore('workouts', 'diet_type', dietType);

        // Pilih makanan acak
        const randomMeals = (count) => meals.sort(() => 0.5 - Math.random()).slice(0, count);

        return {
            breakfast: randomMeals(3),
            lunch: randomMeals(3),
            dinner: randomMeals(3),
            workouts: workouts,
        };
    } catch (err) {
        console.error('Error fetching diet and workout:', err);
        throw err;
    }
};

module.exports = {
    uploadCsvToFirestore,
    getDietAndWorkout,
    getDataFromFirestore,
};
