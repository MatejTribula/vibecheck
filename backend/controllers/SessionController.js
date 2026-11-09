const SessionService = require('../services/SessionService');
const sendResult = require('../common/sendResult');

class SessionController {
  constructor(service = new SessionService()) {
    this.service = service;
  }

  validate(req, res) {
    const result = this.service.validateToken(this.getToken(req));

    return sendResult(res, result);
  }

  logout(req, res) {
    const result = this.service.logout(this.getToken(req));

    return sendResult(res, result);
  }

  getToken(req) {
    const authorization = req.get('Authorization') || '';
    const match = /^Bearer ([a-f0-9]{64})$/i.exec(authorization);

    return match ? match[1] : null;
  }
}

module.exports = SessionController;
