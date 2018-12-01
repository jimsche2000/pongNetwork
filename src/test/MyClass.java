package test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyClass{
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public enum Mode{
        INDEFINITE, FIXED_NO_OF_TIMES
    }

    public MyClass(Mode mode){
        if(mode == Mode.INDEFINITE){
            scheduler.scheduleWithFixedDelay(new DoSomethingTask(), 0, 100, TimeUnit.MILLISECONDS);
        }else if(mode == Mode.FIXED_NO_OF_TIMES){
            scheduler.scheduleWithFixedDelay(new DoSomethingNTimesTask(), 0, 100, TimeUnit.MILLISECONDS);
        }
    }

    private class DoSomethingTask implements Runnable{
        @Override
        public void run(){
            doSomething();
        }
    }

    private class DoSomethingNTimesTask implements Runnable{
        private int count = 0;

        @Override
        public void run(){
            doSomething();
            count++;
            if(count > 42){
                // Cancel the scheduling.
                // Can you do this inside the run method, presumably using
                // the Future returned by the schedule method? Is it a good idea?
            }
        }
    }

    private void doSomething(){
        // do something
    }
}