const admin = require('firebase-admin');
const path = require('path');
const createApp = require('./app');

const serviceAccountPath = process.env.FIREBASE_SERVICE_ACCOUNT_PATH;
const credential = serviceAccountPath
  ? admin.cert(require(path.resolve(serviceAccountPath)))
  : admin.applicationDefault();

admin.initializeApp({ credential });

const app = createApp();

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running at http://localhost:${PORT}`));
