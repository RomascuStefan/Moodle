# Utilizăm imaginea de bază Python 3.10
FROM python:3.10-slim

# Setăm directorul de lucru
WORKDIR /app

# Instalăm pachetele necesare pentru mysqlclient
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    default-libmysqlclient-dev \
    pkg-config \
    && rm -rf /var/lib/apt/lists/*

# Copiem fișierele aplicației IDM
COPY . .

# Instalăm toate pachetele necesare
RUN pip install --no-cache-dir -r requirements.txt

# Expunem portul gRPC utilizat de IDM
EXPOSE 50051

# Comanda pentru a rula serviciul IDM
CMD ["python", "AuthService.py"]
