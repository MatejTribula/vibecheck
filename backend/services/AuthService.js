const { randomBytes, scrypt, timingSafeEqual } = require('node:crypto');
const { promisify } = require('node:util');
const UserRepository = require('../repositories/UserRepository');
const SessionService = require('./SessionService');
const Result = require('../common/Result');
const ErrorCode = require('../common/ErrorCode');

const deriveKey = promisify(scrypt);
const scryptOptions = { N: 131072, r: 8, p: 1, maxmem: 256 * 1024 * 1024 };

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

    const salt = randomBytes(16).toString('hex');
    const key = await deriveKey(password, salt, 64, scryptOptions);
    const passwordHash = `scrypt:${salt}:${key.toString('hex')}`;

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
    if (!validHash || !timingSafeEqual(key, Buffer.from(storedKey, 'hex'))) {
      return Result.failure(ErrorCode.Unauthorized, 'Invalid email or password.');
    }

    const session = this.sessions.create(user.id, firebaseRegistrationId);
    if (session.hasError) {
      return session;
    }

    return Result.success({ user: this.toPublicUser(user), ...session.value });
  }

  validateCredentials(email, password) {
    if (typeof email !== 'string' || email.trim().length > 254 || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim())) {
      return Result.failure(ErrorCode.InvalidRequest, 'Email is invalid.');
    }

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
