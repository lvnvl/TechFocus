package com.android.uiautomator;

import java.io.File;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.android.ddmlib.IDevice;

public class RunScriptDialog extends Dialog {
    private static final int FIXED_TEXT_FIELD_WIDTH = 300;
    private static final int DEFAULT_LAYOUT_SPACING = 10;
	private Text mScriptSaveText;
    private Text mDeviceText;
	private static File sScriptFile;
    private static IDevice sIDevice;
	private int sSelectedDeviceIndex = 0;
	public RunScriptDialog(Shell shell) {
		super(shell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	}

	@Override
	protected Control createDialogArea(Composite parent){
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(1, false);
        gl_container.verticalSpacing = DEFAULT_LAYOUT_SPACING;
        gl_container.horizontalSpacing = DEFAULT_LAYOUT_SPACING;
        gl_container.marginWidth = DEFAULT_LAYOUT_SPACING;
        gl_container.marginHeight = DEFAULT_LAYOUT_SPACING;
        container.setLayout(gl_container);

        //open script composite
        Group openScriptGroup = new Group(container, SWT.NONE);
        openScriptGroup.setLayout(new GridLayout(2, false));
        openScriptGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        openScriptGroup.setText("Open");

        mScriptSaveText = new Text(openScriptGroup, SWT.BORDER | SWT.READ_ONLY);
        if (sScriptFile != null) {
            mScriptSaveText.setText(sScriptFile.getAbsolutePath());
        }
        GridData gd_ScriptSaveText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_ScriptSaveText.minimumWidth = FIXED_TEXT_FIELD_WIDTH;
        gd_ScriptSaveText.widthHint = FIXED_TEXT_FIELD_WIDTH;
        mScriptSaveText.setLayoutData(gd_ScriptSaveText);

        Button openScriptButton = new Button(openScriptGroup, SWT.NONE);
        openScriptButton.setText("...");
        openScriptButton.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                handleOpenScriptFile();
            }
        });
      
        //Devices choose composite
        Group chooseDeviceGroup = new Group(container, SWT.NONE);
        chooseDeviceGroup.setLayout(new GridLayout(1, false));
        chooseDeviceGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        chooseDeviceGroup.setText("select device");
        List<IDevice> devices = DebugBridge.getDevices();
        String[] mDeviceNames;
        mDeviceNames = new String[devices.size()];
        for (int i = 0; i < devices.size(); i++) {
            mDeviceNames[i] = devices.get(i).getName();
        }
        final Combo combo = new Combo(chooseDeviceGroup, SWT.BORDER | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        combo.setItems(mDeviceNames);
        int defaultSelection =
                sSelectedDeviceIndex < devices.size() ? sSelectedDeviceIndex : 0;
        combo.select(defaultSelection);
        sSelectedDeviceIndex = defaultSelection;
        sIDevice = devices.get(sSelectedDeviceIndex);

        combo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                sSelectedDeviceIndex = combo.getSelectionIndex();
                sIDevice = devices.get(sSelectedDeviceIndex);
            }
        });

		return container;
	}

    private void handleOpenScriptFile() {
        FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
        fd.setText("Open Script File");
        File initialFile = sScriptFile;
        // if file has never been selected before, try to base initial path on the mXmlDumpFile
        if (initialFile != null) {
            if (initialFile.isFile()) {
                fd.setFileName(initialFile.getAbsolutePath());
            } else if (initialFile.isDirectory()) {
                fd.setFilterPath(initialFile.getAbsolutePath());
            }
        }
        String[] filter = {"*.xml"};
        fd.setFilterExtensions(filter);
        String selected = fd.open();
        if (selected != null) {
            sScriptFile = new File(selected);
            mScriptSaveText.setText(selected);
        }
    }

    /**
	 * @return the sAPKFile
	 */
	public static IDevice getsIDevice() {
		return sIDevice;
	}

	/**
	 * @return the mDeviceText
	 */
	public Text getmDeviceText() {
		return mDeviceText;
	}

	/**
	 * @return the sScriptFile
	 */
	public static File getsScriptFile() {
		return sScriptFile;
	}

	/**
	 * @param sScriptFile the sScriptFile to set
	 */
	public static void setsScriptFile(File sScriptFile) {
		RunScriptDialog.sScriptFile = sScriptFile;
	}
	
}