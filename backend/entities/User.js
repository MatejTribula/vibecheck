class User {
  constructor(id, displayName, email, passwordHash, createdAt) {
    this.id = id;
    this.displayName = displayName;
    this.email = email;
    this.passwordHash = passwordHash;
    this.createdAt = createdAt;
  }
}

module.exports = User;
