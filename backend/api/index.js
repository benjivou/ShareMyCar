const {Router} = require('express');
const UserRouter = require('./users');
const SessionRouter = require('./sessions');

const router = new Router();
router.get('/', (req, res) => {
    res.redirect('/api/status');
});
router.get('/status', (req, res) => {
    res.status(200).json('ok')
});
router.use('/users', UserRouter);
router.use('/sessions', SessionRouter);
module.exports = router;