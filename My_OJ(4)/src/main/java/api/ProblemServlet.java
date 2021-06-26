package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import problem.Problem;
import problem.ProblemDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

@WebServlet("/problem")
public class ProblemServlet extends HttpServlet {
    private Gson gson=new GsonBuilder().create();
    //用来实现读取题目列表和读取题目详情
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        resp.setContentType("application/json;charset=utf-8");

        //从req对象中读取id
        String id=req.getParameter("id");
        if (id==null||id.equals("")){
            //查找题目列表
            selectALL(resp);
        }else {
            //查找指定题目详情
            selectOne(Integer.parseInt(id),resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //进行新增一个题目的数据
        //1.要读取到请求中 的body数据，把读到的数据构造成一个Problem数据
        //进一步插入数据
        String body=readBody(req);
        //2.把读取到的数据造成Problem 对象
        Problem problem=gson.fromJson(body,Problem.class);
        //3.把数据插入数据库
        ProblemDAO problemDAO=new ProblemDAO();
        problemDAO.insert(problem);
        //4.返回一个结果给客户端
        resp.setStatus(200);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write("{\"ok\":1");

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //删除
        //1.从req 请求对象当中读取出要删除题目的id
        resp.setStatus(200);
        resp.setContentType("application/json;charset=utf-8");
        String id=req.getParameter("id");
        if (id==null || id.equals("")){
            resp.getWriter().write("{\"ok\":0,\"reason:\"id 不存在\"}");
        }
        //2.调用数据库执行删除即可
        ProblemDAO problemDAO=new ProblemDAO();
        problemDAO.delete(Integer.parseInt(id));
        resp.getWriter().write("{\"ok\":1}");
        //3.返回一个删除结果
    }

    private String readBody(HttpServletRequest req) throws UnsupportedEncodingException {
        //1.先获取到body的长度，并分配对应的内存空间
        //单位是字节
        int contentLength=req.getContentLength();
        byte[] buf=new byte[contentLength];
        //2.根据req 对象，拿到读取body的Inputstream对象
        try(InputStream inputStream=req.getInputStream()) {
            inputStream.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3.把读到的buf 转换成String 对象
        return new String(buf,"utf-8");
    }

    private void selectOne(int problemId, HttpServletResponse resp) throws IOException {
        //1.创建 ProblemDAO对象
        ProblemDAO problemDAO=new ProblemDAO();
        //2.查找结果
        Problem problem=ProblemDAO.selectOne(problemId);
        //3.把结果给包装成JSON格式
        String respString = gson.toJson(problem);
        //4.把结果协会给前端
        resp.getWriter().write(respString);
    }

    private void selectALL(HttpServletResponse resp) throws IOException {
        //1.创建 ProblemDAO对象
        ProblemDAO problemDAO=new ProblemDAO();
        //2.查找所以结果
        List<Problem> problems=ProblemDAO.selectAll();
        //3.把结果给包装成JSON格式
         String respString = gson.toJson(problems);
         //4.把结果协会给前端
        resp.getWriter().write(respString);
    }
}
