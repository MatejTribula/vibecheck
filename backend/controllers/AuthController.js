const AuthService = require('../services/AuthService');
const sendResult = require('../common/sendResult');

class AuthController {
  constructor(service = new AuthService()) {
    this.service = service;
  }

  async register(req, res) {
    const { displayName, email, password } = req.body || {};
    const result = await this.service.register(displayName, email, password);

    return sendResult(res, result, 201);
  }

  async login(req, res) {
    const { email, password, firebaseRegistrationId } = req.body || {};
    const result = await this.service.login(email, password, firebaseRegistrationId);

    return sendResult(res, result);
  }
}

module.exports = AuthController;
