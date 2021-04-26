package org.example.util;

import lombok.Getter;
import lombok.Setter;
import org.example.model.JSONResponse;
import org.example.model.User;

public class Main {

    //模拟前端获取响应数据
    public static void main(String[] args) {

//        System.out.println(doGet("abc"));
        System.out.println(doGet("1"));
    }

    //模拟Servlet响应json数据
    public static JSONResponse doGet(String sid){
        JSONResponse r = new JSONResponse();
        //业务执行成功

        try {
            //业务代码
            try {
//                String sid = req.getParameter("id");
                int id = Integer.parseInt(sid);
                User u = query(id);//模拟数据库根据id查询用户
                r.setSuccess(true);//业务操作成功
                r.setData(u);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new MyException("my-err-400", "请求id类型错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof MyException){
                MyException me = (MyException) e;
                r.setCode(me.getCode());
                r.setMessage(me.getMessage());
            }else{
                r.setCode("sys-err");
                r.setMessage("未知的错误");
            }
        }
        return r;

    }

    public static User query(int id){//模拟dao查询数据库数据
        User u = null;//模拟数据库查询的数据
        try {
            u = new User();
            int i = 1/0;
            u.setId(5);
        } catch (Exception e) {
            throw new MyException("my-err-002", "数据库查询用户出错");
        }
        return u;
    }

    @Getter
    @Setter
    private static class MyException extends RuntimeException{
        private String code;

        public MyException(String code, String message) {
            super(message);
            this.code = code;
        }

        public MyException(String code, String message, Throwable cause) {
            super(message, cause);
            this.code = code;
        }
    }
}
