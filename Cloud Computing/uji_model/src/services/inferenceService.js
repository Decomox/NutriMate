const tf = require('@tensorflow/tfjs-node');
const InputError = require('../exceptions/InputError');

async function predictClassification(model, image) {
    try {
        // Decode gambar ke tensor
        const tensor = tf.node
            .decodeJpeg(image)
            .resizeNearestNeighbor([224, 224]) // Pastikan sesuai inputShape model
            .expandDims()
            .toFloat()
            .div(255.0); // Normalisasi 0-1

        const classes = ['Bawang Bombai', 'Daging Ayam', 'Daging Sapi', 'Daun Bawang', 'Kubis Merah', 'Telur', 'Terong', 'Timun', 'Tomat', 'Wortel'];

        // Prediksi gambar
        const prediction = model.predict(tensor);
        const scores = await prediction.data(); // Ambil array probabilitas
        const maxScore = Math.max(...scores);
        const maxIndex = scores.indexOf(maxScore);

        // Label prediksi berdasarkan skor tertinggi
        const label = classes[maxIndex];

        return {
            confidenceScore: maxScore * 100, // Skor dalam persentase
            label,
        };
    } catch (error) {
        throw new InputError(`Prediction failed: ${error.message}`);
    }
}

module.exports = predictClassification;
