class UserSession {
  constructor(id, userId, tokenHash, firebaseRegistrationId, firebaseUpdatedAt, createdAt, expiresAt) {
    this.id = id;
    this.userId = userId;
    this.tokenHash = tokenHash;
    this.firebaseRegistrationId = firebaseRegistrationId;
    this.firebaseUpdatedAt = firebaseUpdatedAt;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
  }
}

module.exports = UserSession;
