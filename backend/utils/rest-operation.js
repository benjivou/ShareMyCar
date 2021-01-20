const fs = require('fs');

class RestOperator {

    constructor(name, fn) {
        this.name = name
        this.filename = fn
        this.items = []

        this.load()
    }

    get = () => {
        return this.items;
    }

    getById = (id) => {
        const item = this.items.find(i => i.id == id);
        if(!item) throw new ItemNotFoundError(`Cannot get ${this.name} object with id=${id}. This object doesn't exist.`);
        return item;
    }

    post = (boby) => {
        const item = Object.assign({}, boby, {id: Date.now()})
        this.items.push(item);
        save(this.items, `/${this.filename}`);
        return item;
    }

    update = (id, body) => {
        const oldIndex = this.items.findIndex(i => i.id == id);
        if(oldIndex == -1) throw new ItemNotFoundError(`Cannot update ${this.name} object with id=${id}. This object doesn't exist.`);
        const updatedItem = Object.assign({}, this.items[oldIndex], body);
        this.items[oldIndex] = updatedItem;
        save(this.items, `${this.filename}`);
        return updatedItem;
    }

    delete = (id) => {
        const index = this.items.findIndex(i => i.id == id);
        if(index == -1) throw new ItemNotFoundError(`Cannot delete ${this.name} object with id=${id}. This object doesn't exist.`);
        this.items.splice(index, 1);
        save(this.items, `${this.filename}`);
    }

    load = () => {
        this.items = require(`../api/${this.filename}`);
    }
}

function save(list, path) {
    try {
        fs.writeFileSync(`${__dirname}/../api/${path}`, JSON.stringify(list));
    } catch(e) {
        console.error(e);
    }
}

class ItemNotFoundError extends Error {

    /**
     * Constructeur de classe
     * @param {string} mess Message d'erreur qui sera affiché
     */
    constructor(mess) {
        super();
        this.name = "ItemNotFoundError";
        this.message = mess;
    }
}

module.exports = RestOperator;