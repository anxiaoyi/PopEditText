package com.zk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 让EditText完美配合的Keyboard的显示和隐藏，一般多用于即时通讯的聊天界面，如QQ和微信等.<br>
 * <p>如何使用：</p>
 *<p> <pre>
 * mPopEditText.setContainer("The container which holds the view that you want to show.").
 * mButton.setOnClickListener(new View.OnClickListener(){
 * 	public void onClick(View v){
 * 		mPopEditText.dispatchOnClickEvent(v, "你想要让Container显示的View")
 * 	}
 * });
 * </pre></p>
 * 
 * @author anxiaoyi
 *
 */
public class PopEditText extends EditText implements OnGlobalLayoutListener, OnFocusChangeListener, OnClickListener {
	
	/** 默认键盘高度 **/
	private static final int DEFAULT_KEYBOARD_HEIGHT = 480;
	
	/** 放置底部View界面的ViewGroup **/
	private ViewGroup mContainer;
	/** 当前显示的View **/
	private View mCurrentShowView;

	/** 键盘高度 **/
	private int keyBoardHeight;
	/** input manager **/
	private InputMethodManager mInputManager;
	/** 键盘是否显示 **/
	private boolean isKeyboardShow;
	/** 当前键盘的值是真实的值还是虚拟的值 **/
	private boolean isKeyboardHeightReal;
	/** 是否需要调整Container高度至0 **/
	private boolean resetKeyboardHeight;
	
	/** mActivity **/
	private Activity mActivity;
	
	public PopEditText(Context context) {
		super(context);
		init(context);
	}

	public PopEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PopEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context){
		if(isInEditMode()){
			return;
		}
		
		mActivity = (Activity) context;
		mInputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		this.getViewTreeObserver().addOnGlobalLayoutListener(this);
		this.setOnFocusChangeListener(this);
		this.setOnClickListener(this);
		
		clearFocus();
		setKeyboardAdjustNothing();
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (isKeyboardShow()) {
				hideKeyboard();
				return true;
			} else {
				if (isContainerVisible()) {
					mContainer.setVisibility(View.GONE);
					return true;
				}
			}
		}
		return super.dispatchKeyEventPreIme(event);
	}
	
	/**
	 * Set view container
	 * @param container
	 */
	public void setContainer(ViewGroup container){
		this.mContainer = container;
	}
	
	/**
	 * 隐藏或者显示键盘
	 * @param clickView 点击的按钮
	 * @param showView 想要显示或者隐藏的view
	 */
	public void dispatchOnClickEvent(final View clickView, final View showView){
		if (isContainerHide()) {
			if (isKeyboardShow()) {
				setKeyboardAdjustNothing();
				showView(showView);
				showContainer();
				hideKeyboard();
			} else {
				setContainerHeight(getKeyboardHeight());
				showContainer();
				showView(showView);
			}
		} else {
			if (isKeyboardShow()) {
				setKeyboardAdjustNothing();
				hideKeyboard();
				setContainerHeight(getKeyboardHeight());
				showView(showView);
			} else {
				if (isKeyboardHeightReal()) {
					if(equal(showView)){
						reShowKeyboard(showView);
					}else{
						setContainerHeight(getKeyboardHeight());
						showView(showView);
					}
				} else {
					resetContainerHeight();
					setKeyboardAdjustResize();

					if(equal(showView)){
						requestFocus();
						showKeyboard();
					}else{
						setContainerHeight(getKeyboardHeight());
						showView(showView);
					}
				}
			}
		}
	}
	
	/**
	 * 重新显示键盘
	 * @param showView
	 */
	private void reShowKeyboard(final View showView) {
		setKeyboardAdjustNothing();
		requestFocus();
		showKeyboard();
		setContainerHeight(getKeyboardHeight());
		showView(showView);
	}
	
	/**
	 * 不让键盘做任何调整
	 */
	private void setKeyboardAdjustNothing() {
		mActivity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
	}

	/**
	 * 让键盘自动调整
	 */
	private void setKeyboardAdjustResize() {
		mActivity
				.getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
								| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	/**
	 * 显示键盘
	 */
	private void showKeyboard() {
		mInputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
		isKeyboardShow = true;
	}

	/**
	 * 隐藏键盘
	 */
	private void hideKeyboard() {
		mInputManager.hideSoftInputFromWindow(getWindowToken(), 0);
		isKeyboardShow = false;
	}
	
	/**
	 * 当前键盘有没有显示
	 * @return
	 */
	private boolean isKeyboardShow() {
		return isKeyboardShow;
	}
	
	/**
	 * 得到当前键盘高度
	 * @return
	 */
	private int getKeyboardHeight() {
		int height = keyBoardHeight > 0 ? keyBoardHeight : DEFAULT_KEYBOARD_HEIGHT;
		isKeyboardHeightReal = keyBoardHeight > 0;
		return height;
	}
	
	/**
	 * 当前显示的键盘高度是否是真实的键盘高度
	 * @return
	 */
	private boolean isKeyboardHeightReal(){
		return isKeyboardHeightReal;
	}
	
	/**
	 * 重置ContainerHeight为0
	 */
	private void resetContainerHeight() {
		resetKeyboardHeight = true;
	}
	
	/**
	 * Container是否可见
	 * @return
	 */
	private boolean isContainerVisible() {
		return mContainer.getVisibility() == View.VISIBLE
				&& mContainer.getMeasuredHeight() > 0;
	}
	
	/**
	 * Container是否正在显示
	 * @return
	 */
	private boolean isContainerHide() {
		return mContainer.getVisibility() == View.GONE;
	}
	
	/**
	 * 显示Container
	 */
	private void showContainer(){
		mContainer.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 调整Container高度
	 * @param height
	 */
	private void setContainerHeight(int height){
		ViewGroup.LayoutParams params = mContainer.getLayoutParams();
		params.height = height;
		mContainer.setLayoutParams(params);
	}
	
	/**
	 * 让当前的Container显示此view
	 * @param showView 想要显示的view
	 */
	private void showView(final View showView){
		mCurrentShowView = showView;
		mContainer.removeAllViews();
		mContainer.addView(showView);
	}
	
	/**
	 * 当前想要隐藏或者显示的view是否就是当前已经显示的
	 * @param showView
	 * @return
	 */
	private boolean equal(final View showView){
		return showView != null && mCurrentShowView == showView;
	}

	@Override
	public void onGlobalLayout() {
		Rect r = new Rect();
		getWindowVisibleDisplayFrame(r);

		int screenHeight = getRootView().getHeight();
		int heightDifference = screenHeight - (r.bottom - r.top);
		int resourceId = getContext().getResources()
				.getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			heightDifference -= getContext().getResources()
					.getDimensionPixelSize(resourceId);
		}

		if (heightDifference > 100) {
			keyBoardHeight = heightDifference;

			if(resetKeyboardHeight){
				setContainerHeight(0);
				resetKeyboardHeight = false;
				isKeyboardShow  = true;
				
				if(isContainerHide()){
					showContainer();
				}
			}
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			resetContainerHeight();
		}
	}

	@Override
	public void onClick(View v) {
		if (!isKeyboardShow()) {
			if (mContainer.getMeasuredHeight() > 0) {
				if (isContainerHide()) {
					showContainer();
					setContainerHeight(0);
					setKeyboardAdjustResize();
					showKeyboard();
				} else {
					if (isKeyboardHeightReal()) {
						setContainerHeight(getKeyboardHeight());
						setKeyboardAdjustNothing();
						showKeyboard();
					} else {
						setKeyboardAdjustResize();
						resetContainerHeight();
						requestFocus();
						showKeyboard();
					}
				}
			} else {
				if (isContainerHide()) {
					showContainer();
				}
				setContainerHeight(0);
				setKeyboardAdjustResize();
				showKeyboard();
			}
		} else {
			resetContainerHeight();
		}
	}
	
	@Override
	public Parcelable onSaveInstanceState() {
		SavedState ss = new SavedState(super.onSaveInstanceState());
		ss.isKeyboardHeightReal = isKeyboardHeightReal;
		ss.isKeyboardShow = isKeyboardShow;
		ss.keyboardHeight = keyBoardHeight;
		ss.resetKeyboardHeight = resetKeyboardHeight;
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState)state;
		super.onRestoreInstanceState(state);
		
		isKeyboardHeightReal  = ss.isKeyboardHeightReal;
		isKeyboardShow = ss.isKeyboardShow;
		keyBoardHeight = ss.keyboardHeight;
		resetKeyboardHeight = ss.resetKeyboardHeight;
		
		if(isKeyboardShow){
			showKeyboard();
		}
	}

	/** 保存状态 **/
	public static class SavedState extends BaseSavedState{

		boolean isKeyboardShow;
		boolean isKeyboardHeightReal;
		int keyboardHeight;
		boolean resetKeyboardHeight;
		
		public SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			
			dest.writeInt(isKeyboardShow ? 1 : 0);
			dest.writeInt(isKeyboardHeightReal ? 1 : 0);
			dest.writeInt(keyboardHeight);
			dest.writeInt(resetKeyboardHeight ? 1 : 0);
		}
	}
}
