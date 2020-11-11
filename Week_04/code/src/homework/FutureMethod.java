package homework;

import java.util.concurrent.*;

public class FutureMethod implements Callable<Long> {

    private long sum(int num) {
        return fibo(num);
    }

    private long fibo(int a) {
        if ( a < 2) {
            return 1;
        }
        return fibo(a-1) + fibo(a-2);
    }

    @Override
    public Long call() throws Exception {
        return sum(45);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start=System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Long> future = executor.submit(new FutureMethod());

        long result = future.get(); 

        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        executor.shutdown();
    }


}
