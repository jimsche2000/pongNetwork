package test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

//public void runNTimes(Runnable task, int maxRunCount, long period, TimeUnit unit, ScheduledExecutorService executor) {
//    new FixedExecutionRunnable(task, maxRunCount).runNTimes(executor, period, unit);
//}

class FixedExecutionRunnable implements Runnable {
    private final AtomicInteger runCount = new AtomicInteger();
    private final Runnable delegate;
    private volatile ScheduledFuture<?> self;
    private final int maxRunCount;

    public FixedExecutionRunnable(Runnable delegate, int maxRunCount) {
        this.delegate = delegate;
        this.maxRunCount = maxRunCount;
    }

    @Override
    public void run() {
        delegate.run();
        if(runCount.incrementAndGet() == maxRunCount) {
            boolean interrupted = false;
            try {
                while(self == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                }
                self.cancel(false);
            } finally {
                if(interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void runNTimes(ScheduledExecutorService executor, long period, TimeUnit unit) {
        self = executor.scheduleAtFixedRate(this, 0, period, unit);
    }
    public static void main(String[] args) {
    	Runnable b = new Runnable() {
			
			@Override
			public void run() {
				FixedExecutionRunnable a = new FixedExecutionRunnable(this, 4);
				a.runNTimes(Executors.newSingleThreadScheduledExecutor(), 1000, TimeUnit.MILLISECONDS);
				
			}
		};
		b.run();
		System.out.println("test");
    	
	}
}