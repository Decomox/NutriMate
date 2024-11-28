// middleware/authMiddleware.js
const jwt = require('jsonwebtoken');
const SECRET_KEY = 'c7a899adf87b667a51c75000a81ecc3b3d789fefc99be39ace09aef9219f8909';


exports.authMiddleware = (req, res, next) => {
  // Mendapatkan token dari header Authorization
  const token = req.headers.authorization?.split(' ')[1];

  // Menampilkan token di console (dalam fungsi middleware)
  console.log(token);

  // Jika token tidak ditemukan, kirim respons Unauthorized
  if (!token) {
    return res.status(401).json({ message: 'Unauthorized' });
  }

  try {
    // Verifikasi token menggunakan jwt
    const decoded = jwt.verify(token, SECRET_KEY);
    req.user = decoded; // Tambahkan data decoded ke req.user
    next();
  } catch (error) {
    // Jika token tidak valid
    return res.status(401).json({ message: 'Invalid token' });
  }
};
