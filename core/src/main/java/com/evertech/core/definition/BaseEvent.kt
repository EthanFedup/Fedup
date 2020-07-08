package com.evertech.core.definition
/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 1:52 PM
 *    desc   :
 */
class BaseEvent {
    var success: Boolean = false

    var type: Int = 0

     var symbol: String=""

    constructor() {}

    constructor(success: Boolean) {
        this.success = success
    }

    constructor(type: Int) {
        this.type = type
    }

    constructor(symbol: String) {
        this.symbol = symbol
    }

    override fun toString(): String {
        return "BaseEvent{" +
                "success=" + success +
                ", type=" + type +
                ", tag='" + symbol + '\''.toString() +
                '}'.toString()
    }

}
