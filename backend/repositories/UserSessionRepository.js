const database = require('../data/database');
const UserSession = require('../entities/UserSession');

class UserSessionRepository {
  constructor(db = database) {
    this.db = db;
  }

  create(userId, tokenHash, expiresAt, firebaseRegistrationId = null) {
    const firebaseUpdatedAt = firebaseRegistrationId ? new Date().toISOString() : null;

    const result = this.db.prepare('INSERT INTO user_sessions (user_id, token_hash, expires_at, firebase_registration_id, firebase_updated_at) VALUES (?, ?, ?, ?, ?)').run(userId, tokenHash, expiresAt, firebaseRegistrationId, firebaseUpdatedAt);

    return this.findById(result.lastInsertRowid);
  }

  findById(id) {
    return this.toUserSession(this.db.prepare('SELECT * FROM user_sessions WHERE id = ?').get(id));
  }

  findByTokenHash(tokenHash) {
    return this.toUserSession(this.db.prepare('SELECT * FROM user_sessions WHERE token_hash = ? AND julianday(expires_at) > julianday(\'now\')').get(tokenHash));
  }

  updateFirebaseRegistration(id, firebaseRegistrationId) {
    const firebaseUpdatedAt = firebaseRegistrationId ? new Date().toISOString() : null;

    this.db.prepare('UPDATE user_sessions SET firebase_registration_id = ?, firebase_updated_at = ? WHERE id = ?').run(firebaseRegistrationId, firebaseUpdatedAt, id);

    return this.findById(id);
  }

  deleteByTokenHash(tokenHash) {
    return this.db.prepare('DELETE FROM user_sessions WHERE token_hash = ?').run(tokenHash).changes > 0;
  }

  toUserSession(row) {
    return row ? new UserSession(row.id, row.user_id, row.token_hash, row.firebase_registration_id, row.firebase_updated_at, row.created_at, row.expires_at) : null;
  }
}

module.exports = UserSessionRepository;
