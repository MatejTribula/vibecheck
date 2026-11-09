const { Router } = require('express');
const SessionController = require('../controllers/SessionController');

function sessionRoutes(service) {
  const router = Router();
  const controller = new SessionController(service);

  router.get('/current', (req, res) => controller.validate(req, res));
  router.post('/logout', (req, res) => controller.logout(req, res));

  return router;
}

module.exports = sessionRoutes;
