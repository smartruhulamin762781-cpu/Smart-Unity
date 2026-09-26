package com.smartunity.ads;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.net.*;
import android.view.*;
import android.widget.*;
import com.unity3d.ads.*;
import com.unity3d.services.banners.*;

public class MainActivity extends Activity implements IUnityAdsInitializationListener {
  SharedPreferences p; LinearLayout root; boolean ready=false; BannerView banner;
  int loaded,shown,completed,clicks,errors;
  String gid(){return p.getString("game","");} String bid(){return p.getString("banner","");}
  String iid(){return p.getString("inter","");} String rid(){return p.getString("reward","");}
  boolean test(){return p.getBoolean("test",true);}
  int blue=Color.rgb(20,124,245), bg=Color.rgb(3,21,45), cyan=Color.rgb(0,191,255), green=Color.rgb(24,216,121);

  public void onCreate(Bundle b){super.onCreate(b);p=getSharedPreferences("cfg",0);home();if(!gid().isEmpty())init();}
  TextView t(String s,int z,int c,boolean bold){TextView v=new TextView(this);v.setText(s);v.setTextColor(c);v.setTextSize(z);v.setPadding(12,7,12,7);if(bold)v.setTypeface(null,1);return v;}
  Button btn(String s){Button b=new Button(this);b.setText(s);b.setTextColor(Color.WHITE);b.setTextSize(13);b.setAllCaps(false);return b;}
  LinearLayout box(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(10,8,10,8);l.setBackgroundColor(Color.rgb(6,36,71));LinearLayout.LayoutParams q=new LinearLayout.LayoutParams(-1,-2);q.setMargins(0,0,0,10);l.setLayoutParams(q);return l;}
  void base(){ScrollView s=new ScrollView(this);root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(12,10,12,25);root.setBackgroundColor(bg);s.addView(root);setContentView(s);}
  void home(){base();root.addView(t("◈  Smart Unity",25,Color.WHITE,true));root.addView(t("Unity Ads Manager & Monitor\nManage • Monitor • Earn",13,Color.LTGRAY,false));
    LinearLayout a=box();a.addView(t("✓  Unity Ads",17,Color.WHITE,true));a.addView(t(ready?"Connected • Ready":gid().isEmpty()?"Not configured":"Connecting…",13,ready?green:Color.YELLOW,false));root.addView(a);
    LinearLayout x=box();x.addView(t("Active Game ID\n"+(gid().isEmpty()?"Not set":gid()),13,Color.WHITE,false));x.addView(t("Ad Type\nBanner • Interstitial • Rewarded",13,Color.LTGRAY,false));x.addView(t("Mode\n"+(test()?"TEST ADS":"LIVE ADS"),13,test()?Color.YELLOW:green,false));root.addView(x);
    LinearLayout st=box();st.addView(t("Ad Statistics",15,cyan,true));st.addView(t("Loaded  "+loaded+"    Shown  "+shown+"    Completed  "+completed+"\nClicks  "+clicks+"    Errors  "+errors,13,Color.WHITE,false));root.addView(st);
    LinearLayout n=box();n.addView(t("Network",15,cyan,true));n.addView(t(net(),13,Color.WHITE,false));root.addView(n);
    Button cfg=btn("⚙  Configure Ads");Button ba=btn("▣  Show Banner");Button ii=btn("▣  Show Interstitial");Button rr=btn("🎁  Show Rewarded");Button ss=btn("▥  Ad Statistics");Button nn=btn("⌁  VPN & Network Details");Button pp=btn("🛡  Privacy & Consent");
    ba.setEnabled(ready&&!bid().isEmpty());ii.setEnabled(ready&&!iid().isEmpty());rr.setEnabled(ready&&!rid().isEmpty());
    root.addView(cfg);root.addView(ba);root.addView(ii);root.addView(rr);root.addView(ss);root.addView(nn);root.addView(pp);
    cfg.setOnClickListener(v->settings());ba.setOnClickListener(v->showBanner());ii.setOnClickListener(v->show(iid(),false));rr.setOnClickListener(v->show(rid(),true));ss.setOnClickListener(v->stats());nn.setOnClickListener(v->network());pp.setOnClickListener(v->privacy());
  }
  void settings(){base();root.addView(t("‹  Game ID & Ad Unit Settings",21,Color.WHITE,true));root.addView(t("Use your own Unity Monetization IDs. Test mode is ON by default.",13,Color.LTGRAY,false));
    EditText g=e("Unity Game ID",gid()),b=e("Banner Placement ID",bid()),i=e("Interstitial Placement ID",iid()),r=e("Rewarded Placement ID",rid());root.addView(g);root.addView(b);root.addView(i);root.addView(r);
    Switch sw=new Switch(this);sw.setText("Test mode (recommended)");sw.setTextColor(Color.WHITE);sw.setChecked(test());root.addView(sw);Button save=btn("Save & Initialize Unity Ads");root.addView(save);
    save.setOnClickListener(v->{if(g.getText().toString().trim().isEmpty()){toast("Game ID required");return;}p.edit().putString("game",g.getText().toString().trim()).putString("banner",b.getText().toString().trim()).putString("inter",i.getText().toString().trim()).putString("reward",r.getText().toString().trim()).putBoolean("test",sw.isChecked()).apply();ready=false;init();home();});
  }
  EditText e(String h,String v){EditText e=new EditText(this);e.setHint(h);e.setText(v);e.setTextColor(Color.WHITE);e.setHintTextColor(Color.GRAY);e.setSingleLine();return e;}
  void stats(){base();root.addView(t("‹  Ad Statistics",21,Color.WHITE,true));LinearLayout x=box();x.addView(t("Today • Local session",15,cyan,true));x.addView(t("Loaded     "+loaded+"\nShown      "+shown+"\nCompleted  "+completed+"\nClicks     "+clicks+"\nErrors     "+errors,14,Color.WHITE,false));root.addView(x);Button b=btn("↻ Reset Statistics");root.addView(b);b.setOnClickListener(v->{loaded=shown=completed=clicks=errors=0;stats();});}
  void network(){base();root.addView(t("‹  VPN & Network Details",21,Color.WHITE,true));root.addView(t(net(),14,Color.WHITE,false));Button b=btn("↻ Refresh Public IP / Country");root.addView(b);TextView out=t("",14,Color.WHITE,false);root.addView(out);b.setOnClickListener(v->{out.setText("Public IP lookup is optional. Use your preferred IP geolocation provider in production.");});}
  void privacy(){new AlertDialog.Builder(this).setTitle("Privacy & Consent").setMessage("No GPS permission is requested. Unity Ads may process advertising/device identifiers and network information. Configure consent/CMP and publish your own privacy policy for your target regions.").setPositiveButton("OK",null).show();}
  String net(){ConnectivityManager c=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);Network n=c.getActiveNetwork();if(n==null)return "Disconnected";NetworkCapabilities q=c.getNetworkCapabilities(n);return "Internet: "+(q!=null&&q.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)+"\nVPN: "+(q!=null&&q.hasTransport(NetworkCapabilities.TRANSPORT_VPN));}
  void init(){try{UnityAds.initialize(getApplicationContext(),gid(),test(),this);}catch(Exception e){toast(e.getMessage());}}
  public void onInitializationComplete(){ready=true;home();}
  public void onInitializationFailed(UnityAds.UnityAdsInitializationError e,String m){errors++;toast("Unity Ads init failed: "+m);home();}
  void showBanner(){if(banner!=null)return;banner=new BannerView(this,bid(),new UnityBannerSize(320,50));banner.setListener(new BannerView.IListener(){public void onBannerLoaded(BannerView v){loaded++;}public void onBannerFailedToLoad(BannerView v,BannerErrorInfo i){errors++;}public void onBannerClick(BannerView v){clicks++;}public void onBannerLeftApplication(BannerView v){}});banner.load();}
  void show(String id,boolean reward){if(id.isEmpty())return;UnityAds.load(id,new IUnityAdsLoadListener(){public void onUnityAdsAdLoaded(String p){loaded++;UnityAds.show(MainActivity.this,p,new UnityAdsShowOptions(),new IUnityAdsShowListener(){public void onUnityAdsShowFailure(String p,UnityAds.UnityAdsShowError e,String m){errors++;}public void onUnityAdsShowStart(String p){shown++;}public void onUnityAdsShowClick(String p){clicks++;}public void onUnityAdsShowComplete(String p,UnityAds.UnityAdsShowCompletionState s){if(reward&&s==UnityAds.UnityAdsShowCompletionState.COMPLETED)completed++;}});}public void onUnityAdsFailedToLoad(String p,UnityAds.UnityAdsLoadError e,String m){errors++;}});}
  void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
  protected void onDestroy(){if(banner!=null)banner.destroy();super.onDestroy();}
}