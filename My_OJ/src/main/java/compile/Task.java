package compile;

import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

//表示完整的编译运行过程
public class Task {

    //罗列需要的临时文件，用于进程间通信 文件名约定
    private String WORK_DIR;
    //编译所需要的类名
    private String CLASS = "Solution";
    //要编译执行的文件名
    private String CODE;
    //程序标准输出放置的文件
    private String STDOUT;
    //程序标准错误放置的文件
    private String STDERR;
    //程序编译出错的详细信息放置的文件
    private String COMPILE_ERROR;

    public Task() {
        //先生成唯一的id，根据id来拼装出目录名
        WORK_DIR = "./tmp/" + UUID.randomUUID().toString()+"/";
        //然后再生成后续的这些文件名
        CODE = WORK_DIR + CLASS + ".java";
        STDOUT = WORK_DIR + "stdout.txt";
        STDERR = WORK_DIR + "stderr.txt";
        COMPILE_ERROR = WORK_DIR + "compile_error.txt";
    }


    //question表示用户提交的代码
    //Answer表示代码运行的结果
    public   Answer compileAndRun(Question  question) throws IOException, InterruptedException {
        Answer answer=new Answer();



        //0.给临时文件准备好一个目录
        //判断WORK_DIR是否存在，如果存在就跳过，不存在就创建目录
        File file=new File(WORK_DIR);
        if (!file.exists()) {
        //创建对应目录
            file.mkdirs();
        }
        //1.准备好需要用到的临时文件
        //编译源代码的文件
        //编译出错要放的文件
        //最终运行的标准输出标准错误的文件
       FileUtil.writeFile(CODE,question.getCode());

        //2.构造编译指令，并且执行，预期得到的结果
        //   就是一个对应的.class文件 或者编译错误的文件
        //-d 表示生成的class文件生成的文件位置
        //javac -encoding utf-8 ./tmp/Solution.java -d./tmp/

        //String compiledCmd="javac -encoding utf-8"+CODE+" -d"+WORK_DIR;
        String compiledCmd=String.format("javac -encoding utf-8 %s -d %s",
              CODE,WORK_DIR);
        System.out.println("编译命令： "+compiledCmd);
        //创建子进程进行编译,此处不关心javac的标准输出
        //只关心javac的标准错误，标准错误中就已经包含了编译出错的信息
        CommandUtil.run(compiledCmd,null,COMPILE_ERROR);
        //此处 判断编译是否出错，看COMPILE_ERROR这个文件是否为空
        String compileError=FileUtil.readFile(COMPILE_ERROR);
        if (!compileError.equals("")){
            //编译文件不为空 则编译出错
            answer.setErrno(1);
            answer.setReason(compileError);
            return answer;
        }

        //3.构造运行指令，并且进行执行，预期得到的结果就是这个代码的标准输出的文件和标准错误的文件
       //为了让java命令找到.class文件的位置还需要加上一个选项 -classPath
        String runCmd=String.format("java -classpath %s %s",WORK_DIR,CLASS);
        System.out.println("runCmd"+runCmd);
        CommandUtil.run(runCmd,STDOUT,STDERR);
        //尝试读取STDERR这个文件的内容，不为空，就认为是在运行
        //如果程序抛出异常，异常的调用栈信息就是通过stderr来输出的
        String runError=FileUtil.readFile(STDERR);
        if (!runError.equals("")){
            //运行出错
            answer.setErrno(2);
            answer.setReason(runError);
            return answer;
        }
        //4.把最终结果构造成Answer对象，并返回
        answer.setErrno(0);
        String runStdout=FileUtil.readFile(STDOUT);
        answer.setStdout(runStdout);
        return answer;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Task task=new Task();
        Question question=new Question();
        question.setCode("public class Solution {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"std\");\n" +
                "        String s=null;\n" +
                "        System.out.println(s.length());\n" +
                "    }\n" +
                "}");
        Answer answer=task.compileAndRun(question);
        System.out.println(answer);

    }
}
