package compile;


//表示编译运行的结果
public class Answer {
    //运行结果是否正确、
    //0 编译运行成功  1编译出错  2 运行出错
    //需要反馈在页面上的信息
    private  int errno;

    //如果出错的原因
    //errno1,reason包含编译错误的信息
    // 2  包含异常调用栈
    private String reason;
    //程序的标准输出
    private  String stdout;
    //程序的标准错误
    private String   stderr;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "errno=" + errno +
                ", reason='" + reason + '\'' +
                ", stdout='" + stdout + '\'' +
                ", stderr='" + stderr + '\'' +
                '}';
    }
}
