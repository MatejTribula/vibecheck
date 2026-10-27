const { DatabaseSync } = require('node:sqlite');
const path = require('node:path');
const database = new DatabaseSync(path.join(__dirname, 'vibecheck.sqlite'));

database.exec(`
  PRAGMA foreign_keys = ON;
  CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY,
    display_name TEXT NOT NULL CHECK(length(trim(display_name)) > 0),
    email TEXT NOT NULL UNIQUE COLLATE NOCASE CHECK(length(trim(email)) > 0),
    password_hash TEXT NOT NULL CHECK(length(password_hash) > 0),
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
  ) STRICT;
  CREATE TABLE IF NOT EXISTS user_sessions (
    id INTEGER PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash TEXT NOT NULL UNIQUE CHECK(length(token_hash) = 64 AND token_hash NOT GLOB '*[^0-9a-f]*'),
    firebase_registration_id TEXT,
    firebase_updated_at TEXT,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TEXT NOT NULL CHECK(julianday(expires_at) IS NOT NULL AND julianday(expires_at) > julianday(created_at))
  ) STRICT;
  CREATE INDEX IF NOT EXISTS user_sessions_user_id ON user_sessions(user_id);
`);

module.exports = database;
