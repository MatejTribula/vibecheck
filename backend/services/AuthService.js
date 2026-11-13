const { createHash, randomBytes, scrypt, timingSafeEqual } = require('node:crypto');
const { promisify } = require('node:util');
const UserRepository = require('../repositories/UserRepository');
const SessionService = require('./SessionService');
const Result = require('../common/Result');
const ErrorCode = require('../common/ErrorCode');

const deriveKey = promisify(scrypt);
const scryptOptions = { N: 131072, r: 8, p: 1, maxmem: 256 * 1024 * 1024 };
const resetLifetime = 15 * 60 * 1000;
const resetCooldown = 60 * 1000;

async function hashPassword(password) {
  const salt = randomBytes(16).toString('hex');
  const key = await deriveKey(password, salt, 64, scryptOptions);

  return `scrypt:${salt}:${key.toString('hex')}`;
}

function hashResetToken(token) {
  return createHash('sha256').update(token).digest('hex');
}

class AuthService {
  constructor(users = new UserRepository(), sessions = new SessionService(users)) {
    this.users = users;
    this.sessions = sessions;
  }

  async register(displayName, email, password) {
    const validation = this.validateCredentials(email, password);
    if (validation.hasError) {
      return validation;
    }

    if (typeof displayName !== 'string' || !displayName.trim() || displayName.trim().length > 100) {
      return Result.failure(ErrorCode.InvalidRequest, 'Display name must contain 1 to 100 characters.');
    }

    if (this.users.findByEmail(email)) {
      return Result.failure(ErrorCode.DuplicateEntity, 'Email is already registered.');
    }

    const passwordHash = await hashPassword(password);

    let user;
    try {
      user = this.users.create(displayName, email, passwordHash);
    } catch (error) {
      if (error.errcode === 2067) {
        return Result.failure(ErrorCode.DuplicateEntity, 'Email is already registered.');
      }

      throw error;
    }

    return Result.success(this.toPublicUser(user));
  }

  async login(email, password, firebaseRegistrationId = null) {
    const validation = this.validateCredentials(email, password);
    if (validation.hasError) {
      return validation;
    }

    const user = this.users.findByEmail(email);
    const [algorithm, salt, storedKey] = (user?.passwordHash || '').split(':');
    const validHash = algorithm === 'scrypt' && /^[a-f0-9]{32}$/.test(salt) && /^[a-f0-9]{128}$/.test(storedKey);
    const key = await deriveKey(password, validHash ? salt : '0'.repeat(32), 64, scryptOptions);
    if (!validHash || !timingSafeEqual(key, Buffer.from(storedKey, 'hex')) || this.users.findById(user.id)?.passwordHash !== user.passwordHash) {
      return Result.failure(ErrorCode.Unauthorized, 'Invalid email or password.');
    }

    const session = this.sessions.create(user.id, firebaseRegistrationId);
    if (session.hasError) {
      return session;
    }

    return Result.success({ user: this.toPublicUser(user), ...session.value });
  }

  validateCredentials(email, password) {
    const emailValidation = this.validateEmail(email);
    if (emailValidation.hasError) {
      return emailValidation;
    }

    return this.validatePassword(password);
  }

  requestPasswordReset(email) {
    const validation = this.validateEmail(email);
    if (validation.hasError) {
      return validation;
    }

    const user = this.users.findByEmail(email);
    const token = randomBytes(32).toString('hex');
    const tokenHash = hashResetToken(token);
    const now = Date.now();

    if (user) {
      const baseUrl = process.env.PASSWORD_RESET_BASE_URL || `http://localhost:${process.env.PORT || 3000}`;
      const link = new URL('/api/auth/reset-password', baseUrl);
      link.searchParams.set('token', token);

      if (this.users.savePasswordReset(user.id, tokenHash, now + resetLifetime, now + resetLifetime - resetCooldown)) {
        console.log(`Password reset link: ${link.href}`);
      }
    }

    return Result.success({ message: 'If an account exists for that email, a reset link will be generated.' });
  }

  async resetPassword(token, password) {
    const validation = this.validatePassword(password);
    if (validation.hasError) {
      return validation;
    }

    const invalidToken = Result.failure(ErrorCode.InvalidRequest, 'Reset link is invalid or expired.');
    if (typeof token !== 'string' || !/^[a-f0-9]{64}$/.test(token)) {
      return invalidToken;
    }

    const tokenHash = hashResetToken(token);
    if (!this.users.hasPasswordReset(tokenHash, Date.now())) {
      return invalidToken;
    }

    const passwordHash = await hashPassword(password);
    if (!this.users.resetPassword(tokenHash, passwordHash, Date.now())) {
      return invalidToken;
    }

    return Result.success();
  }

  validateEmail(email) {
    if (typeof email !== 'string' || email.trim().length > 254 || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim())) {
      return Result.failure(ErrorCode.InvalidRequest, 'Email is invalid.');
    }

    return Result.success();
  }

  validatePassword(password) {
    if (typeof password !== 'string' || password.length < 8 || password.length > 128) {
      return Result.failure(ErrorCode.InvalidRequest, 'Password must contain 8 to 128 characters.');
    }

    return Result.success();
  }

  toPublicUser(user) {
    return { id: user.id, displayName: user.displayName, email: user.email, createdAt: user.createdAt };
  }
}

module.exports = AuthService;
