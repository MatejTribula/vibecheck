const ErrorCode = require('./ErrorCode');

const errorStatuses = {
  [ErrorCode.InvalidRequest]: 400,
  [ErrorCode.Unauthorized]: 401,
  [ErrorCode.Forbidden]: 403,
  [ErrorCode.NotFound]: 404,
  [ErrorCode.DuplicateEntity]: 409
};

function sendResult(res, result, successStatus = 200) {
  const status = result.hasError ? errorStatuses[result.errorCode] || 500 : successStatus;

  return res.status(status).json(result);
}

module.exports = sendResult;
