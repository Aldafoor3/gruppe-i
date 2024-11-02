# Use the official Node.js image as the base image
FROM node:14 as build

# Set the working directory in the container
WORKDIR /app

# Copy the package.json and package-lock.json files to the container
COPY package*.json ./

# Install the dependencies
RUN npm install

# Copy the rest of the application code to the container
COPY . .

# Build the Angular app for production
RUN npm run build --prod

# Use Nginx as the HTTP server for serving the Angular app
FROM nginx:1.21.0-alpine

# Copy the built Angular app from the previous stage to the container
COPY --from=build /app/dist/sep-so /usr/share/nginx/html

# NGINX Config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose the port on which the Angular app will be served
EXPOSE 4200

# Start Nginx to serve the Angular app
CMD ["nginx", "-g", "daemon off;"]
