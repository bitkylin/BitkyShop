package cc.bitky.bitkyshop.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;

public class KyToolBar extends Toolbar {
  private TextView textView;
  private View view;
  private ImageView kyNavigation;
  private ImageView rightButton;

  public KyToolBar(Context context) {
    this(context, null);
  }

  public KyToolBar(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public KyToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    initView();
    final TintTypedArray a =
        TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.KyToolbar,
            defStyleAttr, 0);
    final boolean enableKyNavigationIcon =
        a.getBoolean(R.styleable.KyToolbar_enableKyNavigationIcon, true);
    final Drawable navIcon = a.getDrawable(R.styleable.KyToolbar_KyNavigationIcon);

    setNavigationIcon(navIcon);
    enableKyNavigation(enableKyNavigationIcon);
    a.recycle();
  }

  /**
   * 是否启用kyNavigation
   *
   * @param enableKyNavigationIcon kyNavigation
   */
  private void enableKyNavigation(boolean enableKyNavigationIcon) {
    if (enableKyNavigationIcon) {
      kyNavigation.setVisibility(VISIBLE);
    } else {
      kyNavigation.setVisibility(INVISIBLE);
    }
  }

  @Override public void setNavigationOnClickListener(OnClickListener listener) {
    kyNavigation.setOnClickListener(listener);
  }

  /**
   * 设置navigation的图标
   *
   * @param icon 图标
   */
  @Override public void setNavigationIcon(@Nullable Drawable icon) {
    if (icon != null) {
      kyNavigation.setImageDrawable(icon);
      enableKyNavigation(true);
    }
  }

  private void initView() {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    view = inflater.inflate(R.layout.widget_ky_toolbar, this, true);
    textView = (TextView) view.findViewById(R.id.kytoolbar_title);
    kyNavigation = (ImageView) view.findViewById(R.id.kytoolbar_navigation);
    rightButton = (ImageView) view.findViewById(R.id.kytoolbar_rightButton);
  }

  @Override public void setTitle(CharSequence title) {
    super.setTitle(title);
    if (view == null) initView();
    textView.setText(title);
  }
}

