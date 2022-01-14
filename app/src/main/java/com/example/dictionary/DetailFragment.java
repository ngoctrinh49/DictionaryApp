package com.example.dictionary;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Locale;

public class DetailFragment extends Fragment {
    private String value = "";
    private TextView textView;
    private ImageButton btnBookmark, btnVolume;
    private WebView webView;
    private DBHelper mDBHelper;
    private TextToSpeech textToSpeech;        //am thanh

    public DetailFragment() {
    }

    public static DetailFragment getNewInstance(String value, DBHelper dbHelper) {
        DetailFragment fragment = new DetailFragment();
        fragment.value = value;
        fragment.mDBHelper = dbHelper;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.tvWord);
        webView = view.findViewById(R.id.tvWordTranslate);
        btnBookmark = view.findViewById(R.id.btnBookmark);
        btnVolume = view.findViewById(R.id.btnVolume);

        final Word word = mDBHelper.getWord(value);
        textView.setText(word.key);
        webView.loadDataWithBaseURL(null, word.value, "text/html", "utf-8", null);

        Word bookmarkWord = mDBHelper.getWordFromBookMark(value);
        int isMark = bookmarkWord == null ? 0 : 1;

        btnBookmark.setTag(isMark);

        int icon = bookmarkWord == null ? R.drawable.ic_bookmark_border : R.drawable.ic_bookmark;
        btnBookmark.setImageResource(icon);

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) btnBookmark.getTag();
                if (i == 0) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark);
                    btnBookmark.setTag(1);
                    mDBHelper.addBookMark(word);
                } else if (i == 1) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_border);
                    btnBookmark.setTag(0);
                    mDBHelper.removeBookMark(word);
                }
            }
        });

        //âm thanh
        textToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        btnVolume.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        //Speak btn click
        btnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wordSpeak = textView.getText().toString();
                textToSpeech.speak(wordSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
