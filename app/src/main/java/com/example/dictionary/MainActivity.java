package com.example.dictionary;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    MenuItem menuSetting;
    Toolbar toolbar;

    DBHelper dbHelper;
    ViJpHelper viJpHelper;

    DictionaryFragment dictionaryFragment;
    ViJpDictionaryFragment viJpDictionaryFragment;

    BookmarkFragment bookmarkFragment;
    HistoryFragment historyFragment;
    TranslateFragment translateFragment;
    SentencesFragment sentencesFragment;
    EditText edit_search;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            dbHelper = new DBHelper(this);
            viJpHelper = new ViJpHelper(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dictionaryFragment = new DictionaryFragment();

        viJpDictionaryFragment = new ViJpDictionaryFragment();

        bookmarkFragment = BookmarkFragment.getNewInstance(dbHelper);
        translateFragment = TranslateFragment.getNewInstance();
        historyFragment = HistoryFragment.newInstance(dbHelper);
        sentencesFragment = SentencesFragment.getNewInstance(dbHelper);
        goToFragment(dictionaryFragment, true);

        //click vao listview hien nghia
        dictionaryFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            void onItemClick(String value) {
                Word word = dbHelper.getWord(value);
                dbHelper.addHistory(word);                              //them tu vao danh sach lich su
                goToFragment(DetailFragment.getNewInstance(value, dbHelper), false);
            }
        });

        //click item in viet anh
        viJpDictionaryFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            void onItemClick(String value) {
                Word word = viJpHelper.getWord(value);
                System.out.println("Word-------------------- " + word);
                //vietEngHelper.addHistory(word);
                goToFragment(DetailFragmentViJp.getNewInstance(value, viJpHelper), false);
            }
        });

        bookmarkFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            void onItemClick(String value) {
                goToFragment(DetailFragment.getNewInstance(value, dbHelper), false);
            }
        });

        historyFragment.setOnFragmentListener(new FragmentListener(){
            @Override
            void onItemClick(String value) {
                goToFragment(DetailFragment.getNewInstance(value, dbHelper), false);
            }
        });

        sentencesFragment.setOnFragmentListener(new FragmentListener(){
            @Override
            void onItemClick(String value) {
                goToFragment(SentenceDetailFragment.getNewInstance(value, dbHelper), false);
            }
        });

        edit_search = findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                goToFragment(dictionaryFragment, true);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //menuSetting = menu.findItem((R.id.action_settings));
        ArrayList<String> source = dbHelper.getWord();
        dictionaryFragment.resetDataSource(source);

        ArrayList<String> sourceVietEng = viJpHelper.getWord();      //all word
        viJpDictionaryFragment.resetDataSource(sourceVietEng);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_bookmark) {
            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if (!activeFragment.equals(BookmarkFragment.class.getSimpleName())) {
                goToFragment(bookmarkFragment, false);
            }
        }

        if (id == R.id.nav_history){
            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if (!activeFragment.equals(HistoryFragment.class.getSimpleName())) {
                goToFragment(historyFragment, false);
            }
        }

        //click vào item nav_translate trên menu
        if (id == R.id.nav_translate){
            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if (!activeFragment.equals(DictionaryFragment.class.getSimpleName())) {
                goToFragment(dictionaryFragment, false);
            }
        }

        //Vietnamese to English
        if (id == R.id.nav_translate_vi_en){
            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if (!activeFragment.equals(ViJpDictionaryFragment.class.getSimpleName())) {
                goToFragment(viJpDictionaryFragment, false);
            }
        }

//        //Vietnamese to Japanese
//        if (id == R.id.nav_translate_vi_jp){
//            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
//            if (!activeFragment.equals(ViJpDictionaryFragment.class.getSimpleName())) {
//                goToFragment(viJpDictionaryFragment, false);
//            }
//        }

        if (id == R.id.nav_voice) {
            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if (!activeFragment.equals(TranslateFragment.class.getSimpleName())) {
                goToFragment(translateFragment, false);
            }

        }

        if (id == R.id.nav_sentence) {
            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if (!activeFragment.equals(TranslateFragment.class.getSimpleName())) {
                goToFragment(sentencesFragment, false);
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer((GravityCompat.START));
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
        if (activeFragment.equals(BookmarkFragment.class.getSimpleName())) {
            //menuSetting.setVisible(false);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
            toolbar.setTitle(this.getTitle());  //Bookmark
        } else if(activeFragment.equals(HistoryFragment.class.getSimpleName())) {
            //menuSetting.setVisible(false);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
            toolbar.setTitle(this.getTitle());    //History
        }
        else if(activeFragment.equals(SentencesFragment.class.getSimpleName())){
            //menuSetting.setVisible(true);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
            toolbar.setTitle(this.getTitle());      //Sentences
        }
        else {
            //menuSetting.setVisible(true);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.VISIBLE);
            toolbar.setTitle("");
        }
        return true;
    }
}
