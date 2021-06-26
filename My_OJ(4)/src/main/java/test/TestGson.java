package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Student{
    private  int id=0;
    private String name="xiaoming";
}
public class TestGson {
    public static void main(String[] args) {
        Student student=new Student();
        //准备好一个Gson实例
        //这种风格代码称为“工程设计模式”
        Gson gson=new GsonBuilder().create();
        //对象转为字符串
        String studentString=gson.toJson(student);
        System.out.println(studentString);
        //字符串转为对象
        Student student1=gson.fromJson(studentString,Student.class);
    }
}
