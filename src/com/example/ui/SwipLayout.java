package com.example.ui;

import Utils.Utils;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable.Callback;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class SwipLayout extends FrameLayout {
	private String TAG = "event";
	private ViewDragHelper mDragHelper;
	private static enum Status {Close,Open,Draging};
	private Status status = Status.Close;
	private OnSwipLayoutListener swipLayoutListener;
	
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public OnSwipLayoutListener getSwipLayoutListener() {
		return swipLayoutListener;
	}

	public void setSwipLayoutListener(OnSwipLayoutListener swipLayoutListener) {
		this.swipLayoutListener = swipLayoutListener;
	}
	public static interface OnSwipLayoutListener{
		void onOpen(SwipLayout mSwipLayout);
		void onClose(SwipLayout mSwipLayout);
		void onDraging(SwipLayout mSwipLayout);
		//要去开启
		void onStartOpen(SwipLayout mSwipLayout);
		//要去关闭
		void onStartClose(SwipLayout mSwipLayout);
	}

	public SwipLayout(Context context) {
		this(context,null);
		
	}

	public SwipLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		
	}

	public SwipLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
	}
	
	ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
		
		@Override
		public boolean tryCaptureView(View arg0, int arg1) {
			
			return true;
		}
		//重写监听
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			//修正left的值,限定活动范围
			if(child == mFrontView){
				if(left > 0){
					return 0;
				}else if(left < -mRange){
					return -mRange;
				}
			}else if(child == mBackView){
				if(left > mWidth){
					return mWidth;
				}if(left <mWidth - mRange){
					return mWidth - mRange;
				}
			}
			return left;
			
		};
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			//让两个View一起动
			if(changedView == mFrontView){
				mBackView.offsetLeftAndRight(dx);
			}else if(changedView == mBackView){
				mFrontView.offsetLeftAndRight(dx);
			}
			dispatchSwipEvent();
			//兼容老版本
			invalidate();
		};
		
		
		//手指释放的时候
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			
				if(releasedChild == mFrontView){
					if(xvel == 0 && mFrontView.getLeft()<= -mRange/2.0f){
						open();
					}else if(xvel <0){
						open();
					}else{
						close();
					}
				}
		};
	};
	public void close() {
		Utils.showToast(getContext(), "close");
		close(true);
		//layoutContent(false);close();
	}
	public void close(boolean isSmooth){
		int finalLeft = 0;
		if(isSmooth){
			if(mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)){
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else{
			layoutContent(false);
			}
		}
	public void open() {
		Utils.showToast(getContext(), "open");
		open(true);
		//layoutContent(true);
		
	}
	public void open(boolean isSmooth){
		int finalLeft = -mRange;
		if(isSmooth){
			if(mDragHelper.smoothSlideViewTo(mFrontView, -mRange, 0)){
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else{
			layoutContent(true);
		}
	}
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if(mDragHelper.continueSettling(true)){
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
	private View mBackView;
	private View mFrontView;
	private int mHeight;
	private int mWidth;
	private int mRange;
	
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		return mDragHelper.shouldInterceptTouchEvent(ev);
		
	};
	//更新status
			private void dispatchSwipEvent() {
				swipLayoutListener.onDraging(this);
				//上一次状态
				
				Status preStatus = status;
				//更新当前状态
				
				status = updateStatus();
				
				if(status != preStatus && swipLayoutListener != null){
					if(status == Status.Close){
						swipLayoutListener.onClose(this);
					}else if(status == Status.Open){
						swipLayoutListener.onOpen(this);
					}else if(status == Status.Draging){
						if(preStatus == Status.Close){
							swipLayoutListener.onStartOpen(this);
						}else if(preStatus == Status.Open){
							swipLayoutListener.onStartClose(this);
						}
					}
				}
			}
			private Status updateStatus() {
				int left = mFrontView.getLeft();
				if(left == 0){
					return Status.Close;
				}else if(left == -mRange){
					return Status.Open;
				}
				return Status.Draging;
			}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		try {
			mDragHelper.processTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// 摆放位置
		layoutContent(false);
	}
	
	private void layoutContent(boolean isOpen) {
		// 摆放前View
		Rect frontRect = computeFrontViewRect(isOpen);
		mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
		// 摆放后View
		Rect backRect = computeBackViewViaFront(frontRect);
		mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);
		
		// 调整顺序, 把mFrontView前置
		bringChildToFront(mFrontView);
	}

	private Rect computeBackViewViaFront(Rect frontRect) {
		int left = frontRect.right;
		return new Rect(left, 0, left + mRange, 0 + mHeight);
	}

	private Rect computeFrontViewRect(boolean isOpen) {
		int left = 0;
		if(isOpen){
			left = -mRange;
		}
		return new Rect(left, 0, left + mWidth, 0 + mHeight);
	}

	@Override
	protected void onFinishInflate() {
		
		super.onFinishInflate();
		mBackView = getChildAt(0);
		mFrontView = getChildAt(1); 
	}
	//得到屏幕宽高等
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mHeight = mFrontView.getMeasuredHeight();
		mWidth = mFrontView.getMeasuredWidth();
		
		mRange = mBackView.getMeasuredWidth();
		
		
	}

}
