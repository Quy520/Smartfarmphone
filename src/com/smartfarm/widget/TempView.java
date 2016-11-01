package com.smartfarm.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.smartfarm.db.bean.PengInfo;
import com.smartfarm.view.AppContext;
import com.smartfarm.view.R;

public class TempView extends TextView {
	private int temp;
	private String windowString = "";
	private int color_step[] = new int[49];
	private int alfa = 255;

	public TempView(Context context) {
		this(context, null, 0);
	}

	public TempView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TempView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		Drawable drawable = getResources().getDrawable(R.drawable.temp_nor_icon);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		setCompoundDrawables(drawable, null, null, null);

		// 制作温度显示颜色数组。
		for (int i = 0; i <= 12; i++) {
			color_step[i] = Color.argb(alfa, 0, i * 20, 240);
		}
		for (int i = 13; i <= 24; i++) {
			color_step[i] = Color.argb(alfa, 0, 240, 480 - i * 20);
		}
		for (int i = 25; i <= 36; i++) {
			color_step[i] = Color.argb(alfa, (i - 24) * 20, 240, 0);
		}
		for (int i = 37; i <= 48; i++) {
			color_step[i] = Color.argb(alfa, 240, 240 - (i - 36) * 20, 0);
		}
	}

	public void setTemp(int temp) {
		this.temp = temp;

		SpannableString styledText = new SpannableString(windowString + temp);
		styledText.setSpan(
				new TextAppearanceSpan(getContext(), R.style.style0), 0, 4,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		setText(styledText, TextView.BufferType.SPANNABLE);

		Drawable drawable = null;

		PengInfo info = AppContext.context().getCurrPengInfo();
		
		if (temp >= info.getAlarmTempMax() - 8)
			drawable = getResources().getDrawable(R.drawable.temp_max_icon);
		else if (temp > info.getAlarmTempMin() + 8)
			drawable = getResources().getDrawable(R.drawable.temp_nor_icon);
		else
			drawable = getResources().getDrawable(R.drawable.temp_min_icon);
		

		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		setCompoundDrawables(drawable, null, null, null);

		int tempIndex = Math.round(temp);

		if (tempIndex < 0)
			tempIndex = 0;
		else if (tempIndex >= 48)
			tempIndex = 48;

		setTextColor(color_step[tempIndex]);
	}

	public int getTemp() {
		return temp;
	}

	public void setWindowId(int windowId) {

		StringBuilder mStringBuilder = new StringBuilder("风口");
		mStringBuilder.append(windowId + 1);
		mStringBuilder.append(":\n");

		windowString = mStringBuilder.toString();
	}

}
