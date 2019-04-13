
//The global class of Gomoku
class Gobang {
    constructor(options = {}) {
        this.options = options;
        this.init();
    }

    init() {
        const { options } = this;
        console.log(options);
    }
}

let gobang = new Gobang