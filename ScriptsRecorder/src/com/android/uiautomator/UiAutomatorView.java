/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.uiautomator;

import com.android.uiautomator.UiAutomatorHelper.UiAutomatorException;
import com.android.uiautomator.UiAutomatorHelper.UiAutomatorResult;
import com.android.uiautomator.actions.FinishRecordAction;
import com.android.uiautomator.tree.AttributePair;
import com.android.uiautomator.tree.BasicTreeNode;
import com.android.uiautomator.tree.UiNode;
import com.jack.model.Action;
import com.jack.model.AppiumConfig;
import com.jack.model.Operate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.openqa.selenium.By;


public class UiAutomatorView extends Composite {
    private static final int IMG_BORDER = 2;

    // The screenshot area is made of a stack layout of two components: screenshot canvas and
    // a "specify screenshot" button. If a screenshot is already available, then that is displayed
    // on the canvas. If it is not availble, then the "specify screenshot" button is displayed.
    private Composite mScreenshotComposite;
//    private StackLayout mStackLayout;
//    private Composite mSetScreenshotComposite;
    private Canvas mScreenshotCanvas;

    private TableViewer mTableViewer;

    private ListViewer mListViewer;
    
    private float mScale = 1.0f;
    private int mDx, mDy;

    private UiAutomatorModel mModel;
    private ArrayList<Action> actions;
//    private File mModelFile;
    private Image mScreenshot;

    public UiAutomatorView(Composite parent, int style) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());

        SashForm baseSash = new SashForm(this, SWT.HORIZONTAL);

        mScreenshotComposite = new Composite(baseSash, SWT.BORDER);
        mScreenshotComposite.setLayout(new FillLayout());
        // draw the canvas with border, so the divider area for sash form can be highlighted
        mScreenshotCanvas = new Canvas(mScreenshotComposite, SWT.BORDER);
        mScreenshotCanvas.setMenu(createEditPopup(mScreenshotCanvas.getShell()));
        mScreenshotComposite.layout();
        mScreenshotCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                if (mModel != null) {
//                    mModel.toggleExploreMode();
//                    updateAction(mModel.getSelectedNode());
//                    redrawScreenshot();
                	if(e.button == 1){
                		//left click; perform click
                		performAction(updateAction(mModel.getSelectedNode(), Operate.CLICK, null));
                	}
//                	performAction(updateAction(mModel.getSelectedNode()));
                }
            }
        });
        mScreenshotCanvas.setBackground(
                getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        mScreenshotCanvas.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                if (mScreenshot != null) {
                    updateScreenshotTransformation();
                    // shifting the image here, so that there's a border around screen shot
                    // this makes highlighting red rectangles on the screen shot edges more visible
                    Transform t = new Transform(e.gc.getDevice());
                    t.translate(mDx, mDy);
                    t.scale(mScale, mScale);
                    e.gc.setTransform(t);
                    e.gc.drawImage(mScreenshot, 0, 0);
                    // this resets the transformation to identity transform, i.e. no change
                    // we don't use transformation here because it will cause the line pattern
                    // and line width of highlight rect to be scaled, causing to appear to be blurry
                    e.gc.setTransform(null);
                    if (mModel.shouldShowNafNodes()) {
                        // highlight the "Not Accessibility Friendly" nodes
                        e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_YELLOW));
                        e.gc.setBackground(e.gc.getDevice().getSystemColor(SWT.COLOR_YELLOW));
                        for (Rectangle r : mModel.getNafNodes()) {
                            e.gc.setAlpha(50);
                            e.gc.fillRectangle(mDx + getScaledSize(r.x), mDy + getScaledSize(r.y),
                                    getScaledSize(r.width), getScaledSize(r.height));
                            e.gc.setAlpha(255);
                            e.gc.setLineStyle(SWT.LINE_SOLID);
                            e.gc.setLineWidth(2);
                            e.gc.drawRectangle(mDx + getScaledSize(r.x), mDy + getScaledSize(r.y),
                                    getScaledSize(r.width), getScaledSize(r.height));
                        }
                    }
                    // draw the mouseover rects
                    Rectangle rect = mModel.getCurrentDrawingRect();
                    if (rect != null) {
                        e.gc.setForeground(e.gc.getDevice().getSystemColor(SWT.COLOR_RED));
//                        if (mModel.isExploreMode()) {
//                            // when we highlight nodes dynamically on mouse move,
//                            // use dashed borders
//                            e.gc.setLineStyle(SWT.LINE_DASH);
//                            e.gc.setLineWidth(1);
//                        } else {
//                            // when highlighting nodes on tree node selection,
//                            // use solid borders
//                            e.gc.setLineStyle(SWT.LINE_SOLID);
//                            e.gc.setLineWidth(2);
//                        }
                        e.gc.setLineStyle(SWT.LINE_SOLID);
                        e.gc.setLineWidth(2);
                        e.gc.drawRectangle(mDx + getScaledSize(rect.x), mDy + getScaledSize(rect.y),
                                getScaledSize(rect.width), getScaledSize(rect.height));
                    }
                }
            }
        });
        mScreenshotCanvas.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
//                if (mModel != null && mModel.isExploreMode()) {
            	if (mModel != null) {
                    BasicTreeNode node = mModel.updateSelectionForCoordinates(
                            getInverseScaledSize(e.x - mDx),
                            getInverseScaledSize(e.y - mDy));
                    if (node != null) {
                    	mModel.setSelectedNode(node);
                    	loadAttributeTable();
                        redrawScreenshot();
                    }
                }
            }
        });

        // right sash is split into 2 parts: upper-right and lower-right
        // both are composites with borders, so that the horizontal divider can be highlighted by
        // the borders
        SashForm rightSash = new SashForm(baseSash, SWT.VERTICAL);
        
        // upper-right base contains the toolbar and the tree
        Composite upperRightBase = new Composite(rightSash, SWT.BORDER);
        upperRightBase.setLayout(new GridLayout(1, false));
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		toolBarManager.add(new FinishRecordAction(this));
		ToolBar tb = toolBarManager.createControl(upperRightBase);
		tb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        actions = new ArrayList<Action>();
        mListViewer = new ListViewer(upperRightBase, SWT.BORDER);
        mListViewer.setContentProvider(new IStructuredContentProvider(){
        	@SuppressWarnings("unchecked")
			public Object[] getElements(Object inputElement){
        		ArrayList<Action> v = (ArrayList<Action>)inputElement;
        		return v.toArray();
        	}
        	public void dispose(){
        		System.out.println("list view disposing...");
        	}
        	public void inputChanged(Viewer v, Object oldO, Object newO){
        		System.out.println("list view input changed,from "+oldO+"to "+newO);
        	}
        });
        mListViewer.setInput(actions);
        mListViewer.setLabelProvider(new LabelProvider(){
        	public Image getImage(Object o){
        		return null;
        	}
        	public String getText(Object element){
        		if(Operate.CLICK.equals(((Action)element).getType())){
        			return ((Action)element).getType() + ">>" + ((Action)element).getItemName();
        		} else if(Operate.INPUT.equals(((Action)element).getType())){
        			return ((Action)element).getType() + ">>" + ((Action)element).getItemName() + "[|]" + ((Action)element).getOperation().split("[|]")[1];
        		}
            	return ((Action)element).getType() + ">>" + ((Action)element).getItemName();
            }	
        });
        mListViewer.addFilter(new ViewerFilter(){
        	public boolean select(Viewer v, Object pe, Object e){
//        		if(((Action)e).getType().equals("click")){
//        			return true;
//        		}else{
//        			return false;
//        		}
        		return true;
        	}
        });
        mListViewer.setSorter(new ViewerSorter(){
        	public int compare(Viewer v, Object o1, Object o2){
        		return ((Action)o1).getItemName().compareTo(((Action)o1).getItemName());
        	}
        });
        mListViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        // lower-right base contains the detail group
        Composite lowerRightBase = new Composite(rightSash, SWT.BORDER);
        lowerRightBase.setLayout(new FillLayout());
        Group grpNodeDetail = new Group(lowerRightBase, SWT.NONE);
        grpNodeDetail.setLayout(new FillLayout(SWT.HORIZONTAL));
        grpNodeDetail.setText("Node Detail");

        Composite tableContainer = new Composite(grpNodeDetail, SWT.NONE);

        TableColumnLayout columnLayout = new TableColumnLayout();
        tableContainer.setLayout(columnLayout);

        mTableViewer = new TableViewer(tableContainer, SWT.NONE | SWT.FULL_SELECTION);
        Table table = mTableViewer.getTable();
        table.setLinesVisible(true);
        // use ArrayContentProvider here, it assumes the input to the TableViewer
        // is an array, where each element represents a row in the table
        mTableViewer.setContentProvider(new ArrayContentProvider());

        TableViewerColumn tableViewerColumnKey = new TableViewerColumn(mTableViewer, SWT.NONE);
        TableColumn tblclmnKey = tableViewerColumnKey.getColumn();
        tableViewerColumnKey.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof AttributePair) {
                    // first column, shows the attribute name
                    return ((AttributePair)element).key;
                }
                return super.getText(element);
            }
        });
        columnLayout.setColumnData(tblclmnKey,
                new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));

        TableViewerColumn tableViewerColumnValue = new TableViewerColumn(mTableViewer, SWT.NONE);
        tableViewerColumnValue.setEditingSupport(new AttributeTableEditingSupport(mTableViewer));
        TableColumn tblclmnValue = tableViewerColumnValue.getColumn();
        columnLayout.setColumnData(tblclmnValue,
                new ColumnWeightData(2, ColumnWeightData.MINIMUM_WIDTH, true));
        tableViewerColumnValue.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof AttributePair) {
                    // second column, shows the attribute value
                    return ((AttributePair)element).value;
                }
                return super.getText(element);
            }
        });
        // sets the ratio of the vertical split: left 5 vs right 3
        baseSash.setWeights(new int[]{5, 3});
    }

    private int getScaledSize(int size) {
        if (mScale == 1.0f) {
            return size;
        } else {
            return new Double(Math.floor((size * mScale))).intValue();
        }
    }

    private int getInverseScaledSize(int size) {
        if (mScale == 1.0f) {
            return size;
        } else {
            return new Double(Math.floor((size / mScale))).intValue();
        }
    }

    private void updateScreenshotTransformation() {
        Rectangle canvas = mScreenshotCanvas.getBounds();
        Rectangle image = mScreenshot.getBounds();
        float scaleX = (canvas.width - 2 * IMG_BORDER - 1) / (float)image.width;
        float scaleY = (canvas.height - 2 * IMG_BORDER - 1) / (float)image.height;
        // use the smaller scale here so that we can fit the entire screenshot
        mScale = Math.min(scaleX, scaleY);
        // calculate translation values to center the image on the canvas
        mDx = (canvas.width - getScaledSize(image.width) - IMG_BORDER * 2) / 2 + IMG_BORDER;
        mDy = (canvas.height - getScaledSize(image.height) - IMG_BORDER * 2) / 2 + IMG_BORDER;
    }

    private class AttributeTableEditingSupport extends EditingSupport {

        private TableViewer mViewer;

        public AttributeTableEditingSupport(TableViewer viewer) {
            super(viewer);
            mViewer = viewer;
        }

        @Override
        protected boolean canEdit(Object arg0) {
            return true;
        }

        @Override
        protected CellEditor getCellEditor(Object arg0) {
            return new TextCellEditor(mViewer.getTable());
        }

        @Override
        protected Object getValue(Object o) {
            return ((AttributePair)o).value;
        }

        @Override
        protected void setValue(Object arg0, Object arg1) {
        }
    }

    /**
     * Causes a redraw of the canvas.
     *
     * The drawing code of canvas will handle highlighted nodes and etc based on data
     * retrieved from Model
     */
    public void redrawScreenshot() {
        mScreenshotComposite.layout();

        mScreenshotCanvas.redraw();
    }

    public void loadAttributeTable() {
        // udpate the lower right corner table to show the attributes of the node
        mTableViewer.setInput(mModel.getSelectedNode().getAttributesArray());
    }

    public void performAction(Action action){
    	//first,use appium to perform action
    	if(Operate.CLICK.equals(action.getType())){
    		AppiumConfig.getDriver().findElement(By.xpath(action.getxPath())).click();
        }else if(Operate.INPUT.equals(action.getType())){
        	System.out.println("\toperation:"+action.getOperation());
        	System.out.println("\toperation:"+action.getOperation().split("[|]")[1]);
        	AppiumConfig.getDriver().findElement(
        			By.xpath(action.getxPath())).sendKeys(action.getOperation().split("[|]")[1]);
        }
    	try {
			Thread.sleep(2*1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	//then ,repaint and refresh the data
    	ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
        try {
            dialog.run(true, false, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
                InterruptedException {
//                    UiAutomatorResult result = null;
                    try {
                    	UiAutomatorResult result = UiAutomatorHelper.takeSnapshot(monitor);
//                    	System.out.println("test driver init;currentActivity:" + AppiumConfig.getDriver().currentActivity());
//                      setModel(result.model, result.uiHierarchy, result.screenshot);
                        if (Display.getDefault().getThread() != Thread.currentThread()) {
                            Display.getDefault().syncExec(new Runnable() {
                                @Override
                                public void run() {
                                	setModel(result.model, result.uiHierarchy, result.screenshot);
                                }
                            });
                        } else {
                        	setModel(result.model, result.uiHierarchy, result.screenshot);
                        }
                        System.out.println("set model done!!!");
                    } catch (UiAutomatorException e) {
                        monitor.done();
                        AppiumConfig.getDriver().quit();
                        showError("Appium obtain page error", e);
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
            showError("Unexpected error while obtaining UI hierarchy", e);
        }
    }
    private void showError(final String msg, final Throwable t) {
        getShell().getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
                Status s = new Status(IStatus.ERROR, "Screenshot", msg, t);
                ErrorDialog.openError(
                        getShell(), "Error", "Error obtaining UI hierarchy", s);
            }
        });
    }
    public Action updateAction(BasicTreeNode node, String type, String arg){
    	Action action = new Action(type, ((UiNode)node).toString(), ((UiNode)node).getxPath());
    	if(Operate.INPUT.equals(type)){
    		action.setInput(arg);
//    		System.out.println("454 input is:"+arg);
    	}
    	actions.add(action);
    	System.out.println(type + action.getItemName());
    	mListViewer.refresh(false);
    	return action;
    }
    
    public Menu createEditPopup(Shell parentShell){
    	Menu popMenu = new Menu(parentShell, SWT.POP_UP);
    	MenuItem inputItem = new MenuItem(popMenu, SWT.PUSH);
    	inputItem.setText("&input");
    	inputItem.setImage(new Image(null, "images/input.png"));
    	inputItem.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				BasicTreeNode node = mModel.getSelectedNode();
				if(!((UiNode)node).getAttribute("class").contains("EditText")){
					showError("Error,not a inputable widget", new Exception("unsupport input widget"));
					return;
				}
				InputDialog idg = new InputDialog(parentShell);
				if(idg.open() != InputDialog.OK){
					return;
				}
				if(InputDialog.getInput() == null || InputDialog.getInput().trim().equals("")){
					return;
				}
				performAction(updateAction(mModel.getSelectedNode(), Operate.INPUT, InputDialog.getInput()));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    	return popMenu;
    }
    
    public void setModel(UiAutomatorModel model, String pageSource, Image screenshot) {
        mModel = model;
//        mModelFile = pageSource;

        if (mScreenshot != null) {
            mScreenshot.dispose();
        }
        mScreenshot = screenshot;

        redrawScreenshot();
    }

    public boolean shouldShowNafNodes() {
        return mModel != null ? mModel.shouldShowNafNodes() : false;
    }

    public void toggleShowNaf() {
        if (mModel != null) {
            mModel.toggleShowNaf();
        }
    }

	/**
	 * @return the actions
	 */
	public ArrayList<Action> getActions() {
		return actions;
	}
}