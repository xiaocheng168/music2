const app = new Vue({
    el: "#main-catplay-login-app",
    data: {
        username: "",
        password: ""
    },
    methods: {
        login: function() {
            const data = {
                    username: this.username,
                    password: this.password
                }
                //验效登录请求参数
            if (this.username == "" || this.password == "") {
                message("用户或密码不完整", "info", 700)
            } else {
                const logining = this.$loading({
                    lock: true,
                    text: "Logining",
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                $.post(api.loginApi, data, (rd) => {
                    logining.close()
                    autoMessage(rd)

                    if (rd.code === 0) {
                        //设置Cookie保存Token
                        setCookie("token", rd.data, 24)
                        message(rd.msg, "success", 3000)
                        setTimeout(() => {
                            message("即将起航🚀 2's!", "success", 3000)
                            setTimeout(() => {
                                window.location.href = "/"
                            }, 2000);
                        }, 100);
                    }
                })
            }
        }
    }
})