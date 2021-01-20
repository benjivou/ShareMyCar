const {Router} = require('express');
const RestOperator = require('../../utils/rest-operation');
const users = require('./users.json');

const router = new Router();
//Setup the RestOperation module
const rest_operator = new RestOperator('users', 'users/users.json')

//Creation of the routes
router.get('/', (req, res) => {
    res.status(200).json(rest_operator.get());
})
router.get('/token/:username/:pwd', async (req, res) => {
    //res.status(200).json(users);
    try {
        const user = rest_operator.get().find(u => u.username === req.params.username && u.password === req.params.pwd);
        await res.json({token: user.id})
    } catch(err) {
        await res.json({message: "The username or the password is incorrect."})
    }
});
router.post('/new', (req, res) => {
    try {
        const user = rest_operator.post(req.body);
        res.status(201).json(user)
    } catch(err) {
        res.status(500).json(e);
    }
})
router.post('/login', (req, res) => {
    try {
        const data = req.body;
        const user = rest_operator.get()
                        .find(u => u.username === data.username && u.password === data.password);
        if(user) res.status(200).json(user);
        else res.status(404).json({err: "Unknown user"})
    } catch(err) {
        res.status(500).json(e);
    }
})
module.exports = router;