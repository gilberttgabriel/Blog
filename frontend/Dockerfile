FROM node:lts-alpine AS builder

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html

# Exponemos el puerto 80
EXPOSE 80

# Comando para encender Nginx en primer plano
CMD ["nginx", "-g", "daemon off;"]