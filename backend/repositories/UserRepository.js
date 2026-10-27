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

  toUser(row) {
    return row ? new User(row.id, row.display_name, row.email, row.password_hash, row.created_at) : null;
  }
}

module.exports = UserRepository;
