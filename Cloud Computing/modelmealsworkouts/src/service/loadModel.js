const tf = require('@tensorflow/tfjs-node');

async function loadModel() {
    try {
        console.log('Loading model from URL...');
        const model = await tf.loadLayersModel('https://storage.googleapis.com/model_1_nutrimate/model.json');
        console.log('Model metadata:', model);
        return model;
    } catch (error) {
        console.error('Failed to load model:', error.message);
        console.error('Error details:', error);
        throw new Error('Unable to load the model. Please check the URL or network connection.');
    }
}

module.exports = loadModel;
