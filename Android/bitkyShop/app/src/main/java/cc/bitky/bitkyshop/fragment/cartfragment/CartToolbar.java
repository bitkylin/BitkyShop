package cc.bitky.bitkyshop.fragment.cartfragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import cc.bitky.bitkyshop.R;

public class CartToolbar extends Toolbar {

  private TextView textViewTitle;
  private TextView textViewEdit;
  OnTextViewEditClickListener textViewEditClickListener;

  public CartToolbar(Context context) {
    this(context, null);
  }

  public CartToolbar(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CartToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.cartfragment_toolbar, this, true);
    textViewTitle = (TextView) view.findViewById(R.id.cartFragment_toolbar_title);
    textViewEdit = (TextView) view.findViewById(R.id.cartFragment_toolbar_edit);
    textViewEdit.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (textViewEdit.getText().toString().equals("编辑")) {
          textViewEdit.setText("完成");
          if (textViewEditClickListener != null) {
            textViewEditClickListener.OnClick(TextViewEditType.edit);
          }
        } else {
          textViewEdit.setText("编辑");
          if (textViewEditClickListener != null) {
            textViewEditClickListener.OnClick(TextViewEditType.completed);
          }
        }
      }
    });
  }

  void setTextViewEditClickListener(OnTextViewEditClickListener textViewEditClickListener) {
    this.textViewEditClickListener = textViewEditClickListener;
  }

  public void setTextViewTitle(String msg) {
    textViewTitle.setText(msg);
  }

  enum TextViewEditType {
    edit,
    completed
  }

  interface OnTextViewEditClickListener {
    void OnClick(TextViewEditType type);
  }
}
