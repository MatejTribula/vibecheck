const express = require('express');
const swaggerUi = require('swagger-ui-express');
const openapi = require('./openapi.json');
const authRoutes = require('./routes/authRoutes');
const sessionRoutes = require('./routes/sessionRoutes');
const errorHandler = require('./middleware/errorHandler');

function createApp(authService, sessionService) {
  const app = express();

  app.use(express.json());
  app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(openapi));
  app.get('/openapi.json', (req, res) => res.json(openapi));
  app.use('/api/auth', authRoutes(authService));
  app.use('/api/sessions', sessionRoutes(sessionService));

  app.get('/', (req, res) => {
    res.send('Backend is running');
  });

  app.use(errorHandler);

  return app;
}

module.exports = createApp;
