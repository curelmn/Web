package lesson1;

public class JavaProcess {

    public static void main(String[] args) {
        A  a=new A(){
            @Override
            public void pro() {
                System.out.println("pro");
            }
        };
       a.pro();

       B b=new B() {      //匿名内部类：B接口的实现类，不能等同于B
           @Override
           public void pro() {
               System.out.println("pro");
           }
       };
       b.pro();
    }
    static  class A{ //静态内部类
        public void pro()
        {
            System.out.println("a pro");
        };
    }

    //内部接口
    interface B{
        void pro();
    }

}
