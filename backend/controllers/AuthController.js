const AuthService = require('../services/AuthService');
const sendResult = require('../common/sendResult');

function resetPage(res, token, message = '', status = 200) {
  res.set({ 'Cache-Control': 'no-store', 'Referrer-Policy': 'no-referrer' });

  return res.status(status).type('html').send(`<!doctype html>
<html lang="en">
<head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><title>Reset password | Vibe Check</title></head>
<body>
  <main>
    <h1>Reset password</h1>
    ${message ? `<p>${message}</p>` : ''}
    ${token ? `<form method="post" action="/api/auth/reset-password">
      <input type="hidden" name="token" value="${token}">
      <label for="password">New password</label>
      <input id="password" name="password" type="password" autocomplete="new-password" minlength="8" maxlength="128" required>
      <button type="submit">Reset password</button>
    </form>` : ''}
  </main>
</body>
</html>`);
}

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

  requestPasswordReset(req, res) {
    return sendResult(res, this.service.requestPasswordReset(req.body?.email), 202);
  }

  showResetPage(req, res) {
    const token = req.query.token;
    if (typeof token !== 'string' || !/^[a-f0-9]{64}$/.test(token)) {
      return resetPage(res, null, 'Reset link is invalid or expired.', 400);
    }

    return resetPage(res, token);
  }

  async resetPassword(req, res) {
    const { token, password } = req.body || {};
    const result = await this.service.resetPassword(token, password);
    if (!req.is('application/x-www-form-urlencoded')) {
      return sendResult(res, result);
    }

    if (result.hasError) {
      return resetPage(res, null, 'Password reset failed. Go back to check the password or request a new reset link.', 400);
    }

    return resetPage(res, null, 'Password updated. You can sign in again in Vibe Check.');
  }
}

module.exports = AuthController;
