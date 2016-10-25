package com.android.uiautomator.actions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;

import com.android.uiautomator.NewScriptDialog;
import com.android.uiautomator.UiAutomatorView;
import com.jack.model.AppiumConfig;
import com.jack.utils.ErrorHandler;
import com.jack.utils.FileUtil;
import com.jack.utils.XmlUtils;

public class FinishRecordAction extends Action{
	private UiAutomatorView mView;
	public FinishRecordAction(UiAutomatorView view) {
		// TODO Auto-generated constructor stub
		super("&Finish Record");
        mView = view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//then ,repaint and refresh the data
    	ProgressMonitorDialog dialog = new ProgressMonitorDialog(mView.getShell());
        try {
            dialog.run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
                InterruptedException {
                	monitor.subTask("Save Script ...");
            		//save operations and release resource
            		ArrayList<com.jack.model.Action> actions = mView.getActions();
            		String script = NewScriptDialog.getsScriptSaveFile();
            		XmlUtils.createXml(script,
            				NewScriptDialog.getsAPKFile().getAbsolutePath(), 
            				AppiumConfig.getmPackage(), 
            				String.valueOf(NewScriptDialog.getmPort()), 
            				AppiumConfig.getmActivity(), 
            				actions);
                	monitor.subTask("Save Script, 10%");
                	ArrayList<String> pages = mView.getPageSources();
                	if(pages != null && pages.size() > 0){
                		File scriptFile = new File(script);
                		String pageSavePath = scriptFile.getParent() + File.separator + "pages";
                		FileUtil.createDir(pageSavePath);
                		int i = 0;
                		for(String page:pages){
                			try {
								FileUtil.writeAll(pageSavePath + File.separator + String.valueOf(i) + ".xml", page);
			                	monitor.subTask("Save Script, " + String.valueOf((i++)*100/pages.size()) + " %");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                		}
                	}
                	monitor.subTask("Save Script done, quit appium...");
                	mView.getAppiumConfig().getDriver().quit();
            		// kill appium
                	mView.getAppiumConfig().close();
                	monitor.subTask("quit appium success, release page ..");
                	Display.getDefault().syncExec(new Runnable() {
                	    public void run() {
                    		mView.setModel(null, null, null);
                	    }
                	});
                	monitor.subTask("done");
//                    AppiumConfig.getDriver().quit();
                    monitor.done();
                    System.out.println("monitor done!!!");
                }
            });
        } catch (Exception e) {
        	e.printStackTrace();
            ErrorHandler.showError(mView.getShell(), "Save script", e);
        }
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#setImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
	 */
	@Override
	public ImageDescriptor getImageDescriptor(){
		// TODO Auto-generated method stub
		return ImageDescriptor.createFromFile(null, "images/finish.png");
	}
}