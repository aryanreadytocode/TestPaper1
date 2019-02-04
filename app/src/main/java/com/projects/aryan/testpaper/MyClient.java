package  com.topperstuition.testpaper;

import android.webkit.WebView;
import android.webkit.WebViewClient;

class MyClient extends WebViewClient {
   private WebView mwv;

   public MyClient(WebView v) {
       mwv = v;
   }

   @Override
   public boolean shouldOverrideUrlLoading(WebView view, String url) {
       if(url.equals("www.digitalprolearn.com")) {
           mwv.loadUrl(url);
           return true;
       }
       return false;
   }
}