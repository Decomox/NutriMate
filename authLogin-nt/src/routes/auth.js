const express = require('express');
const router = express.Router();
const { register, login, getProfile } = require('../controllers/authController');
const { authMiddleware } = require('../middleware/authMiddleware');

// Routes untuk register, login, dan profile
router.post('/register', register);
router.post('/login', login);
router.get('/profile', authMiddleware, getProfile);

module.exports = router;
