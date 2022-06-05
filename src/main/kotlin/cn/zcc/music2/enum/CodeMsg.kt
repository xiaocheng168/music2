package cn.zcc.music2.enum

enum class CodeMsg(msg: String, code: Int) {
    C__0("数据提交不完整", -1),
    C_0("success", 0),
    C_403("验效失败", 403),
    C_404("404了诶!", 404),
    C_40001("系统错误!", 40001)
    ;

    val msg: String
    val code: Int

    init {
        this.msg = msg
        this.code = code
    }
}