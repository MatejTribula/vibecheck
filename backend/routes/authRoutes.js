const { Router, urlencoded } = require('express');
const AuthController = require('../controllers/AuthController');
const Result = require('../common/Result');
const ErrorCode = require('../common/ErrorCode');

function resetRateLimit() {
  const requests = new Map();
  let windowEndsAt = 0;

  return (req, res, next) => {
    const now = Date.now();
    if (now >= windowEndsAt) {
      requests.clear();
      windowEndsAt = now + 60_000;
    }

    const count = requests.get(req.ip) || 0;
    if (count >= 10 || (count === 0 && requests.size >= 10_000)) {
      res.set('Retry-After', String(Math.ceil((windowEndsAt - now) / 1000)));

      return res.status(429).json(Result.failure(ErrorCode.InvalidRequest, 'Too many reset requests. Try again shortly.'));
    }

    requests.set(req.ip, count + 1);
    return next();
  };
}

function authRoutes(service) {
  const router = Router();
  const controller = new AuthController(service);
  const limitReset = resetRateLimit();

  router.post('/register', (req, res) => controller.register(req, res));
  router.post('/login', (req, res) => controller.login(req, res));
  router.post('/forgot-password', limitReset, (req, res) => controller.requestPasswordReset(req, res));
  router.post('/reset-password', limitReset, urlencoded({ extended: false }), (req, res) => controller.resetPassword(req, res));
  router.get('/reset-password', (req, res) => controller.showResetPage(req, res));

  return router;
}

module.exports = authRoutes;
