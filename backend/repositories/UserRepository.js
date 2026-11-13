const database = require('../data/database');
const User = require('../entities/User');

class UserRepository {
  constructor(db = database) {
    this.db = db;
  }

  create(displayName, email, passwordHash) {
    const result = this.db.prepare('INSERT INTO users (display_name, email, password_hash) VALUES (?, ?, ?)').run(displayName.trim(), email.trim().toLowerCase(), passwordHash);

    return this.findById(result.lastInsertRowid);
  }

  findById(id) {
    return this.toUser(this.db.prepare('SELECT * FROM users WHERE id = ?').get(id));
  }

  findByEmail(email) {
    return this.toUser(this.db.prepare('SELECT * FROM users WHERE email = ?').get(email.trim().toLowerCase()));
  }

  savePasswordReset(userId, tokenHash, expiresAt, cooldownCutoff) {
    return this.db.prepare(`
      INSERT INTO password_resets (user_id, token_hash, expires_at) VALUES (?, ?, ?)
      ON CONFLICT(user_id) DO UPDATE SET token_hash = excluded.token_hash, expires_at = excluded.expires_at
      WHERE password_resets.expires_at <= ?
    `).run(userId, tokenHash, expiresAt, cooldownCutoff).changes > 0;
  }

  hasPasswordReset(tokenHash, now) {
    return Boolean(this.db.prepare('SELECT 1 FROM password_resets WHERE token_hash = ? AND expires_at > ?').get(tokenHash, now));
  }

  resetPassword(tokenHash, passwordHash, now) {
    this.db.exec('BEGIN IMMEDIATE');

    try {
      const reset = this.db.prepare('DELETE FROM password_resets WHERE token_hash = ? AND expires_at > ? RETURNING user_id').get(tokenHash, now);
      if (reset) {
        this.db.prepare('UPDATE users SET password_hash = ? WHERE id = ?').run(passwordHash, reset.user_id);
        this.db.prepare('DELETE FROM user_sessions WHERE user_id = ?').run(reset.user_id);
      }

      this.db.exec('COMMIT');

      return Boolean(reset);
    } catch (error) {
      this.db.exec('ROLLBACK');

      throw error;
    }
  }

  toUser(row) {
    return row ? new User(row.id, row.display_name, row.email, row.password_hash, row.created_at) : null;
  }
}

module.exports = UserRepository;
