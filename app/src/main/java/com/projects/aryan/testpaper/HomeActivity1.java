package com.projects.aryan.testpaper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
import com.kaopiz.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity1 extends AppCompatActivity {

    WebView webView;
    KProgressHUD hud;
    ResponseModel1 responseModel1;
    private DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        callApiForDrawerItem();

//        setViewForNavigationDrawer();


        webView = findViewById(R.id.webview);
        webView.clearCache(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //Required functionality here
                return super.onJsAlert(view, url, message, result);
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setSupportZoom(true);

        webView.setWebViewClient(new MyClient(webView));

        callRetrofitForWebViewApi();
    }

    private void callApiForDrawerItem() {
        hud.show();
        ApiInterface1 apiInterface1 = ApiClient1.getClient().create(ApiInterface1.class);
        Call<DrawerItemModel> call = apiInterface1.getItemForDrawer();
        call.enqueue(new Callback<DrawerItemModel>() {
            @Override
            public void onResponse(Call<DrawerItemModel> call, Response<DrawerItemModel> response) {

                if (response.body() != null) {
                    DrawerItemModel responseModel = response.body();
                    if (responseModel.getStatus()) {
                        setViewForNavigationDrawer(responseModel);
                    } else {
                        if (hud.isShowing()) {
                            hud.dismiss();
                        }
                        Toast.makeText(HomeActivity1.this, responseModel1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                    Toast.makeText(HomeActivity1.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DrawerItemModel> call, Throwable t) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
            }
        });
    }

    private void setViewForNavigationDrawer(final DrawerItemModel responseModel) {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        addMenuItemInNavMenuDrawer(responseModel, navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                for (int i = 0; i < responseModel.getList().size(); i++) {
                    if (menuItem.getItemId() == Integer.parseInt(responseModel.getList().get(i).getId())) {
                        setViewForWeb(responseModel.getList().get(i).getUrl());
                        toolbar.setTitle(responseModel.getList().get(i).getPageName());
                    }
                }
                return false;
            }
        });
    }

    private void addMenuItemInNavMenuDrawer(DrawerItemModel responseModel, NavigationView navigationView) {
        Menu menu = navigationView.getMenu();

        for (int i = 0; i < responseModel.getList().size(); i++) {

            /* Setting MEnu Item dynamically*/
            menu.add(0, Integer.parseInt(responseModel.getList().get(i).getId()), Menu.NONE, responseModel.getList().get(i).getPageName());

        }
        navigationView.invalidate();
        setViewForWeb(responseModel.getList().get(0).getUrl());


    }

    private void callRetrofitForWebViewApi() {
        ApiInterface1 apiInterface1 = ApiClient1.getClient().create(ApiInterface1.class);
        Call<ResponseModel1> call = apiInterface1.getDynamicApi();
        call.enqueue(new Callback<ResponseModel1>() {
            @Override
            public void onResponse(Call<ResponseModel1> call, Response<ResponseModel1> response) {

                if (response.body() != null) {
                    responseModel1 = response.body();
                    if (responseModel1.getStatus()) {
                        setViewForWeb(responseModel1.getData());
                    } else {
                        if (hud.isShowing()) {
                            hud.dismiss();
                        }
                        Toast.makeText(HomeActivity1.this, responseModel1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (hud.isShowing()) {
                        hud.dismiss();
                    }
                    Toast.makeText(HomeActivity1.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel1> call, Throwable t) {
                if (hud.isShowing()) {
                    hud.dismiss();
                }
            }
        });

    }

    private void setViewForWeb(String data) {
        if (hud.isShowing()) {
            hud.dismiss();
        }

        webView.loadUrl(data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView.canGoBack()) {
//            webView.goBack();
//            return true;
//        } else {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            showExitAlertDialog();
        }

//        }
        return false;
    }

    private void showExitAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HomeActivity1.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
