const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const SECRET_KEY = 'your_secret_key'; // Sama seperti di middleware
const users = []; // Array untuk menyimpan data pengguna (gunakan database di produksi)

// Endpoint untuk registrasi
exports.register = async (req, res) => {
  const { email, password, name } = req.body;

  // Cek jika email sudah digunakan
  if (users.find(user => user.email === email)) {
    return res.status(400).json({ 
      status: 'gagal',
      message: 'Email already exists' });
  }

  // Hash password dan simpan user
  const hashedPassword = await bcrypt.hash(password, 10);
  users.push({ email, password: hashedPassword, name });

  res.status(201).json({ 
    status: 'berhasil',
    message: 'Registrasi anda Berhasil' 
  });
};

// Endpoint untuk login
// authController.js
exports.login = async (req, res) => {
    const { email, password } = req.body;
  
    const user = users.find(user => user.email === email);
    if (!user) {
      return res.status(404).json({
        status: 'gagal', 
        message: 'email tidak terdaftar' 
      
      });
    }
  
    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
      return res.status(401).json({ 
        status: 'gagal',
        message: 'Password salah' });
    }
  
    // Buat token dengan email dan name
    const token = jwt.sign(
      { email: user.email, name: user.name },
      SECRET_KEY,
      { expiresIn: '1h' }
    );
  
    res.status(200).json({ 
      status: 'berhasil',
      message: 'Login berhasil', 
      token });
};
  

// Endpoint untuk mendapatkan profil pengguna
exports.getProfile = (req, res) => {
    const { email, name } = req.user;
  
    res.status(200).json({
      message: 'Profile fetched successfully',
      user: { email, name }
    });
};
