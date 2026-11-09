const { applicationDefault, getApps, initializeApp } = require('firebase-admin/app');
const { getMessaging } = require('firebase-admin/messaging');
const Result = require('../common/Result');
const ErrorCode = require('../common/ErrorCode');

class NotificationService {
  constructor(messaging = null) {
    this.messaging = messaging;
  }

  async send(tokens, title, body) {
    if (!Array.isArray(tokens) || tokens.length === 0 || tokens.some(token => typeof token !== 'string' || !token.trim() || token.trim().length > 4096)) {
      return Result.failure(ErrorCode.InvalidRequest, 'Provide an array of valid Firebase tokens.');
    }

    if (typeof title !== 'string' || !title.trim() || typeof body !== 'string' || !body.trim()) {
      return Result.failure(ErrorCode.InvalidRequest, 'Notification title and body are required.');
    }

    const uniqueTokens = [...new Set(tokens.map(token => token.trim()))];
    const android = {
      priority: 'high',
      notification: { title: title.trim(), body: body.trim(), sound: 'default' }
    };
    const messaging = this.getMessaging();
    const result = { successCount: 0, failureCount: 0, failedDevices: [] };

    for (let index = 0; index < uniqueTokens.length; index += 500) {
      const batch = uniqueTokens.slice(index, index + 500);
      const response = await messaging.sendEachForMulticast({ tokens: batch, android });
      result.successCount += response.successCount;
      result.failureCount += response.failureCount;

      for (const [deviceIndex, delivery] of response.responses.entries()) {
        if (!delivery.success) {
          result.failedDevices.push({ token: batch[deviceIndex], error: delivery.error?.code || 'messaging/unknown-error' });
        }
      }
    }

    return Result.success(result);
  }

  getMessaging() {
    if (this.messaging) {
      return this.messaging;
    }

    const app = getApps().find(app => app.name === '[DEFAULT]') || initializeApp({ credential: applicationDefault() });
    this.messaging = getMessaging(app);

    return this.messaging;
  }
}

module.exports = NotificationService;
