package util;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//方便其他的代码随时能够和数据库建立连接
public class DBUtil {
    //DataSource 一个程序里面有一个就够了
    //即就是 单例模式
    private static final String URL = "\"jdbc:mysql://127.0.0.1:3306/my_oj?characterEncoding=utf8&useSSL=true\"";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2222";
    private static DataSource dataSource = null;

    //目的是只创建出一个DataSource实例
    public static DataSource getDataSource() {
        if (dataSource == null) {
            //没有被实例化过，就创建一个
            dataSource = new MysqlDataSource();
            ((MysqlDataSource) dataSource).setUrl(URL);
            ((MysqlDataSource) dataSource).setUser(USERNAME);
            ((MysqlDataSource) dataSource).setPassword(PASSWORD);
        }
        //如果已经被实例化过了，就直接返回现有的实例
        return dataSource;
    }

    public static Connection getConnection() {
        try {
            return (Connection) getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    //回收资源
    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}

