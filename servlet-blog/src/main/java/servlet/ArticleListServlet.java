package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ArticleDAO;
import example.Article;
import example.JSONResponse;
import example.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/articleList")
public class ArticleListServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");


        JSONResponse json=new JSONResponse();

       try{

           HttpSession session=req.getSession(false);
           User user=(User) session.getAttribute("user");
           //业务 ：查询文章列表（查所有文章）
           //数据库查询所有文章数据，返回
           //TODO  需要只查询当前用户的文章
           //TODO 代码复用：
           List<Article> query=ArticleDAO.query(user.getId());
           //业务处理成功：返回 success true和业务数据
           json.setSuccess(true);
           json.setData(query);
       }catch (Exception e){
           e.printStackTrace();
           //业务处理失败；返回false+错误码+错误信息
           //TODO 简单返回错误
           json.setCode("ERROR");
           json.setMessage("系统出错，请联系系统管理员");
       }
        ObjectMapper m=new ObjectMapper();
       String s=m.writeValueAsString(json);
       resp.getWriter().println(s);
    }
}
