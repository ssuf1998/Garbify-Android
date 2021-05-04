package indi.ssuf1998.garbify;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.bar.OnTitleBarListener;

import indi.ssuf1998.garbify.databinding.ActivityKnowledgeBinding;

public class KnowledgeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final ActivityKnowledgeBinding binding = ActivityKnowledgeBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.knowledgeTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
      @Override
      public void onLeftClick(View view) {
        finish();
      }

      @Override
      public void onTitleClick(View view) {

      }

      @Override
      public void onRightClick(View view) {

      }
    });
  }
}