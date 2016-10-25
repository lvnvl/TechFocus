package com.android.uiautomator.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;

import com.android.uiautomator.UiAutomatorHelper;
import com.android.uiautomator.UiAutomatorView;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorResult;
import com.jack.model.AppiumConfig;
import com.jack.utils.ErrorHandler;

public class RefreshAction extends Action{
	private UiAutomatorView mView;
	public RefreshAction(UiAutomatorView view) {
		// TODO Auto-generated constructor stub
		super("&Refresh");
        mView = view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// refresh and repaint the current page
		//then ,repaint and refresh the data
    	ProgressMonitorDialog dialog = new ProgressMonitorDialog(mView.getShell());
        try {
            dialog.run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
                InterruptedException {
//                    UiAutomatorResult result = null;
                    try {
                    	UiAutomatorResult result = UiAutomatorHelper.takeSnapshot(monitor, mView.getAppiumConfig().getDriver());
//                    	System.out.println("test driver init;currentActivity:" + AppiumConfig.getDriver().currentActivity());
//                      setModel(result.model, result.uiHierarchy, result.screenshot);
                        if (Display.getDefault().getThread() != Thread.currentThread()) {
                            Display.getDefault().syncExec(new Runnable() {
                                @Override
                                public void run() {
                                	mView.setModel(result.model, result.uiHierarchy, result.screenshot);
                                }
                            });
                        } else {
                        	mView.setModel(result.model, result.uiHierarchy, result.screenshot);
                        }
                        System.out.println("set model done!!!");
                    } catch (UiAutomatorException e) {
                        monitor.done();
                        mView.getAppiumConfig().getDriver().quit();
                        ErrorHandler.showError(mView.getShell(), "Appium obtain page error", e);
                        return;
                    } catch (Exception e){
                    	e.printStackTrace();
                    	return;
                    }
                    
//                    AppiumConfig.getDriver().quit();
                    monitor.done();
                    System.out.println("monitor done!!!");
                }
            });
        } catch (Exception e) {
        	e.printStackTrace();
            ErrorHandler.showError(mView.getShell(), "Unexpected error while obtaining UI hierarchy", e);
        }
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#setImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
	 */
	@Override
	public ImageDescriptor getImageDescriptor(){
		// TODO Auto-generated method stub
		return ImageDescriptor.createFromFile(null, "images/refresh.png");
	}
}
