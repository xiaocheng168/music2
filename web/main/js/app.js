const app = new Vue({
    el: "#main-catplay-app",
    data: {
        version: "b1.0",
        user: {
            name: "Zcc"
        },
        main: {
            soContent: {
                soServer: "1",
                text: "",
                soBackData: []
                    /**
                     * 
                     * {
                        name: "吹梦到西洲",
                        authors: "xxxx",
                        playTime: " 35124",
                        url: "",
                        lyricsUrl: "",
                        id: 0,
                        type: 1
                    }
                     */
            },
            loading: false,
            pane: "player"
        },
        player: {
            url: "",
            play: false,
            duration: 0,
            curr: 0,
            list: [],
            lyrics: "",
            the_lyrics: "ovo~",
            playData: { name: "空空如也诶~" },
            volume: 0
        }
    },
    methods: {
        soMusicBtn: function() {
            this.main.loading = true
            if (this.main.soContent.text == "") {
                message("搜索的内容为空诶!", "info", 1000)
                return
            }
            //Loading
            const data = {
                so: this.main.soContent.text,
                type: this.main.soContent.soServer
            }
            this.main.soContent.soBackData = [];
            //搜索音乐
            $.post(api.soMusic, data, (d) => {

                if (d.code == 0) {
                    d.data.forEach(soMusic => {
                        var authors = ""
                        for (const i of soMusic.author) {
                            authors += i.name + " "
                        }
                        const playTime = soMusic.playTime / 1000
                        const data = {
                            name: soMusic.name,
                            authors: authors,
                            playTime: parseInt((playTime / 60)) + ":" + parseInt(playTime % 60),
                            url: soMusic.url,
                            lyrics: soMusic.lyrics,
                            id: soMusic.id,
                            type: soMusic.type
                        }
                        this.main.soContent.soBackData.push(data)
                        this.main.loading = false
                    });
                } else {
                    autoMessage(d)
                }

            }).error((e) => {
                errorMessage(e)
                this.main.loading = false
            })
        },
        //快捷搜索提示
        soMusic: function(q, cb) {
            //快捷搜索
            cb([])
        },
        //切换音乐按钮被点下
        clickPlayMusic: function(row, column, cell, event) {
            //播放音乐
            app.player.id = row.id;
            changeMusicData(row);
            //列表排序
            for (let index = 0; index < this.player.list.length; index++) {
                const element = this.player.list[index];
                //过滤旧列表内容
                if (element == row) {
                    this.player.list.splice(index, 1)
                }
            }
            this.player.list.unshift(row)
        },
        //播放器事件
        playing: function(e) {
            const tge = e.target
            this.player.curr = parseInt(tge.currentTime);
            this.player.duration = parseInt(tge.duration);
            //歌词处理
            var s = "[0" + parseInt((this.player.curr / 60) < 10 ? ("0" + (this.player.curr / 60)) : this.player.curr / 60) + ":" + (tge.currentTime.toFixed(0) % 60 < 10 ? "0" + tge.currentTime.toFixed(0) % 60 : tge.currentTime.toFixed(0) % 60) + "."
            if (this.player.lyrics == undefined) {
                this.player.the_lyrics = "无歌词"
                return
            }
            if (this.player.lyrics.indexOf(s) < 0) {
                return
            }
            var start = this.player.lyrics.indexOf(s) + s.length
            for (let index = 0; index < 5; index++) {
                if (this.player.lyrics.substring(start, start + index).indexOf("]") != -1) {
                    start += index;
                    break
                }
            }
            var end = start
            for (let index = 0; index < 128; index++) {
                if (this.player.lyrics.substring(start, start + index).indexOf("[") != -1) {
                    end += index
                    end -= 1
                    break
                }
            }
            //设置歌词
            changePlayLyric(this.player.lyrics.substring(start, end));

            /*
                设置播放状态
                设置播放顺序，随机、单首循环、顺序循环、播放一次列表
                歌词修补
                2022年5月31日14:19:14
            */

            /*
            //歌词算法有部分问题，后期修复
            //算法为 寻找当前时间段的文本，从那一刻开始进行剪切，直到遇到\n结束
            //期间可能会有一些较多的歌词遇到非\n结束,属于多行,注意！
            //2022年5月31日03:03:52
            console.log(s);
            if (this.player.lyrics.indexOf(s) != -1) {
                for (let index = 0; index < 128; index++) {
                    this.player.the_lyrics = this.player.lyrics.substring(this.player.lyrics.indexOf(s) + s.length + 4, this.player.lyrics.indexOf(s) + index).replace("]", "").replace("[", "");
                    if (this.player.the_lyrics.indexOf("\n") > 1) {
                        console.log(this.player.the_lyrics);
                        return
                    }
                }
            }
            */
        },
        //播放开始
        playMusic: function() {
            this.player.play = true;
            updatePlayList()
        },
        //停止播放
        pauseMusic: function() {
            this.player.play = false;
        },
        //播放结束
        playEnded: function() {
            this.player.play = false;
            this.onDown()
        },
        //修改音量
        changeVolume: function(e) {
            this.player.volume = e
            $("#main-player")[0].volume = this.player.volume / 100
            setCookie("volume", e, "720")
        },
        //手动操作播放器
        //播放音乐 0 = 播放 1=暂停
        onPlay: function(t) {
            if (t == 0) {
                //如果默认音乐数据里没有音乐，就从列表中抽取第一个进行播放
                if (this.player.playData.name == "空空如也诶~") {
                    if (this.player.list.length > 0) {
                        changeMusicData(this.player.list[0])
                        return;
                    } else {
                        message("没有音乐诶..快去首页找找吧", "info", 1520)
                    }
                }
                $("#main-player")[0].play();
            } else {
                $("#main-player")[0].pause()
            }
        },
        //上一首
        //手动操作播放器
        onUp: function() {
            if (this.player.list.length < 2) {
                message("播放列表只有一首也诶..没法切换下一首", "info", 1000)
                return
            }
            for (let index = 0; index < this.player.list.length; index++) {
                const element = this.player.list[index];
                //播放上一个音乐
                if (element == this.player.playData) {
                    //如果是在播放列表第一个，直接循环播放列表回到最后一个
                    if (index == 0) {
                        changeMusicData(this.player.list[this.player.list.length - 1])
                        break
                    }
                    changeMusicData(this.player.list[index - 1])
                    break
                }
            }
        },
        //下一首
        //手动操作播放器
        onDown: function() {
            if (this.player.list.length < 2) {
                message("播放列表只有一首也诶..没法切换下一首", "info", 1000)
                return
            }
            for (let index = 0; index < this.player.list.length; index++) {
                const element = this.player.list[index];
                //如果是在播放列表最后一个，直接循环播放列表回到第一个
                if (index == this.player.list.length - 1) {
                    changeMusicData(this.player.list[0])
                    break
                }
                //播放下一个音乐
                if (element == this.player.playData) {
                    changeMusicData(this.player.list[index + 1])
                    break
                }
            }
        },
        //修改播放进度
        changeTime: function(e) {
            var player = $("#main-player")[0]
            if (player.currentTime != undefined && this.player.duration != 0) {
                player.currentTime = e
            }
        }
    }
})

//修改音乐播放数据
function changeMusicData(row) {
    app.player.playData = row;
    app.player.lyrics = JSON.parse(row.lyrics.lyrics).lyric;
    app.player.the_lyrics = "ovo~";
    //最后设置URL 
    app.player.url = row.url;
}
//歌词事件
//当即将出现下一句歌词时，会触发这个事件
var tempLyrics = ""

function changePlayLyric(lyric) {
    if (tempLyrics == lyric || lyric == "") {
        return
    }
    tempLyrics = lyric
    app.player.the_lyrics = (lyric);
}
//重新播放
function rePlay() {
    const player = $("#main-player")[0];
    player.currentTime = 0;
    player.play()
}

/*
    同步历史播放记录
    2022年6月1日00:29:34
*/

console.log(getCookie("volume"));
//同步播放记录
function updatePlayList() {
    //id与type不为空处理
    if (app.player.playData.id != undefined) {
        let data = {
            "token": token,
            "type": app.player.playData.type,
            "id": app.player.playData.id
        }
        $.post(api.updatePlayList, data, (e) => {
            console.log(e);
        }).error((e) => {
            errorMessage(e)
        })
    }

}

if (getCookie("volume") == "") {
    app.player.volume = 50
} else {
    app.player.volume = parseInt(getCookie("volume"))
}


var token = "";
//token验效
$(document).ready(function() {
    token = getCookie("token");
    if (token == "") {
        window.location.href = "/login"
    }
    $.post(api.auto, {
        token: token
    }, function(e) {
        if (e.code === 0) {
            app.user.name = e.data
            message("欢迎 " + app.user.name + " 回来~ 好耶! ✨", "success", 1500);
            //同步播放列表
            $.get(api.getPlayList, { "token": token }, (e) => {
                var data = e.data
                data.forEach(music => {
                    //处理作者
                    var authors = ""
                    for (const i of music.author) {
                        authors += i.name + " "
                    }
                    //打包信息
                    let musicInfo = {
                        id: music.id,
                        name: music.name,
                        authors: authors,
                        lyrics: music.lyrics,
                        url: music.url,
                        type: music.type
                    };
                    //加入列表
                    app.player.list.unshift(musicInfo);
                });

            }).error((e) => {
                errorMessage(e)
            })
        } else if (e.code === -403) {
            setCookie("token", "")
            window.location.href = "/login"
        }
        /*
        2022年6月1日19:38:47
        可测试

        */

    }).error((e) => {
        errorMessage(e)
    });
})