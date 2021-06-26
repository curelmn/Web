package util;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//使用这个类封装Java文件操作。
//让后面的代码更方便的读写整个文件
public class FileUtil {
    //从指定文件中一次把所有内容都读出来
    public static  String readFile(String filePath){
        StringBuilder stringBuilder=new StringBuilder();

        try(FileInputStream fileInputStream=new FileInputStream(filePath)) {
            while (true){
                int ch=fileInputStream.read();
                if (ch==-1){
                    break;
                }
                //每次read方法只读不到一个字节
                //设计为-1只为了多表示一下 -1
                //实际读取的结果插入stringbuilder的时候需要
                stringBuilder.append((char)ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  stringBuilder.toString();
    }
//把content中的内容一次写入到filePath对应的文件中
    public  static  void writeFile (String filePath,String  content){
      try(FileOutputStream fileOutputStream=new FileOutputStream(filePath)){
          fileOutputStream.write(content.getBytes());
      }catch (IOException e){
          e.printStackTrace();
      }
    }
}
