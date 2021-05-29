package it.unibo.sonarguigpring

class ResourceRep {
    var content: String? = null
        private set

    constructor() {}
    constructor(content: String?) {
        this.content = content
    }
}