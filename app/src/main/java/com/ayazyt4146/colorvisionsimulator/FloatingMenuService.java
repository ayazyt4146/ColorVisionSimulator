package com.ayazyt4146.colorvisionsimulator;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingMenuService extends Service {
 private WindowManager wm; private View panel; private View bubble; private WindowManager.LayoutParams lp; private Spinner spinner;
 private float downX,downY; private int startX,startY;
 private final String[] tr={"Normal","Protanopi","Protanomali","Deuteranopi","Deuteranomali","Tritanopi","Tritanomali","Akromatopsi","Akromatomali"};
 private final String[] en={"Normal","Protanopia","Protanomaly","Deuteranopia","Deuteranomaly","Tritanopia","Tritanomaly","Achromatopsia","Achromatomaly"};
 private int dp(float n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
 private GradientDrawable bg(int c){GradientDrawable d=new GradientDrawable();d.setColor(c);d.setCornerRadius(dp(14));d.setStroke(dp(1),Color.rgb(90,110,145));return d;}
 private TextView txt(String s,int z,int c,boolean b){TextView t=new TextView(this);t.setText(s);t.setTextSize(z);t.setTextColor(c);if(b)t.setTypeface(null,Typeface.BOLD);return t;}
 private Button btn(String s,int c){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextColor(-1);b.setTextSize(14);b.setTypeface(null,Typeface.BOLD);b.setMinHeight(dp(46));b.setBackground(bg(c));return b;}
 @Override public void onCreate(){super.onCreate();if(!Settings.canDrawOverlays(this)){stopSelf();return;}wm=(WindowManager)getSystemService(WINDOW_SERVICE);
 boolean e=getSharedPreferences("app_settings",0).getBoolean("english",false);
 LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setPadding(dp(14),dp(12),dp(14),dp(14));root.setBackground(bg(Color.rgb(17,25,41)));
 TextView h=txt("◈ COLOR VISION · "+(e?"CONTROL":"KONTROL"),16,-1,true);h.setPadding(dp(4),dp(5),dp(4),dp(12));
 h.setOnTouchListener((v,x)->{if(x.getAction()==0){downX=x.getRawX();downY=x.getRawY();startX=lp.x;startY=lp.y;return true;}if(x.getAction()==2){lp.x=startX+(int)(x.getRawX()-downX);lp.y=startY+(int)(x.getRawY()-downY);wm.updateViewLayout(panel,lp);return true;}return x.getAction()==1;});root.addView(h);
 TextView cap=txt(e?"Choose a simulation mode":"Simülasyon türünü seç",13,Color.LTGRAY,false);cap.setPadding(dp(4),0,0,dp(7));root.addView(cap);
 spinner=new Spinner(this);ArrayAdapter<String>a=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,e?en:tr);a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);spinner.setAdapter(a);spinner.setBackground(bg(Color.rgb(42,55,77)));root.addView(spinner,new LinearLayout.LayoutParams(-1,dp(48)));
  TextView modeInfo=txt(e?"Normal colors. The system filter is off.":"Normal renk görünümü. Sistem filtresi kapalıdır.",12,Color.LTGRAY,false);modeInfo.setPadding(dp(5),dp(8),dp(5),dp(5));root.addView(modeInfo);
  String[] trInfo={"Normal renk görünümü. Sistem filtresi kapalıdır.","Protanopi: Kırmızıya duyarlı koni hücreleri yoktur; kırmızı-yeşil ayrımı zorlaşabilir.","Protanomali: Kırmızıya duyarlılık azalmıştır.","Deuteranopi: Yeşile duyarlı koni hücreleri yoktur; kırmızı-yeşil ayrımı zorlaşabilir.","Deuteranomali: Yeşile duyarlılık azalmıştır.","Tritanopi: Maviye duyarlı koni hücreleri yoktur; mavi-sarı ayrımı etkilenebilir.","Tritanomali: Maviye duyarlılık azalmıştır.","Akromatopsi: Renk algısı yoktur veya çok sınırlıdır; gri tonlama yaklaşık simülasyondur.","Akromatomali: Renk algısı azalmıştır; bu mod yaklaşık önizlemedir."};
  String[] enInfo={"Normal color appearance. System filter is off.","Protanopia: red-sensitive cone cells are absent; red-green distinction may be difficult.","Protanomaly: reduced sensitivity to red.","Deuteranopia: green-sensitive cone cells are absent; red-green distinction may be difficult.","Deuteranomaly: reduced sensitivity to green.","Tritanopia: blue-sensitive cone cells are absent; blue-yellow distinction may be affected.","Tritanomaly: reduced sensitivity to blue.","Achromatopsia: color perception is absent or very limited; grayscale is an approximation.","Achromatomaly: color perception is reduced; this mode is an approximate preview."};
  spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onItemSelected(android.widget.AdapterView<?> p,View v,int position,long id){modeInfo.setText((e?enInfo:trInfo)[position]);}public void onNothingSelected(android.widget.AdapterView<?> p){}});
 Button apply=btn(e?"APPLY MODE":"MODU UYGULA",Color.rgb(25,145,101));LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(-1,dp(49));ap.topMargin=dp(10);root.addView(apply,ap);apply.setOnClickListener(v->applyMode(spinner.getSelectedItemPosition()));
 Button reset=btn(e?"RESTORE ORIGINAL COLORS":"ORİJİNAL RENKLERE DÖN",Color.rgb(170,65,75));LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,dp(47));rp.topMargin=dp(7);root.addView(reset,rp);reset.setOnClickListener(v->restore());
 Button minimize=btn(e?"− MINIMIZE":"− KÜÇÜLT",Color.rgb(65,105,165));LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(-1,dp(44));mp.topMargin=dp(7);root.addView(minimize,mp);minimize.setOnClickListener(v->minimizePanel(e));
 Button close=btn(e?"CLOSE MENU":"MENÜYÜ KAPAT",Color.rgb(76,89,111));LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,dp(44));cp.topMargin=dp(7);root.addView(close,cp);close.setOnClickListener(v->stopSelf());
 panel=root;int type=Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE;
 lp=new WindowManager.LayoutParams(dp(290),-2,type,WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT);lp.gravity=Gravity.TOP|Gravity.END;lp.x=dp(14);lp.y=dp(100);try{wm.addView(panel,lp);}catch(Exception ex){stopSelf();}}
 private void minimizePanel(boolean english){
  if(panel==null||bubble!=null)return;
  TextView b=txt("◈",24,Color.WHITE,true);b.setGravity(Gravity.CENTER);b.setBackground(bg(Color.rgb(25,145,101)));
  WindowManager.LayoutParams blp=new WindowManager.LayoutParams(dp(54),dp(54),Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT);blp.gravity=Gravity.TOP|Gravity.END;blp.x=lp.x;blp.y=lp.y;
  b.setOnTouchListener(new View.OnTouchListener(){float sx,sy;int ox,oy;long down;boolean moved;public boolean onTouch(View v,MotionEvent ev){if(ev.getAction()==MotionEvent.ACTION_DOWN){sx=ev.getRawX();sy=ev.getRawY();ox=blp.x;oy=blp.y;down=System.currentTimeMillis();moved=false;return true;}if(ev.getAction()==MotionEvent.ACTION_MOVE){int dx=(int)(ev.getRawX()-sx),dy=(int)(ev.getRawY()-sy);if(Math.abs(dx)+Math.abs(dy)>dp(6))moved=true;if(moved){blp.x=ox-dx;blp.y=oy+dy;wm.updateViewLayout(bubble,blp);}return true;}if(ev.getAction()==MotionEvent.ACTION_UP){if(!moved)restorePanel();return true;}return true;}});
  try{wm.removeView(panel);bubble=b;wm.addView(bubble,blp);}catch(Exception ex){bubble=null;Toast.makeText(this,english?"Could not minimize menu":"Menü küçültülemedi",Toast.LENGTH_SHORT).show();}
 }
 private void restorePanel(){if(bubble!=null){try{wm.removeView(bubble);}catch(Exception ignored){}bubble=null;}if(panel!=null){try{wm.addView(panel,lp);}catch(Exception ex){Toast.makeText(this,"Menü açılamadı.",Toast.LENGTH_SHORT).show();}}}
 private int mode(int i){switch(i){case 1:case 2:return 11;case 3:case 4:return 12;case 5:case 6:return 13;case 7:return 0;default:return -1;}}
 private void applyMode(int i){if(checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS")!=android.content.pm.PackageManager.PERMISSION_GRANTED){Toast.makeText(this,"WRITE_SECURE_SETTINGS izni gerekli.",1).show();return;}
 SharedPreferences p=getSharedPreferences("global_filter_backup",0);if(!p.getBoolean("active",false)){int oe=Settings.Secure.getInt(getContentResolver(),"accessibility_display_daltonizer_enabled",0);int om=Settings.Secure.getInt(getContentResolver(),"accessibility_display_daltonizer",-1);p.edit().putInt("old_enabled",oe).putInt("old_mode",om).putBoolean("active",true).apply();}
 if(i==0)Settings.Secure.putInt(getContentResolver(),"accessibility_display_daltonizer_enabled",0);else{Settings.Secure.putInt(getContentResolver(),"accessibility_display_daltonizer",mode(i));Settings.Secure.putInt(getContentResolver(),"accessibility_display_daltonizer_enabled",1);}Toast.makeText(this,"Mod uygulandı (Android desteğine bağlı).",0).show();}
 private void restore(){SharedPreferences p=getSharedPreferences("global_filter_backup",0);if(!p.getBoolean("active",false)||checkSelfPermission("android.permission.WRITE_SECURE_SETTINGS")!=android.content.pm.PackageManager.PERMISSION_GRANTED)return;Settings.Secure.putInt(getContentResolver(),"accessibility_display_daltonizer",p.getInt("old_mode",-1));Settings.Secure.putInt(getContentResolver(),"accessibility_display_daltonizer_enabled",p.getInt("old_enabled",0));p.edit().putBoolean("active",false).apply();Toast.makeText(this,"Önceki ekran ayarları geri yüklendi.",0).show();}
 @Override public int onStartCommand(Intent i,int f,int id){return START_NOT_STICKY;}
 @Override public void onDestroy(){if(panel!=null&&wm!=null){try{wm.removeView(panel);}catch(Exception ignored){}panel=null;}if(bubble!=null&&wm!=null){try{wm.removeView(bubble);}catch(Exception ignored){}bubble=null;}super.onDestroy();}
 @Override public IBinder onBind(Intent i){return null;}
}