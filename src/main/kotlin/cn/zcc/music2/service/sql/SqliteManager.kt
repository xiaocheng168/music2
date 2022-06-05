package cn.zcc.music2.service.sql

import cn.zcc.music2.tools.CodeTools
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.logging.Logger
import kotlin.system.exitProcess

//数据库核心服务
abstract class SqliteManager {

    companion object {
        //日志系统
        val logger: Logger = Logger.getLogger("qwq")

        //连接类
        private val connection: Connection

        init {
            try {
                Class.forName("org.sqlite.JDBC")
                connection = DriverManager.getConnection("jdbc:sqlite:database.db")
            } catch (e: Exception) {
                logger.info(e.message + "数据库驱动异常!!!服务器关闭!")
                exitProcess(0)
            }
        }

        fun installTable() {
            val user = "user"
            val tempToken = "temp_token"
            val playList = "play_list"
            try {
                (query("select * from user"))
            } catch (e: SQLException) {
                this.execute(
                    "create table '$user'\n" +
                            "(\n" +
                            "    uid           id integer primary key,\n" +
                            "    name          varchar(32),\n" +
                            "    password      varchar(64),\n" +
                            "    card_info     varchar(256),\n" +
                            "    cre_date      date,\n" +
                            "    cre_date_long integer,\n" +
                            "    exp           int,\n" +
                            "    ban           int\n" +
                            ");"
                )
                logger.info("$user 已新建成功")
            }
            try {
                (query("select * from $tempToken"))
            } catch (e: SQLException) {
                this.execute(
                    "create table '${tempToken}'(\n" +
                            "    token primary key,\n" +
                            "    data varchar(256)\n" +
                            ")"
                )
                logger.info("$tempToken 已新建成功")
            }
            try {
                (query("select * from $playList"))
            } catch (e: SQLException) {
                this.execute(
                    "create table ${playList}(\n" +
                            "    type int,\n" +
                            "    id integer,\n" +
                            "    play_time_long integer,\n" +
                            "    play_user_id integer\n" +
                            ")"
                )
                logger.info("$playList 已新建成功")
            }


        }

        //注册 >  0注册成功 1用户存在 -1数据库错误
        fun register(name: String, password: String): Int {
            try {
                //用户是否存在 使用用户名去查询
                if (nameToUid(name) != -1) {
                    return 1
                }

                //如果没重复，返回false!
                val b = execute(
                    "insert into user values ((select count(*) from user),'$name','${CodeTools.stringToSha256(password)}','',${
                        SimpleDateFormat("yyyyMMddHHmmss").format(
                            System.currentTimeMillis()
                        )
                    },${System.currentTimeMillis()},0,0)"
                )
                return if (!b) {
                    0
                } else {
                    1
                }
            } catch (e: SQLException) {
                return -1
            }
        }

        @JvmStatic
        //-1表示不存在
        fun nameToUid(name: String): Int {
            val resultSet = query("select uid from user where name = '$name'")
            while (resultSet.next()) {
                return resultSet.getInt("uid")
            }
            resultSet.close()
            return -1
        }

        @JvmStatic
        fun login(username: String, password: String): Boolean {
            return loginNotSha256(username, CodeTools.stringToSha256(password))
        }

        @JvmStatic
        fun loginNotSha256(username: String, password: String): Boolean {
            //判断是否能查询到相对的数据行
            val query = query("select * from user where name='$username' and password = '${password}'")
            while (query.next()) {
                (query.getString("name") == username && query.getString("password") == password)
                return true
            }
            query.close()
            return false
        }

        //一定要注意请求完要关闭IO流
        @JvmStatic
        fun query(sql: String): ResultSet {
            return connection.createStatement().executeQuery(sql)
        }

        //一定要注意请求完要关闭IO流
        @JvmStatic
        fun execute(sql: String): Boolean {
            return try {
                connection.createStatement().execute(sql)
            } catch (e: Exception) {
                true
            }
        }
    }
}

fun main() {
    SqliteManager.installTable()
    println(SqliteManager.login("qwq", "qwq1"))
}