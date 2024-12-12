const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const firestore = require('../config/firestore');
const { Firestore } = require('@google-cloud/firestore');
const { success, error } = require('../utils/responseHelper');
const path = require('path');
const Hapi = require('@hapi/hapi');


// Recommendation Handler
const db = new Firestore({
    projectId: 'intricate-gamma-443612-g6',
});

// Helper untuk menghitung status BMI (untuk progress)
const calculateStatus = (weight, height) => {
    const bmi = (weight / ((height / 100) ** 2)).toFixed(1);
    let status = 'Belum ideal';

    if (bmi < 18.5) {
        status = 'Kurus';
    } else if (bmi >= 18.5 && bmi <= 24.9) {
        status = 'Ideal';
    } else if (bmi >= 25 && bmi <= 29.9) {
        status = 'Gemuk';
    } else {
        status = 'Obesitas';
    }

    return { bmi, status };
};

// Auth Handler
const register = async (request, h) => {
    try {
        const { name, email, password } = request.payload;

        if (!name || !email || !password || password.length < 8) {
            return h.response({ message: 'Invalid input: name, email, and password (min. 8 characters) are required.' }).code(400);
        }

        // Hash password
        const hashedPassword = await bcrypt.hash(password, 10);

        // Add user to Firestore
        const docRef = db.collection('users').doc();
        await docRef.set({
            id: docRef.id,
            name,
            email,
            password: hashedPassword,
        });

        return success(h, 'User registered successfully', { id: docRef.id, name, email }, 201);
    } catch (err) {
        console.error('Register error:', err);
        return error(h, 'Internal server error', err.message);
    }
};

const login = async (request, h) => {
    try {
        const { email, password } = request.payload;

        if (!email || !password) {
            return h.response({ message: 'Email and password are required.' }).code(400);
        }

        // Find user by email
        const snapshot = await db.collection('users').where('email', '==', email).get();
        if (snapshot.empty) {
            return h.response({ message: 'User not found.' }).code(404);
        }

        const user = snapshot.docs[0].data();

        // Verify password
        const isPasswordValid = await bcrypt.compare(password, user.password);
        if (!isPasswordValid) {
            return h.response({ message: 'Incorrect password.' }).code(403);
        }

        // Generate JWT token
        const payload = { id: user.id, name: user.name };
        const secret = process.env.JWT_SECRET || 'fallbackSecret';
        const token = jwt.sign(payload, secret, { expiresIn: '1h' });

        return success(h, 'Login successful', { user: payload, token });
    } catch (err) {
        console.error('Login error:', err);
        return error(h, 'Internal server error', err.message);
    }
};

// Handler untuk GET /users
const getAllUsers = async (request, h) => {
    try {
        const snapshot = await db.collection('users').get();
        const users = snapshot.docs.map(doc => doc.data());

        return h.response({
            message: 'Users retrieved successfully',
            users,
        }).code(200);
    } catch (err) {
        console.error('Error getting users:', err);
        return h.response({
            message: 'Error retrieving users',
            error: err.message,
        }).code(500);
    }
};

// Handler untuk DELETE /users/{id}
const deleteUser = async (request, h) => {
    try {
        const { id } = request.params;

        // Periksa apakah ID pengguna ada
        const docRef = db.collection('users').doc(id);
        const doc = await docRef.get();

        if (!doc.exists) {
            return h.response({ message: 'User not found' }).code(404);
        }

        // Hapus pengguna dari Firestore
        await docRef.delete();

        return h.response({ message: 'User deleted successfully' }).code(200);
    } catch (err) {
        console.error('Error deleting user:', err);
        return h.response({ message: 'Error deleting user', error: err.message }).code(500);
    }
};

// Progress Handler
const createProgress = async (request, h) => {
    try {
        const { weight, height, title = 'Progress Baru' } = request.payload;

        // Validasi input
        if (!weight || !height) {
            return h.response({ message: 'Weight and height are required!' }).code(400);
        }

        // Hitung status berdasarkan BMI
        const { bmi, status } = calculateStatus(weight, height);

        // Simpan data ke Firestore
        const docRef = db.collection('progress').doc();
        await docRef.set({
            title,
            weight,
            height,
            status,
            bmi, // BMI tetap disimpan untuk keperluan internal
            createdAt: new Date().toISOString(),
        });

        // Respon berhasil tanpa menyertakan BMI
        return h.response({
            message: 'Progres baru berhasil disimpan!',
            progress: { title, weight: `${weight} kg`, height: `${height} cm`, status },
        }).code(201);
    } catch (error) {
        console.error('Error saving progress:', error);
        return h.response({ message: 'Internal server error', error: error.message }).code(500);
    }
};


const getRecommendation = async (request, h) => {
    try {
        // Debugging: Periksa apakah payload ada
        console.log('Received Payload:', request.payload);

        // Validasi keberadaan payload
        if (!request.payload) {
            console.error('Error: Payload is undefined');
            return h.response({
                message: 'Payload is missing or undefined',
            }).code(400);
        }

        const { weight, height, gender, age, activityLevel } = request.payload;

        // Validasi input
        if (!weight || !height || !gender || !age || !activityLevel) {
            return h.response({
                message: 'Incomplete input data',
            }).code(400);
        }
        if (height <= 0 || isNaN(height)) {
            return h.response({
                message: 'Height must be a positive number.',
            }).code(400);
        }
        if (weight <= 0 || isNaN(weight)) {
            return h.response({
                message: 'Weight must be a positive number.',
            }).code(400);
        }
        if (age <= 0 || isNaN(age)) {
            return h.response({
                message: 'Age must be a positive number.',
            }).code(400);
        }
        if (!['male', 'female'].includes(gender.toLowerCase())) {
            return h.response({
                message: 'Gender must be either "male" or "female".',
            }).code(400);
        }

        // Kalkulasi BMI
        const bmi = weight / ((height / 100) ** 2);

        // Faktor aktivitas
        const activityFactors = {
            ringan: 25,
            sedang: 30,
            berat: 35,
        };
        const defaultActivityFactor = 25;

        // Menentukan tingkat aktivitas
        const activityFactor = activityFactors[activityLevel.toLowerCase()] || defaultActivityFactor;

        // Kalkulasi kalori
        const mempertahankanCalories = bmi * activityFactor;
        const menaikkanCalories = mempertahankanCalories + 500;
        const menurunkanCalories = mempertahankanCalories - 500;

        // Menentukan program berdasarkan BMI
        const program = bmi < 18.5 ? 'menaikkan' : bmi > 25 ? 'menurunkan' : 'mempertahankan';

        // Status berdasarkan program
        const statusMap = {
            menaikkan: 'Gemuk',
            menurunkan: 'Kurus',
            mempertahankan: 'Ideal',
        };
        const status = statusMap[program] || 'Ideal';

        // Simpan ke Firestore
        const recommendationsRef = db.collection('recommendations');
        const docRef = await recommendationsRef.add({
            weight,
            height,
            gender,
            age,
            activityLevel,
            bmi,
            program,
            createdAt: new Date().toISOString(),
        });

        // Response
        return h.response({
            message: 'Recommendation generated successfully',
            data: {
                id: docRef.id,
                mempertahankan: mempertahankanCalories,
                menaikkan: menaikkanCalories,
                menurunkan: menurunkanCalories,
                program: program,
                status: status, // Status baru
            },
        }).code(200);
    } catch (error) {
        console.error('Error in getRecommendation:', error.message);
        return h.response({
            message: 'Error in recommendation calculation',
            error: error.message,
        }).code(500);
    }
};




// Export handlers
module.exports = {
    register,
    login,
    createProgress,
    getRecommendation,
    getAllUsers,
    deleteUser, // Pastikan deleteUser diekspor
};
