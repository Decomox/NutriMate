const jwt = require('jsonwebtoken');

const accsessValidation = async (request, h) => {
    const authorization = request.headers.authorization;

    // Periksa apakah header Authorization tersedia
    if (!authorization) {
        return h
            .response({ message: 'Token diperlukan' })
            .code(401)
            .takeover();
    }

    // Pastikan token memiliki format yang benar (Bearer <token>)
    const parts = authorization.split(' ');
    if (parts.length !== 2 || parts[0] !== 'Bearer') {
        return h
            .response({ message: 'Format token salah' })
            .code(401)
            .takeover();
    }

    const token = parts[1];
    const secret = process.env.JWT_SECRET;

    // Pastikan kunci rahasia tersedia
    if (!secret) {
        console.error('JWT_SECRET tidak diatur dalam environment');
        return h
            .response({ message: 'Konfigurasi server bermasalah' })
            .code(500)
            .takeover();
    }

    // Log token yang diterima
    console.log('Token yang diterima:', token);

    try {
        // Verifikasi token dengan algoritma yang sesuai (misalnya HS256)
        const jwtDecode = jwt.verify(token, secret, { algorithms: ['HS256'] });
        console.log('Decoded JWT:', jwtDecode); // Log hasil decoding token

        // Simpan data pengguna di request jika token valid
        request.userData = jwtDecode;
    } catch (error) {
        console.error('JWT Error:', error);

        // Tampilkan pesan kesalahan yang lebih spesifik berdasarkan jenis error
        const errorMessage =
            error.name === 'TokenExpiredError'
                ? 'Token telah kedaluwarsa'
                : error.name === 'JsonWebTokenError'
                ? 'Token tidak valid'
                : 'Terjadi kesalahan saat memverifikasi token';

        return h
            .response({ message: errorMessage })
            .code(401)
            .takeover();
    }

    return h.continue;
};

module.exports = accsessValidation;
