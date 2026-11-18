package com.ayazyt4146.colorvisionsimulator;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingMenuService extends Service {
    private WindowManager windowManager;
    private View panel;
    private WindowManager.LayoutParams params;
    private boolean english;
    private final String[] tr = {"Normal","Protanopi","Protanomali","Deuteranopi","Deuteranomali","Tritanopi","Tritanomali","Akromatopsi","Akromatomali"};
    private final String[] en = {"Normal","Protanopia","Protanomaly","Deuteranopia","Deuteranomaly","Tritanopia","Tritanomaly","Achromatopsia","Achromatomaly"};

    @Override public void onCreate() {
        super.onCreate();
        english = getSharedPreferences("app_settings", MODE_PRIVATE).getBoolean("english", false);
        if (!Settings.canDrawOverlays(this)) { stopSelf(); return; }
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        buildPanel();
    }

    private GradientDrawable bg(int color) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(24f); return d;
    }

    private TextView label(String value, int size) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextColor(Color.WHITE); t.setTextSize(size);
        t.setTypeface(null, Typeface.BOLD); t.setGravity(Gravity.CENTER);
        return t;
    }

    private void buildPanel() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(14, 12, 14, 12);
        root.setBackground(bg(Color.rgb(20, 29, 45)));

        TextView header = label("COLOR VISION  ·  " + (english ? "MENU" : "MENÜ"), 15);
        header.setPadding(8, 10, 8, 10);
        header.setBackground(bg(Color.rgb(58, 82, 130)));
        root.addView(header, new LinearLayout.LayoutParams(-1, -2));
        header.setOnTouchListener(new View.OnTouchListener() {
            private int x, y, startX, startY;
            @Override public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    x = params.x; y = params.y; startX = (int)e.getRawX(); startY = (int)e.getRawY(); return true;
                }
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = x + startX - (int)e.getRawX();
                    params.y = y + (int)e.getRawY() - startY;
                    windowManager.updateViewLayout(panel, params); return true;
                }
                return true;
            }
        });

        ScrollView scroll = new ScrollView(this);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        String[] names = english ? en : tr;
        for (int i = 1; i < names.length; i++) {
            final int mode = i;
            Button b = new Button(this);
            b.setText(names[i]); b.setTextSize(13); b.setAllCaps(false);
            b.setTextColor(Color.WHITE); b.setBackground(bg(Color.rgb(48, 62, 83)));
            LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, 48);
            bp.setMargins(0, 5, 0, 5);
            list.addView(b, bp);
            b.setOnClickListener(v -> applyMode(mode));
        }
        scroll.addView(list);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 330));

        Button off = new Button(this);
        off.setText(english ? "TURN FILTER OFF" : "FİLTREYİ KAPAT");
        off.setAllCaps(false); off.setTextColor(Color.WHITE);
        off.setBackground(bg(Color.rgb(160, 58, 68)));
        root.addView(off, new LinearLayout.LayoutParams(-1, 48));
        off.setOnClickListener(v -> {
            if (hasSecurePermission()) {
                Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer_enabled", 0);
                Toast.makeText(this, english ? "System filter off" : "Sistem filtresi kapatıldı", Toast.LENGTH_SHORT).show();
            }
        });

        Button close = new Button(this);
        close.setText(english ? "CLOSE MENU" : "MENÜYÜ KAPAT");
        close.setAllCaps(false); close.setTextColor(Color.WHITE);
        close.setBackground(bg(Color.rgb(78, 91, 111)));
        root.addView(close, new LinearLayout.LayoutParams(-1, 44));
        close.setOnClickListener(v -> stopSelf());

        panel = root;
        int type = Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
        params = new WindowManager.LayoutParams(300, WindowManager.LayoutParams.WRAP_CONTENT, type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        params.x = 18; params.y = 140;
        windowManager.addView(panel, params);
    }

    private boolean hasSecurePermission() {
        if (checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS") == android.content.pm.PackageManager.PERMISSION_GRANTED) return true;
        Toast.makeText(this, english ? "Grant the one-time secure settings permission first." : "Önce tek seferlik güvenli ayarlar iznini ver.", Toast.LENGTH_LONG).show();
        return false;
    }

    private void applyMode(int selection) {
        if (!hasSecurePermission()) return;
        int mode;
        switch (selection) {
            case 1: case 2: mode = 11; break;
            case 3: case 4: mode = 12; break;
            case 5: case 6: mode = 13; break;
            case 7: mode = 0; break;
            default:
                Toast.makeText(this, english ? "This mode is preview-only on Android." : "Bu mod Android'de yalnızca önizlemedir.", Toast.LENGTH_SHORT).show();
                return;
        }
        boolean a = Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer", mode);
        boolean b = Settings.Secure.putInt(getContentResolver(), "accessibility_display_daltonizer_enabled", 1);
        Toast.makeText(this, a && b ? (english ? "Mode applied" : "Mod uygulandı") : (english ? "Device does not support this filter" : "Cihaz bu filtreyi desteklemiyor"), Toast.LENGTH_SHORT).show();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) { return START_NOT_STICKY; }
    @Override public void onDestroy() {
        if (windowManager != null && panel != null) try { windowManager.removeView(panel); } catch (Exception ignored) {}
        panel = null; super.onDestroy();
    }
    @Override public IBinder onBind(Intent intent) { return null; }
}
