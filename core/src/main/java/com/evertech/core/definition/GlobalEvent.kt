package com.evertech.core.definition
/**
 *    author : Ethan
 *    e-mail : Ethan@fedup.cn
 *    date   : 7/2/2020 3:58 PM
 *    desc   :
 */
class GlobalEvent {

    var type: Int = 0

    var tag: Any = ""
    var ticket: Boolean = false

    constructor() {}

    constructor(type: Int) {
        this.type = type
    }

    constructor(type: Int, tag: Any) {
        this.type = type
        this.tag = tag
    }

    override fun toString(): String {
        return "GlobalEvent{" +
                "type=" + type +
                ", tag=" + tag +
                '}'.toString()
    }

    companion object {
        const val TYPE_REVERSE_LAYOUT = 0x1001

        const val TYPE_TOKEN_CHANGE = 0x1002

        const val TYPE_BOTTOM_VIEW = 0x1003

        const val TYPE_COUPON_VIEW = 0x1004
    }

}
