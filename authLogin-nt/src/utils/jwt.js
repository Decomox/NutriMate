const jwt = require('jsonwebtoken'); // Menggunakan library jsonwebtoken
require('dotenv').config(); // Memuat environment variable dari .env file

const SECRET_KEY = process.env.JWT_SECRET || 'c4e7082f1d3e6d13bca9f33d8b4b2421b14a38ef46c228a03c9d5d823d9e4b62';

// Middleware untuk memverifikasi token
exports.authMiddleware = (req, res, next) => {
  const token = req.headers.authorization?.split(' ')[1];
  if (!token) {
    return res.status(401).json({ message: 'Unauthorized' });
  }

  try {
    const decoded = jwt.verify(token, SECRET_KEY);
    req.user = decoded; // Simpan informasi user yang terdekode ke dalam req.user
    next();
  } catch (error) {
    return res.status(401).json({ message: 'Invalid token' });
  }
};

// Generate token
exports.generateToken = (payload) => {
  return jwt.sign(payload, SECRET_KEY, { expiresIn: '1h' });
};

// Verifikasi token
exports.verifyToken = (token) => {
  try {
    return jwt.verify(token, SECRET_KEY);
  } catch (error) {
    return null; // Jika token tidak valid, kembalikan null
  }
};
