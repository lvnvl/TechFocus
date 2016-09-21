package com.android.uiautomator.actions;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.android.uiautomator.NewScriptDialog;
import com.android.uiautomator.UiAutomatorView;
import com.jack.model.AppiumConfig;
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
		//save operations and release resource
		ArrayList<com.jack.model.Action> actions = mView.getActions();
		String script = NewScriptDialog.getsScriptSaveFile();
		XmlUtils.createXml(script,
				NewScriptDialog.getsAPKFile().getAbsolutePath(), 
				NewScriptDialog.getmPackage(), 
				String.valueOf(NewScriptDialog.getmPort()), 
				NewScriptDialog.getmActivity(), 
				actions);
		AppiumConfig.getDriver().quit();
		mView.setModel(null, null, null);
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