package com.lazy.dropdownlistview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.lazy.dropdownlistview.R;

/**
 * 头部
 */
public class HeaderView extends LinearLayout {
	/** 刷新状态 */
	private LoadState mState = LoadState.NORMAL;

	private View mHeader = null;
	private ImageView mArrow = null;
	private final static int ROTATE_DURATION = 250;

	/** 一分钟的毫秒值，用于判断上次的更新时间. */
	private final long ONE_MINUTE = 60 * 1000;
	/** 一小时的毫秒值，用于判断上次的更新时间. */
	private final long ONE_HOUR = 60 * ONE_MINUTE;
	/** 一天的毫秒值，用于判断上次的更新时间. */
	private final long ONE_DAY = 24 * ONE_HOUR;
	/** 一月的毫秒值，用于判断上次的更新时间. */
	private final long ONE_MONTH = 30 * ONE_DAY;
	/** 一年的毫秒值，用于判断上次的更新时间. */
	private final long ONE_YEAR = 12 * ONE_MONTH;

	public HeaderView(Context context) {
		this(context, null);
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView(context);
	}

	private void initHeaderView(Context context) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mHeader = LayoutInflater.from(context).inflate(R.layout.g_refresh_header, null);
		addView(mHeader, lp);
		setGravity(Gravity.BOTTOM);
		mArrow = (ImageView) mHeader.findViewById(R.id.iv);
		Log.e("HeaderView","HeaderView初始化了");
	}

	public void setHeaderState(LoadState state) {
		if (mState == state) {
			return;
		}
		mArrow.clearAnimation();
		switch (state) {
		case NORMAL:

			break;

		case WILL_RELEASE:

			break;

		case LOADING:

			break;

		default:
			break;
		}

		mState = state;
	}

	public LoadState getCurrentState() {
		return mState;
	}

	public void setHeaderHeight(int height) {
		if (height <= 0) {
			height = 0;
		}
		LayoutParams lp = (LayoutParams) mHeader.getLayoutParams();
		lp.height = height;
		mHeader.setLayoutParams(lp);
	}

	public int getHeaderHeight() {
		return mHeader.getHeight();
	}

	/**
	 * 刷新下拉头中上次更新时间的文字描述。
	 */
	protected void refreshUpdatedAtValue(long lastUpdateTime) {

	}
}
