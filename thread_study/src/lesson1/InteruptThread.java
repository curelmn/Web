package lesson1;

public class InteruptThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
//                System.out.println("开始转账");
//                while (!Thread.currentThread().isInterrupted()){
//                    System.out.println("转账ing...");
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }

                try {
                    System.out.println("开始转账");
                    while (!Thread.currentThread().isInterrupted()){
                        System.out.println("转账ing...");
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        Thread.sleep(3*1000);
        t.interrupt();
    }
}
