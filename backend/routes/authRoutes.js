const { Router } = require('express');
const AuthController = require('../controllers/AuthController');

function authRoutes(service) {
  const router = Router();
  const controller = new AuthController(service);

  router.post('/register', (req, res) => controller.register(req, res));
  router.post('/login', (req, res) => controller.login(req, res));

  return router;
}

module.exports = authRoutes;
