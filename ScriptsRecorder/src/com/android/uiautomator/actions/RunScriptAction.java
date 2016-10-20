package com.android.uiautomator.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.openqa.selenium.By;

import com.android.ddmlib.IDevice;
import com.android.uiautomator.DebugBridge;
import com.android.uiautomator.RunScriptDialog;
import com.android.uiautomator.UiAutomatorViewer;
import com.jack.model.AppiumConfig;
import com.jack.model.Operate;
import com.jack.model.ScriptConfig;
import com.jack.utils.XmlUtils;

public class RunScriptAction extends Action {

	private UiAutomatorViewer mViewer;
	
	public RunScriptAction(UiAutomatorViewer viewer) {
		super("&Run Script");
        mViewer = viewer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return ImageDescriptor.createFromFile(null, "images/run.png");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		List<IDevice> devices = DebugBridge.getDevices();
        if(devices.size() < 1){
        	MessageBox mb = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR);
			mb.setMessage("no devices detected,please check and try again latter");
			mb.open();
			return;
        }
		// TODO Auto-generated method stub
        RunScriptDialog d = new RunScriptDialog(Display.getDefault().getActiveShell());
        if (d.open() != RunScriptDialog.OK) {
            return;
        }

        if(RunScriptDialog.getsIDevices().size() < 1){
        	showError("no devices selected", new Exception("please select at lease one device"));
        	return;
        }
        
        /**
         * run script and show the steps
         */
        ScriptConfig sc = XmlUtils.readScript(RunScriptDialog.getsScriptFile());
        if(sc == null){
        	showError("Unexpected error while parsing script", new Exception("script broken"));
        	return;
        }
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(mViewer.getShell());
        try {
            dialog.run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
                InterruptedException {         
                	monitor.subTask("Initating appium ...");
                	new AppiumConfig(new File(sc.getApkPath()), 
                			RunScriptDialog.getsIDevices().get(0),
                			sc.getPackageName(),
                			sc.getActivity(),
                			Integer.parseInt(sc.getPort()));
                	if(AppiumConfig.getDriver() == null){
                		showError("start appium first!", new Exception("appium init error"));
                	}
                	monitor.subTask("perform operations ...");
                	int i = 1;int total = sc.getOperations().size();
                	
                	for(String s:sc.getOperations()){
                		Thread.sleep(2*1000);
                		String type = s.split("::")[0];
                		try{
                			if(Operate.CLICK.equals(type)){
                    			AppiumConfig.getDriver().findElement(By.xpath(s.split("::")[1])).click();
                    		}else if(Operate.INPUT.equals(type)){
                    			//TODO input process
                    			AppiumConfig.getDriver().findElement(
                            			By.xpath(s.split("::")[1].split("[|]")[0])).sendKeys(s.split("[|]")[1]);
                    		}
                    		monitor.subTask(String.valueOf((i++)*100/total)+"%");
                		}catch(Exception e){
                			e.printStackTrace();
                			AppiumConfig.getDriver().quit();
                            monitor.done();
                			return;
                		}
                	}
                	AppiumConfig.getDriver().quit();
                    monitor.done();
                }
            });
        } catch (Exception e) {
            showError("Unexpected error while obtaining UI hierarchy", e);
        }
	}

    private void showError(final String msg, final Throwable t) {
        mViewer.getShell().getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
                Status s = new Status(IStatus.ERROR, "Screenshot", msg, t);
                ErrorDialog.openError(
                        mViewer.getShell(), "Error", "Error obtaining UI hierarchy", s);
            }
        });
    }
}