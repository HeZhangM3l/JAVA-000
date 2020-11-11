package homework;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierMethod {

    private volatile Integer value = null;
    CyclicBarrier barrier;

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public void sum(int num) throws BrokenBarrierException, InterruptedException {
        value = fibo(num);
        barrier.await();
    }

    private int fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    public int getValue() throws InterruptedException {
        return value;
    }

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();


        final CyclicBarrierMethod method = new CyclicBarrierMethod();
        CyclicBarrier barrier = new CyclicBarrier(1, ()-> {
            int result = 0;
            try {
                result = method.getValue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("异步计算结果为："+result);

            System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        });
        method.setBarrier(barrier);

        Thread thread = new Thread(() -> {
            try {
                method.sum(45);
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

}
