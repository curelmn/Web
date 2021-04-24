package dao;

import example.Article;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {
    public static List<Article> query(int userId) throws SQLException {
        //jdbc查询用户关联的文章列表
        //1.创建连接 Connection对象

        Connection c =DBUtil.getConnection();
        System.out.println(c);
        //2.创建操作命令对象Statement
         String sql="select id,title from article where user_id=?";
        PreparedStatement ps=c.prepareStatement(sql);

        //替换占位符的值  第一个参数表示占位符的索引从1 开始，第二个参数表示要替换的值
        ps.setInt(1,userId);
        //3.执行sql
        ResultSet rs=ps.executeQuery();
        List<Article> query=new ArrayList<>();
        //4.处理结果集
        while (rs.next()){   //移动到下一行  返回true则有数据
        int id = rs.getInt("id");
        String title=rs.getString("title");
        //每一行数据 对应一个对象
        Article a=new Article();
        a.setId(id);
        a.setTitle(title);
        query.add(a);
        }
        //5.释放资源   //TODO 之前的代码会出异常，就没有机会释放资源
        DBUtil.close(c, ps, rs);

        return query;

    }

    public static void main(String[] args) throws SQLException {
        query(1);
    }
}
