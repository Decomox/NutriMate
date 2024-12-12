# Gunakan Node.js sebagai base image
FROM node:18.18.0

# Set working directory
WORKDIR /app


# Salin package.json dan package-lock.json untuk menginstal dependensi
COPY package*.json ./

# Install dependencies
RUN npm install

# Salin seluruh kode aplikasi
COPY ./src /app/src
COPY ./server.js /app/

# Ekspose port aplikasi
EXPOSE 4000

# Tambahkan variabel environment (opsional)
ENV gugel=default_value
ENV jwt=default_value


ENV PORT=4000

ENV HOST=0.0.0.0

# Jalankan aplikasi
CMD ["npm", "run", "start"]

