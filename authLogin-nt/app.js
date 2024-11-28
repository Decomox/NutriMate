const express = require('express');
const authRoutes = require('./routes/auth');
require('dotenv').config();

const app = express();
app.use(express.json()); // Parsing JSON

app.get('/', (req, res) => {
  res.send('Welcome to the API!');
});

app.get('/auth/register', (req, res) => {
  res.send('Welcome to the Register!');
});

app.get('/auth/login', (req, res) => {
  res.send('Welcome to the Login!');
});

// Route API
app.use('/auth', authRoutes);

app.use(express.json());

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
