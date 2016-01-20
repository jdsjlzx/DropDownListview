package com.lazy.dropdownlistview.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.OverScroller;
import com.lazy.dropdownlistview.listenenr.XrefershListviewListener;

/**
 * 作者：liujingyuan on 2015/12/21 13:14
 * 邮箱：906514731@qq.com
 */
public class DropDownListview extends ListView
        implements AbsListView.OnScrollListener {
    private final static String TAG = "[DropDownListview]";
    private final static float OFFSET_Y = 0.7f;
    private OverScroller mScroller;
    private final static int SCROLL_HEADER = 0;
    private final static int SCROLL_FOOTER = 1;
    //刷新的监听
    public XrefershListviewListener mListViewListener;
    /** 滑动项. */
    private int iScrollWhich = SCROLL_HEADER;
    //是否显示footview
    public boolean isShowLoadeFooterView = false;
    HeaderView mHeaderView;
    int iHeaderHeight;
    int iFooterHeight;
    float EndRawy;
    float mStartY;
    float dy;
    private float mLastY;
    View rlTitle;

    public DropDownListview(Context context) {
        super(context);
        initHeardView(context);
    }


    public DropDownListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeardView(context);
    }


    public DropDownListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeardView(context);
    }


    //初始化头部的view
    private void initHeardView(Context context) {
        //初始化scmScroller
        mScroller = new OverScroller(context, new DecelerateInterpolator());
        mHeaderView = new HeaderView(context);
        //初始化hearview的高度
        mHeaderView.setHeaderHeight(200);
        addHeaderView(mHeaderView);
        this.setOnScrollListener(this);
        //监听到view加载完毕获取view的高度
        mHeaderView.getViewTreeObserver()
                   .addOnGlobalLayoutListener(
                           new ViewTreeObserver.OnGlobalLayoutListener() {
                               @Override public void onGlobalLayout() {
                                   iHeaderHeight
                                           = mHeaderView.getMeasuredHeight();
                                   Log.d(TAG,
                                           "iHeaderHeight = " + iHeaderHeight);
                                   //刚进来的时候我们隐藏heardview
                                   mHeaderView.setPadding(0, -iHeaderHeight, 0,
                                           0);
                                   getViewTreeObserver().removeGlobalOnLayoutListener(
                                           this);
                               }
                           });
    }


    @Override public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下的时候我们需要记住起始点的Y轴的坐标
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动的时候获取当前的y轴的坐标
                mStartY = ev.getY();
                //计算滑动的距离
                dy = mStartY - mLastY;
                //再次获取一次，因为我们是增量增加宽度的，每次获取的是相对于上一点的位移
                mLastY = ev.getY();
                if (getFirstVisiblePosition() == 0 &&
                        (mHeaderView.getMeasuredHeight() > 0 || dy > 0)) {
                    //通过改变heardview的高度
                    upDataHeardView(dy * 0.7f);
                    rlTitle.getBackground().setAlpha((int) dy);
                }
                Log.e(TAG, "dy" +
                        "==" + dy);
                break;
            case MotionEvent.ACTION_UP:
                EndRawy = ev.getRawY();
                //如果当前的可见的第一个条目是0，并且当前的hreadview的高度不为0，滑动的距离大于0就证明是在下拉刷新
                if (getFirstVisiblePosition() == 0 &&
                        (mHeaderView.getMeasuredHeight() > 0 || dy > 0)) {
                    //判断当前的状态不是正在刷新的状态就设置为刷新的状态
                    if (mHeaderView.getCurrentState() != LoadState.LOADING) {
                        if (mHeaderView.getHeaderHeight() > iHeaderHeight) {
                            mHeaderView.setHeaderState(LoadState.LOADING);
                            if (null != mListViewListener) {
                                //调用是界面的刷新,延时1秒
                                mListViewListener.onRefresh();
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override public void run() {
                                    resetHeaderHeight();
                                }
                            }, 1000);
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 重置heardview的状态
     */
    private void resetHeaderHeight() {
        //重置刷新的状态
        mHeaderView.setHeaderState(LoadState.NORMAL);
        //获取测量得到的heardview的高度
        int height = mHeaderView.getMeasuredHeight();
        //如果获取当前的View的高度等于0代表是没有移动就不做处理
        if (height == 0) // not visible.
        {
            return;
        }
        int finalHeight = 0;
        //如果超过HeaderView高度，则回滚到HeaderView高度即可
        if (height > iHeaderHeight &&
                mHeaderView.getCurrentState() != LoadState.NORMAL) {
            finalHeight = iHeaderHeight;
        }
        //标记当前是heardview在移动
        iScrollWhich = SCROLL_HEADER;
        //inalHeight - height， finalHeight代表heardview抬起的时候高度-heardview初始化的高度=heardview在y轴上移动的距离，280毫秒内移动完毕
        //int startX,开始的位置
        // int mLastY,y位置开始的位置
        // int dx, x滑动的距离
        // int dy, y滑动的距离
        // int duration执行完毕需要的时间
        mScroller.startScroll(0, height, 0, finalHeight - height, 300);
        //手动调用刷新移动
        invalidate();
    }


    /**
     * 设置头部的view的高度，来达到下移的效果
     */
    private void upDataHeardView(float dy) {
        //如果当前的状态不是正在加载中，就改变状态
        if (mHeaderView.getCurrentState() != LoadState.LOADING) {
            //如果当前的heardview的高度大于原始的高度就代表用户下拉刷新了要该表状态，变成下拉刷新的状态
            if (mHeaderView.getHeaderHeight() > iHeaderHeight) {
                mHeaderView.setHeaderState(LoadState.WILL_RELEASE);
            }
            else {
                mHeaderView.setHeaderState(LoadState.NORMAL);
            }
        }
        //移动距离等于move的距离加上heardview的高度
        mHeaderView.setHeaderHeight((int) (dy + mHeaderView.getHeaderHeight()));
    }


    /**
     * 设置刷新的监听
     */
    public void setXrefershListviewListener(XrefershListviewListener l) {
        mListViewListener = l;
    }


    //每次移动都会调用computeScroll
    @Override public void computeScroll() {
        //判断是否滑动结束
        if (mScroller.computeScrollOffset()) {
            //代表是下拉刷新
            if (iScrollWhich == SCROLL_HEADER) {
                //获取到当前滚动的位置，来动态的设置mHeaderView的高度
                mHeaderView.setHeaderHeight(mScroller.getCurrY());
            }
            invalidate();
        }
    }


    //监听listview的滑动监听
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }


    //监听listview的滑动监听
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }


    public void setView(View rlTitle) {
        this.rlTitle=rlTitle;
    }
}
