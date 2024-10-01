const express = require('express');
const path = require('path');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

// Serving static files from the React app's build directory
app.use(express.static(path.join(__dirname, 'build')));

// Proxy API requests to the Java backend
app.use(
  '/api',
  createProxyMiddleware({
    target: 'http://localhost:8080', 
    changeOrigin: true,
  })
);

// Catch-all handler: For any request that doesn't match the above, send back React's index.html file.
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});

// Starting the server on the specified port
const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Express server running on port ${PORT}`);
});
