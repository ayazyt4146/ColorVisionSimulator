package com.ayazyt4146.colorvisionsimulator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

public class MainActivity extends Activity {
    private final String[] modes = {
        "Normal (X)", "Protanopi", "Protanomali", "Deuteranopi",
        "Deuteranomali", "Tritanopi", "Tritanomali", "Akromatopsi",
        "Akromatomali", "Yeşil", "Kırmızı", "Mavi", "Daha yeşil", "Daha mavi"
    };
    private int selected = 0;
    private Spinner modeSpinner;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        showMenu();
    }

    private int dp(float value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(null, Typeface.BOLD);
        return t;
    }

    private Button button(String label, int color) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(16);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setTypeface(null, Typeface.BOLD);
        b.setBackground(shape(color, 16));
        b.setMinHeight(dp(62));
        return b;
    }

    private void showMenu() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(24), dp(22), dp(24));
        page.setBackgroundColor(Color.rgb(13, 19, 31));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        TextView title = text("RENK KÖRÜ\nSİMÜLATÖRÜ", 30, Color.WHITE, true);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, dp(10), 0, dp(12));
        content.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView subtitle = text("Bir görünüm seç. BAŞLAT'a basınca simülasyon ekranı açılır.", 16, Color.LTGRAY, false);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(dp(8), 0, dp(8), dp(24));
        content.addView(subtitle);

        TextView section = text("SİMÜLASYON TÜRÜ", 17, Color.rgb(116, 210, 170), true);
        section.setPadding(dp(4), dp(8), 0, dp(10));
        content.addView(section);

        modeSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modes) {
            @Override public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTextColor(Color.WHITE);
                v.setTextSize(18);
                v.setPadding(dp(12), dp(8), dp(8), dp(8));
                return v;
            }
            @Override public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getDropDownView(position, convertView, parent);
                v.setTextSize(17);
                v.setPadding(dp(14), dp(12), dp(14), dp(12));
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(adapter);
        modeSpinner.setSelection(selected);
        modeSpinner.setBackground(shape(Color.rgb(39, 51, 70), 14));
        modeSpinner.setPadding(dp(8), dp(6), dp(8), dp(6));
        content.addView(modeSpinner, new LinearLayout.LayoutParams(-1, dp(64)));

        TextView info = text("Protanopi • Protanomali • Deuteranopi • Deuteranomali • Tritanopi • Tritanomali • Akromatopsi ve renk ayarları", 14, Color.LTGRAY, false);
        info.setPadding(dp(6), dp(14), dp(6), dp(20));
        content.addView(info);

        LinearLayout sample = new LinearLayout(this);
        sample.setOrientation(LinearLayout.VERTICAL);
        sample.setPadding(dp(16), dp(16), dp(16), dp(16));
        sample.setBackground(shape(Color.rgb(27, 37, 54), 18));
        TextView sampleTitle = text("ÖNİZLEME ALANI", 17, Color.WHITE, true);
        sample.addView(sampleTitle);
        TextView sampleDesc = text("Seçtiğin filtre, BAŞLAT ekranındaki renk örneklerine uygulanır.", 14, Color.LTGRAY, false);
        sampleDesc.setPadding(0, dp(6), 0, dp(12));
        sample.addView(sampleDesc);
        LinearLayout swatches = new LinearLayout(this);
        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
        for (int c : colors) {
            View chip = new View(this);
            chip.setBackground(shape(c, 9));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(0, dp(38), 1);
            cp.setMargins(dp(3), 0, dp(3), 0);
            swatches.addView(chip, cp);
        }
        sample.addView(swatches);
        content.addView(sample);

        Button start = button("BAŞLAT  ▶", Color.rgb(25, 150, 103));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(66));
        bp.setMargins(0, dp(22), 0, dp(10));
        content.addView(start, bp);
        start.setOnClickListener(v -> {
            selected = modeSpinner.getSelectedItemPosition();
            showSimulation();
        });

        TextView limitation = text("ÖNEMLİ: Bu sürüm uygulama içi simülasyon ekranıdır. Android, normal bir uygulamanın diğer oyunların ve uygulamaların görüntüsünü doğrudan değiştirmesine izin vermez. Sistem geneli renk düzeltme cihazın kendi ayarlarıyla ayrıca yönetilir.", 12, Color.rgb(190, 199, 211), false);
        limitation.setPadding(dp(6), dp(8), dp(6), dp(12));
        content.addView(limitation);

        scroll.addView(content);
        page.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(page);
    }

    private void showSimulation() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(18), dp(20), dp(18), dp(18));
        page.setBackgroundColor(Color.rgb(10, 15, 25));

        TextView heading = text("SİMÜLASYON AKTİF", 24, Color.WHITE, true);
        heading.setGravity(Gravity.CENTER);
        page.addView(heading);

        TextView selectedMode = text("Seçilen görünüm: " + modes[selected], 16, Color.rgb(116, 210, 170), true);
        selectedMode.setGravity(Gravity.CENTER);
        selectedMode.setPadding(0, dp(8), 0, dp(18));
        page.addView(selectedMode);

        SimulationView simulation = new SimulationView();
        page.addView(simulation, new LinearLayout.LayoutParams(-1, 0, 1));

        TextView explanation = text("Bu ekran yalnızca bu uygulamanın içeriğini simüle eder. Başka uygulamaların veya oyunların renklerini değiştirmez.", 13, Color.LTGRAY, false);
        explanation.setGravity(Gravity.CENTER);
        explanation.setPadding(dp(8), dp(12), dp(8), dp(12));
        page.addView(explanation);

        Button cancel = button("İPTAL  ·  MENÜYE DÖN", Color.rgb(180, 65, 72));
        page.addView(cancel, new LinearLayout.LayoutParams(-1, dp(64)));
        cancel.setOnClickListener(v -> {
            selected = 0;
            showMenu();
        });
        setContentView(page);
    }

    @Override public void onBackPressed() {
        showMenu();
    }

    private class SimulationView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final String[] labels = {"KIRMIZI", "YEŞİL", "MAVİ", "SARI", "CYAN", "MAGENTA", "TURUNCU", "BEYAZ"};
        private final int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.rgb(255,128,0), Color.WHITE};
        SimulationView() { super(MainActivity.this); setLayerType(View.LAYER_TYPE_SOFTWARE, null); }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.rgb(19, 28, 43));
            float gap = dp(12);
            float cellW = (getWidth() - gap * 3) / 2f;
            float cellH = Math.min(dp(112), (getHeight() - gap * 5) / 4f);
            paint.setTextSize(dp(14));
            paint.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
            for (int i = 0; i < colors.length; i++) {
                int col = i % 2, row = i / 2;
                float left = gap + col * (cellW + gap);
                float top = gap + row * (cellH + gap);
                paint.setColor(colors[i]);
                paint.setColorFilter(filterFor(selected));
                canvas.drawRoundRect(left, top, left + cellW, top + cellH, dp(14), dp(14), paint);
                paint.setColorFilter(null);
                paint.setColor(Color.BLACK);
                canvas.drawText(labels[i], left + dp(12), top + cellH / 2f + dp(5), paint);
            }
        }

        private ColorFilter filterFor(int mode) {
            float[] gray = {.299f,.587f,.114f,0,0, .299f,.587f,.114f,0,0, .299f,.587f,.114f,0,0, 0,0,0,1,0};
            float[] matrix;
            switch (mode) {
                case 7: matrix = gray; break;
                case 8: matrix = new float[]{.65f,.25f,.10f,0,0, .25f,.65f,.10f,0,0, .10f,.25f,.65f,0,0, 0,0,0,1,0}; break;
                case 1: case 2: matrix = new float[]{.567f,.433f,0,0,0, .558f,.442f,0,0,0, 0,.242f,.758f,0,0, 0,0,0,1,0}; break;
                case 3: case 4: matrix = new float[]{.625f,.375f,0,0,0, .7f,.3f,0,0,0, 0,.3f,.7f,0,0, 0,0,0,1,0}; break;
                case 5: case 6: matrix = new float[]{.95f,.05f,0,0,0, 0,.433f,.567f,0,0, 0,.475f,.525f,0,0, 0,0,0,1,0}; break;
                case 9: matrix = new float[]{.8f,.1f,.1f,0,0, .1f,1.1f,.1f,0,0, .1f,.1f,.8f,0,0, 0,0,0,1,0}; break;
                case 10: matrix = new float[]{1.15f,-.1f,-.05f,0,0, -.05f,.9f,.05f,0,0, -.05f,-.1f,1.15f,0,0, 0,0,0,1,0}; break;
                case 11: matrix = new float[]{.85f,.05f,.1f,0,0, .05f,.85f,.1f,0,0, .1f,.05f,1.15f,0,0, 0,0,0,1,0}; break;
                case 12: matrix = new float[]{.9f,.05f,.05f,0,0, .05f,1.2f,.05f,0,0, .05f,.05f,.9f,0,0, 0,0,0,1,0}; break;
                case 13: matrix = new float[]{.9f,.05f,.05f,0,0, .05f,.9f,.05f,0,0, .05f,.05f,1.2f,0,0, 0,0,0,1,0}; break;
                default: return null;
            }
            return new ColorMatrixColorFilter(matrix);
        }
    }
}
