package lesson1;

public class NewThread {
    public static void main(String[] args) {
        Thread t=new Thread(){
            @Override
            public void run() {  //线程要执行的任务
                System.out.println("ok");
            }
        };
    }
}
