package lesson1;

public class FlagStopThread {
    //使用标志位标识是否继续执行线程中的任务，true中断，false继续执行
    private static volatile boolean isStop = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始转账");
                try {
                    while (!isStop){
                        System.out.println("转账ing...");
                        Thread.sleep(10000);
                    }
                    System.out.println("停止转账");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(3*1000);
        //发现是诈骗犯，需要停止转账
        isStop = true;
    }
}
