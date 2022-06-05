const app = new Vue({
    el: "#main-catplay-register-app",
    data: {
        username: "",
        password: "",
        confirm_password: ""
    },
    methods: {
        login: function() {
            const data = {
                    username: this.username,
                    password: this.password,
                    confirm_password: this.confirm_password
                }
                //验效登录请求参数
            if (this.username == "" || this.password == "" || this.confirm_password == "") {
                message("用户或密码不完整", "info", 1000)
            } else if (this.password != this.confirm_password) {
                message("确认密码与密码不一致", "info", 1000)
            } else {
                const logining = this.$loading({
                    lock: true,
                    text: "正在努力处理中...ovo!",
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                $.post(api.registerApi, data, (rd) => {
                    logining.close()
                        //自动处理一些比较常见的提示
                    autoMessage(rd)
                    if (rd.code === 0) {
                        message(rd.msg, "success", 3000)
                        setTimeout(() => {
                            message("即将起航🚀 2's!", "success", 3000)
                            setTimeout(() => {
                                window.location.href = "/login"
                            }, 2000);
                        }, 100);
                    }
                }).error((e) => {
                    message("服务器内部错误 Code: " + e.readyState, "error", 5000)
                    logining.close()
                })
            }
        }
    }
})