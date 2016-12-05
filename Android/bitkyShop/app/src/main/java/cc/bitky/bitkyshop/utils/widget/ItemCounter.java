package cc.bitky.bitkyshop.utils.widget;

import android.content.Context;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;

/**
 * 注：需要传入两个监听器
 */
public class ItemCounter extends LinearLayout implements View.OnClickListener {

  private LayoutInflater inflater;
  private Button buttonAdd;
  private Button buttonMinus;
  private TextView textViewShow;

  private int value = 1;
  private int valueMax = 99;
  private int valueMin = 1;

  OnCounterOverflowListener overflowListener;
  OnCounterClickListener counterClickListener;

  public ItemCounter(Context context) {
    this(context, null);
  }

  public ItemCounter(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ItemCounter(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.widget_item_counter, this, true);

    buttonAdd = (Button) view.findViewById(R.id.btnAdd_widget_itemCounter);
    buttonMinus = (Button) view.findViewById(R.id.btnMinus_widget_itemCounter);
    textViewShow = (TextView) view.findViewById(R.id.textViewShow_widget_itemCounter);
    textViewShow.setText(value + "");
    buttonAdd.setOnClickListener(this);
    buttonMinus.setOnClickListener(this);

    //if (attrs != null) {
    //  TintTypedArray typedArray =
    //      TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.ItemCounter,
    //          defStyleAttr, 0);
    //
    //  typedArray.recycle();
    //}
  }

  public void setOnCounterOverflowListener(OnCounterOverflowListener overflowListener) {
    this.overflowListener = overflowListener;
  }

  public void setOnCounterClickListener(OnCounterClickListener counterClickListener) {
    this.counterClickListener = counterClickListener;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
    textViewShow.setText(value + "");
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnAdd_widget_itemCounter:
        if (value < valueMax) {
          value++;
          textViewShow.setText(value + "");
          if (counterClickListener != null) {
            counterClickListener.OnButtonClick(value, ButtonType.add);
          }
        } else {
          if (overflowListener != null) overflowListener.addOverflow();
        }
        break;

      case R.id.btnMinus_widget_itemCounter:
        if (value > valueMin) {
          value--;
          textViewShow.setText(value + "");
          if (counterClickListener != null) {
            counterClickListener.OnButtonClick(value, ButtonType.minus);
          }
        } else {
          if (overflowListener != null) overflowListener.minusOverflow();
        }
        break;
    }
  }

  public enum ButtonType {
    add,
    minus
  }

  public interface OnCounterClickListener {

    void OnButtonClick(int currentCount, ButtonType type);
  }

  public interface OnCounterOverflowListener {

    void addOverflow();

    void minusOverflow();
  }
}
