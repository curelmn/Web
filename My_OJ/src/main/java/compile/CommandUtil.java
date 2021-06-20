package compile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

// 通过这个类来创建进行, 并执行 javac, java 等命令
public class CommandUtil {
    // run 方法就用于进行创建进程并执行命令
    // cmd 表示要执行的命令, 比如 javac
    // stdoutFile 指定标准输出写到哪个文件中
    // stderrFile 执行标准错误写到哪个文件中
    public static int run(String cmd, String stdoutFile,
                          String stderrFile) throws IOException, InterruptedException {
        // Java 中对于多进程的操作其实是比较粗糙的, 不方便进行精细化的控制
        // 但是当前咱们也够用了~~
        // 可以使用 Runtime 这样的类完成进程的创建
        // 注意, Runtime 类比较特殊, 咱们在使用的时候, 并不需要
        // 手动创建新的实例, 而是使用现成有的实例就可以了
        // 一个 Java 程序里, Runtime 的实例只有唯一的一个, 不应该有多个. 称为 "单例模式"
        // 这个 Process 对象其实就是用来表示你新创建出来的这个进程的~~
        Process process = Runtime.getRuntime().exec(cmd);
        // 当新的进程跑起来之后, 就需要获取到新进程的输出结果
        if (stdoutFile != null) {
            // getInputStream 得到的是标准输出~
            InputStream stdoutFrom = process.getInputStream();
            // 通过这个对象就可以去读取到当前新进程的标准输出的内容
            FileOutputStream stdoutTo = new FileOutputStream(stdoutFile);
            // 接下来就从新进程这边依次读取每个字节, 写入到 stdoutTo 这个文件里
            while (true) {
                int ch = stdoutFrom.read();
                if (ch == -1) {
                    break;
                }
                stdoutTo.write(ch);
            }
            // 文件读写完毕, 关闭文件了
            stdoutFrom.close();
            stdoutTo.close();
        }

        // 再针对标准错误进行重定向
        if (stderrFile != null) {
            // getErrorStream 得到的是标准错误
            InputStream stderrFrom = process.getErrorStream();
            FileOutputStream stderrTo = new FileOutputStream(stderrFile);
            while (true) {
                int ch = stderrFrom.read();
                if (ch == -1) {
                    break;
                }
                stderrTo.write(ch);
            }
            stderrFrom.close();
            stderrTo.close();
        }
        // 等待新进程结束, 并获取到退出码
        int exitCode = process.waitFor();
        return exitCode;
    }

    public static void main(String[] args) {
        try {
            int ret = CommandUtil.run("javac", "./stdout.txt", "./stderr.txt");
            System.out.println(ret);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
