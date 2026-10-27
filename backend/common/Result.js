const ErrorCode = require('./ErrorCode');

class Result {
  constructor(value, errorCode, message) {
    this.value = value;
    this.errorCode = errorCode;
    this.message = message;
    this.hasError = errorCode !== ErrorCode.None;
  }

  static success(value = null) {
    return new Result(value, ErrorCode.None, null);
  }

  static failure(errorCode, message) {
    if (errorCode === ErrorCode.None || !Object.values(ErrorCode).includes(errorCode)) {
      throw new Error('A valid error code is required.');
    }

    return new Result(null, errorCode, message);
  }
}

module.exports = Result;
