const {Router} = require('express');

const router = new Router();
router.get('/', (req, res) => {
    res.status(200).json('Nothing implemented for sessions yet.')
})
module.exports = router;