const { randomBytes, createHash } = require('node:crypto');
const UserRepository = require('../repositories/UserRepository');
const UserSessionRepository = require('../repositories/UserSessionRepository');
const Result = require('../common/Result');
const ErrorCode = require('../common/ErrorCode');

class SessionService {
  constructor(users = new UserRepository(), sessions = new UserSessionRepository()) {
    this.users = users;
    this.sessions = sessions;
  }

  create(userId, firebaseRegistrationId = null) {
    if (firebaseRegistrationId !== null && (typeof firebaseRegistrationId !== 'string' || !firebaseRegistrationId.trim() || firebaseRegistrationId.length > 4096)) {
      return Result.failure(ErrorCode.InvalidRequest, 'Firebase registration is invalid.');
    }

    if (!this.users.findById(userId)) {
      return Result.failure(ErrorCode.NotFound, 'User not found.');
    }

    const token = randomBytes(32).toString('hex');
    const tokenHash = this.hashToken(token);
    const expiresAt = new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString();
    this.sessions.create(userId, tokenHash, expiresAt, firebaseRegistrationId?.trim() || null);

    return Result.success({ token, expiresAt });
  }

  validateToken(token) {
    if (typeof token !== 'string' || !/^[a-f0-9]{64}$/.test(token)) {
      return Result.failure(ErrorCode.Unauthorized, 'Session is invalid or expired.');
    }

    const session = this.sessions.findByTokenHash(this.hashToken(token));
    if (!session) {
      return Result.failure(ErrorCode.Unauthorized, 'Session is invalid or expired.');
    }

    return Result.success({ id: session.id, userId: session.userId, expiresAt: session.expiresAt });
  }

  logout(token) {
    const validation = this.validateToken(token);
    if (validation.hasError) {
      return validation;
    }

    this.sessions.deleteByTokenHash(this.hashToken(token));

    return Result.success();
  }

  hashToken(token) {
    return createHash('sha256').update(token).digest('hex');
  }
}

module.exports = SessionService;
