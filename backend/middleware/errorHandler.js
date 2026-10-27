const Result = require('../common/Result');
const ErrorCode = require('../common/ErrorCode');

function errorHandler(error, req, res, next) {
  if (res.headersSent) {
    return next(error);
  }

  if (error.type === 'entity.parse.failed' || error.type === 'entity.too.large') {
    return res.status(error.status).json(Result.failure(ErrorCode.InvalidRequest, 'Request body is invalid.'));
  }

  console.error(error);

  return res.status(500).json(Result.failure(ErrorCode.InternalError, 'An unexpected error occurred.'));
}

module.exports = errorHandler;
