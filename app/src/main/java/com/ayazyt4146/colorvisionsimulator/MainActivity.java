package com.ayazyt4146.colorvisionsimulator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {
    private final String[] modes = {
        "Normal (X)", "Protanopi", "Protanomali", "Deuteranopi",
        "Deuteranomali", "Tritanopi", "Tritanomali", "Akromatopsi",
        "Akromatomali", "Yeşil", "Kırmızı", "Mavi", "Daha yeşil", "Daha mavi"
    };
    private Preview preview;
    private int selected = 0;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 28, 24, 20);
        root.setBackgroundColor(Color.rgb(16, 20, 30));

        TextView title = new TextView(this);
        title.setText("RENK KÖRÜ SİMÜLATÖRÜ");
        title.setTextColor(Color.WHITE);
        title.setTextSize(23);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 12, 0, 18);
        root.addView(title);

        TextView info = new TextView(this);
        info.setText("Bir görünüm seç ve renklerin nasıl değiştiğini önizle.");
        info.setTextColor(Color.LTGRAY);
        info.setTextSize(14);
        info.setGravity(Gravity.CENTER);
        root.addView(info);

        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modes);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> p) {}
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                selected = pos;
                if (preview != null) { preview.setMode(pos); }
            }
        });
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, 54);
        sp.setMargins(0, 20, 0, 12);
        root.addView(spinner, sp);

        preview = new Preview();
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, 0, 1);
        pp.setMargins(0, 8, 0, 12);
        root.addView(preview, pp);

        Button start = new Button(this);
        start.setText("ÖNİZLEMEYİ BAŞLAT");
        start.setOnClickListener(v -> preview.setMode(selected));
        root.addView(start, new LinearLayout.LayoutParams(-1, 52));

        Button cancel = new Button(this);
        cancel.setText("İPTAL / NORMAL RENKLER");
        cancel.setOnClickListener(v -> { spinner.setSelection(0); preview.setMode(0); });
        root.addView(cancel, new LinearLayout.LayoutParams(-1, 52));

        TextView note = new TextView(this);
        note.setText("Not: Bu sürüm renk filtresini yalnızca uygulama içindeki önizlemeye uygular. Telefonun diğer uygulamalarını değiştirmez.");
        note.setTextColor(Color.LTGRAY);
        note.setTextSize(12);
        note.setGravity(Gravity.CENTER);
        note.setPadding(4, 12, 4, 0);
        root.addView(note);

        setContentView(root);
    }

    private class Preview extends View {
        Paint paint = new Paint(3);
        int mode = 0;
        Preview() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE, null); }
        void setMode(int m) { mode = m; invalidate(); }
        @Override protected void onDraw(Canvas c) {
            super.onDraw(c);
            float w = getWidth(), h = getHeight();
            c.drawColor(Color.rgb(29, 36, 50));
            paint.setColor(Color.WHITE); paint.setTextSize(19);
            c.drawText("RENK TESTİ", 18, 32, paint);
            int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.rgb(255,128,0), Color.WHITE};
            float gap = 10, cell = (w - 3*gap)/4;
            for (int i=0;i<colors.length;i++) {
                int x=i%4, y=i/4;
                paint.setColor(colors[i]);
                paint.setColorFilter(filter(mode));
                float l=gap+x*(cell+gap), t=55+y*(cell+gap);
                c.drawRoundRect(l,t,l+cell,t+cell,12,12,paint);
            }
            paint.setColorFilter(null);
            paint.setColor(Color.WHITE); paint.setTextSize(14);
            c.drawText("Seçili: " + modes[mode], 18, Math.min(h-20, 55+2*(cell+gap)+32), paint);
        }
        private ColorFilter filter(int m) {
            float[] gray = {0.299f,0.587f,0.114f,0,0, 0.299f,0.587f,0.114f,0,0, 0.299f,0.587f,0.114f,0,0, 0,0,0,1,0};
            float[] matrix;
            switch(m) {
                case 7: matrix=gray; break;
                case 8: matrix=new float[]{.65f,.25f,.10f,0,0, .25f,.65f,.10f,0,0, .10f,.25f,.65f,0,0, 0,0,0,1,0}; break;
                case 1: case 2: matrix=new float[]{.567f,.433f,0,0,0, .558f,.442f,0,0,0, 0,.242f,.758f,0,0, 0,0,0,1,0}; break;
                case 3: case 4: matrix=new float[]{.625f,.375f,0,0,0, .7f,.3f,0,0,0, 0,.3f,.7f,0,0, 0,0,0,1,0}; break;
                case 5: case 6: matrix=new float[]{.95f,.05f,0,0,0, 0,.433f,.567f,0,0, 0,.475f,.525f,0,0, 0,0,0,1,0}; break;
                case 9: matrix=new float[]{.8f,.1f,.1f,0,0, .1f,1.1f,.1f,0,0, .1f,.1f,.8f,0,0, 0,0,0,1,0}; break;
                case 10: matrix=new float[]{1.15f,-.1f,-.05f,0,0, -.05f,.9f,.05f,0,0, -.05f,-.1f,1.15f,0,0, 0,0,0,1,0}; break;
                case 11: matrix=new float[]{.85f,.05f,.1f,0,0, .05f,.85f,.1f,0,0, .1f,.05f,1.15f,0,0, 0,0,0,1,0}; break;
                case 12: matrix=new float[]{.9f,.05f,.05f,0,0, .05f,1.2f,.05f,0,0, .05f,.05f,.9f,0,0, 0,0,0,1,0}; break;
                case 13: matrix=new float[]{.9f,.05f,.05f,0,0, .05f,.9f,.05f,0,0, .05f,.05f,1.2f,0,0, 0,0,0,1,0}; break;
                default: return null;
            }
            return new ColorMatrixColorFilter(matrix);
        }
    }
}
