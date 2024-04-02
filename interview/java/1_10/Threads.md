# 쓰레드 여러 개의 순서를 보장하는 코드를 작성해 주세요.

```java
class WorkerThread implements Runnable {
    private int num;

    public WorkerThread(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        System.out.println("Thread " + num + " is running");
    }
}


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[11];

        for (int i = 1; i <= 10; i++) {
            threads[i] = new Thread(new WorkerThread(i));

            threads[i].start();
            threads[i].join();
        }
    }
}
```

- 각 쓰레드가 `start()`를 통해 작업을 시작하고, `join()`을 통해 각 쓰레드가 끝날 때까지 기다린다.