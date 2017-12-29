package io.github.viscent.mtia.util.stf;

import java.lang.reflect.Method;
import java.util.Observer;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@ConcurrencyTest
public class TestRunner {
	private static final Semaphore FLOW_CONTROL = new Semaphore(Runtime
			.getRuntime().availableProcessors());
	
	private static final ExecutorService EXECUTOR_SERVICE = Executors
			.newCachedThreadPool(new ThreadFactory(){
				
				@Override
				public Thread newThread(Runnable r){
					Thread t = new Thread(r);
					t.setPriority(Thread.MAX_PRIORITY);
					t.setDaemon(false);
					return t;
				}
			});
					
	private final AtomicInteger runs = new AtomicInteger(0);				
	private final int iterations;				
	private final int thinkTime;				
	private final Method publishMethod;				
	private final Method observerMethod;				
    private final Object testCase;
    private final SortedMap<Integer, ExpectInfo> expectMap;
    private volatile boolean stop = false;				
    private volatile Method setupMethod = null;				
					
    public TestRunner(Method publishMethod, Method observerMethod,
            Method setupMethod, Object testCase) {
    	
    	this.publishMethod = publishMethod;
        this.observerMethod = observerMethod;
        this.setupMethod = setupMethod;
        this.testCase = testCase;
        this.expectMap = parseExpects(getExpects(observerMethod));
        ConcurrencyTest testCaseAnn = testCase.getClass().getAnnotation(
                ConcurrencyTest.class);
        iterations = testCaseAnn.iterations();
        thinkTime = testCaseAnn.thinkTime();
    }
  
    public static void runTest(Class<?> testCaseClazz)	
    		throws InstantiationException, IllegalAccessException {
    
    	Object test = testCaseClazz.newInstance();
    	Method publishMethod = null;
        Method observerMethod = null;
        Method setupMethod = null;
        for (Method method : testCaseClazz.getMethods()) {
            if (method.getAnnotation(Actor.class) != null) {
                publishMethod = method;
            }
            if (method.getAnnotation(Observer.class) != null) {
                observerMethod = method;
            }
            if (method.getAnnotation(Setup.class) != null) {
                setupMethod = method;
            }
        }
        
        TestRunner runner = new TestRunner(publishMethod, observerMethod,
                setupMethod, test);
        runner.doTest();
        
    }
    
    private static Expect[] getExpects(final Method observerMethod) {
    	Observer observerAnn = observerMethod.getAnnotation(Observer.class);
    	Expect[] expects = observerAnn.value();
    	return expects;
    }
    
    private static SortedMap<Integer, ExpectInfo> parseExpects(
            final Expect[] expects) {
        SortedMap<Integer, ExpectInfo> map = new ConcurrentSkipListMap<Integer, ExpectInfo>();
        for (Expect expect : expects) {
            map.put(Integer.valueOf(expect.expected()), new ExpectInfo(expect.desc()));
        }
        return map;
    }
    
					
    private static class ExpectInfo{
    	public final String description;
    	private final AtomicInteger counter;
    
    	public ExpectInfo(String description) {
            this(description, 0);
        }
    	
    	public ExpectInfo(String description, int hitCount) {
            this.description = description;
            this.counter = new AtomicInteger(hitCount);
        }
    	
    	public int hit() {
            return counter.incrementAndGet();
        }
    	
    	 public int count() {
             return counter.get();
         }
   
    }
    
    private static class DummyLatch extends CountDownLatch{
    	public DummyLatch(int count) {
            super(count);
        }
    
    	@Override
        public void await() throws InterruptedException {
            //;
        }
    	
    	@Override
        public boolean await(long timeout, TimeUnit unit)
                throws InterruptedException {
            return true;
        }
    	
    	 @Override
         public void countDown() {
             ;
         }
    	 
    	 @Override
         public long getCount() {
             return 0;
         }
    }
					

}
