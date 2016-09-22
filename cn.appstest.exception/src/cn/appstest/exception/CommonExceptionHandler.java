package cn.appstest.exception;

public class CommonExceptionHandler implements Thread.UncaughtExceptionHandler{

	public interface ExceptionCallBack {
        public void callWhenExceptionHappen();
    }
	
	private final static String             TAG      = "My Common Exception Handler";

    private ExceptionCallBack               mycafe   = null;
    private Thread.UncaughtExceptionHandler morignal = null;

    public CommonExceptionHandler(Thread.UncaughtExceptionHandler orignal, ExceptionCallBack myCallBack) {
        this.morignal = orignal;
        this.mycafe = myCallBack;
    }

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// TODO Auto-generated method stub
        System.out.println(TAG + " this is in common exceptionhandler");
        e.printStackTrace();
        this.mycafe.callWhenExceptionHappen();
        this.morignal.uncaughtException(t, e);
	}
}