const {Router} = require('express');

const router = new Router();
router.get('/', (req, res) => {
    res.status(200).json('Nothing implemented for user yet.')
})
module.exports = router;