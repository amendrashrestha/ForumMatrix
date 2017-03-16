package com.proj.forummatrix.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractThreadProcessor<T1, T2, T3> {

    protected ArrayList<Thread> _workerThreads = new ArrayList<>();

    protected Object lockObject = new Object();

    private Object waitObject = new Object();

    protected AtomicInteger threadCompleteCount;

    private int currentProcessingIndex;

    protected List<T1> sourceInputs;
    protected T3 table;

    protected BlockingQueue<T2> outputResult;

    protected Thread outputHandlerThread;

    private class WorkerThreadRunnable implements Runnable {

        @Override
        public void run() {
            boolean threadComplete = false;
            while (true) {
                T1 inputData = null;

                synchronized (lockObject) {
                    if (currentProcessingIndex == sourceInputs.size()) {
                        threadComplete = true;
                    } else {
                        inputData = sourceInputs.get(currentProcessingIndex++);
                    }
                }

                if (threadComplete) {
                    threadCompleteCount.incrementAndGet();
                    onWorkerThreadComplete();
                    return;
                }
               
                outputResult.add(processInput(inputData, table));
            }
        }
    }

    public AbstractThreadProcessor(int threadCount) {
        this.outputHandlerThread = new Thread(() -> {
            int workerThreadCount = _workerThreads.size();
            
            while (true) {
                T2 output;
                try {
                    output = outputResult.poll(1000, TimeUnit.MILLISECONDS);
                    
                    // the queue is empty case
                    if (output == null && outputResult.size() == 0) {
                        if (threadCompleteCount.get() == workerThreadCount) {
                            onOutputHandleThreadComplete();
                            synchronized (waitObject) {
                                waitObject.notify();
                            }                            
                            return;
                        }
                    } else {
                        handleOutput(output);
                    }
                } catch (InterruptedException e1) {
                }
            }
        });
        for (int i = 0; i < Math.max(1, threadCount); i++) {
            Thread t = new Thread(new WorkerThreadRunnable());
            t.setName(String.valueOf(i));
            _workerThreads.add(t);
        }
    }

    public void startProcessing(List<T1> inputs, T3 tableName) {
        threadCompleteCount = new AtomicInteger(0);

        currentProcessingIndex = 0;

        outputResult = new ArrayBlockingQueue<>(2000);

        sourceInputs = inputs;
        table = tableName;
        
        //start the output handler thread
        outputHandlerThread.start();

        //start the worker thread
        _workerThreads.stream().forEach((t) -> {
            t.start();
        });

        try {
            synchronized (waitObject) {
                waitObject.wait();
            }
        } catch (InterruptedException e) {
        }
    }

    /*
     * The implementation should be thread safe
     */
    public abstract T2 processInput(T1 input, T3 input2);

    /*
     * The implementation should be thread safe
     */
    public abstract void handleOutput(T2 output);

    /*
     * The implementation should be thread safe
     */
    public abstract void onWorkerThreadComplete();

    /*
     * The implementation should be thread safe
     */
    public abstract void onOutputHandleThreadComplete();
}
