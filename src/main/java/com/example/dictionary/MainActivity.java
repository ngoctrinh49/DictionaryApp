package com.example.dictionary;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{

    MenuItem menuSetting;

    DictionaryFragment dictionaryFragment;
    BookmarkFragment bookmarkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dictionaryFragment = new DictionaryFragment();
        bookmarkFragment = new BookmarkFragment();

        goToFragment(dictionaryFragment, true);
        dictionaryFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                goToFragment(DetialFragment.getNewInstance(message), false);
            }
        });

        bookmarkFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                goToFragment(DetialFragment.getNewInstance(message), false);
            }
        });
        EditText edit_search_var = findViewById(R.id.edit_search);
        edit_search_var.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dictionaryFragment.filterValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuSetting = menu.findItem(R.id.action_settings);

        String id = Global.getState(this, "dic_type");

        if (id != null) {
            //onOptionsItemSelected(menu.findItem(Integer.valueOf(id)));              //BUG(menu.findItem(212221)-> null

            //THAY  THE BANG
            onOptionsItemSelected(menu.findItem(R.id.action_settings));
        } else {
            //DB.getData(R.id.action_eng_viet);
            dictionaryFragment.resetDataSource(DB.getData(R.id.action_eng_viet));
        }

        return true;
    }

    //dùng khi đã có dữ liệu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // xu ly action bar item clicks here. The action bar will automatcaly handle clicks on the
        // Home/Up button, so long as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        //System.out.println("Hello i am bug " + id);

        Global.saveState(this, "dic_type", String.valueOf(id));
        String[] source = DB.getData(id);

        if (id == R.id.action_eng_viet) {
            dictionaryFragment.resetDataSource(source);
            menuSetting.setIcon(getDrawable(R.drawable.english_vietnamese));
        } else if (id == R.id.action_viet_eng) {
            dictionaryFragment.resetDataSource(source);
            menuSetting.setIcon(getDrawable(R.drawable.vietnamese_english));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //xu ly navigation view item khi click here
        int id = item.getItemId();

        //neu an vao nut bookmark
        if (id == R.id.nav_bookmark) {
            goToFragment(bookmarkFragment,false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void goToFragment(Fragment fragment, boolean isTop) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (!isTop) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}