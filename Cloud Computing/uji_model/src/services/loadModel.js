const tf = require('@tensorflow/tfjs-node');

async function loadModel() {
    try {
        console.log('Starting model load...');
        const model = await tf.loadLayersModel('https://storage.googleapis.com/model_nutrimate/model.json');
        console.log('Model loaded successfully:', model.summary());
        return model;
    } catch (error) {
        console.error('Error loading model:', error.message);
        throw new Error('Failed to load the model. Please check the URL or the model format.');
    }
}

module.exports = loadModel;
