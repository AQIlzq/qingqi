package com.qingqi.adskip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etKeywords;
    private TextInputEditText etDelay;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tv_status);
        etKeywords = findViewById(R.id.et_keywords);
        etDelay = findViewById(R.id.et_delay);
        MaterialButton btnOpen = findViewById(R.id.btn_open);
        MaterialButton btnSave = findViewById(R.id.btn_save);

        List<String> kws = Prefs.getKeywords(this);
        etKeywords.setText(TextUtils.join(",", kws));
        etDelay.setText(String.valueOf(Prefs.getDelay(this)));

        btnOpen.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        btnSave.setOnClickListener(v -> {
            String raw = etKeywords.getText() == null ? "" : etKeywords.getText().toString();
            Prefs.setKeywords(this, raw);
            String d = etDelay.getText() == null ? "300" : etDelay.getText().toString().trim();
            try {
                Prefs.setDelay(this, Integer.parseInt(d));
            } catch (NumberFormatException e) {
                Prefs.setDelay(this, 300);
            }
            Toast.makeText(this, "设置已保存", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean on = isAccessibilityServiceEnabled(this, SplashSkipService.class);
        tvStatus.setText(on ? R.string.status_on : R.string.status_off);
    }

    private static boolean isAccessibilityServiceEnabled(Context context, Class<?> service) {
        AccessibilityManager am =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am == null) return false;
        List<android.accessibilityservice.AccessibilityServiceInfo> list =
                am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (android.accessibilityservice.AccessibilityServiceInfo info : list) {
            if (info.getResolveInfo().serviceInfo.packageName.equals(context.getPackageName())
                    && info.getResolveInfo().serviceInfo.name.equals(service.getName())) {
                return true;
            }
        }
        return false;
    }
}
