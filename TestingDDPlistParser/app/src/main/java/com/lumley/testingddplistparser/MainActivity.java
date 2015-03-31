package com.lumley.testingddplistparser;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

import com.dd.plist.BinaryPropertyListParser;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListFormatException;

import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.button_working_plist)
    Button mButtonLoadWorkingPlist;

    @InjectView(R.id.button_failing_plist)
    Button mButtonFailingPlist;

    @InjectView(R.id.text_message)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.button_working_plist)
    public void onLoadWorkingPlistPressed() {
        final InputStream stream = getResources().openRawResource(R.raw.test_negative_bin);

        loadContentsFrom(stream);
    }

    @OnClick(R.id.button_failing_plist)
    public void onLoadFailingPlistPressed() {
        //final InputStream stream = getResources().openRawResource(R.raw.connect);

        //loadContentsFrom(stream);
    }

    private void loadContentsFrom(InputStream stream) {

        String stringToDisplay;
        NSObject result;
        try {
            result = BinaryPropertyListParser.parse(stream);
            stringToDisplay = result.toXMLPropertyList();
        } catch (IOException | PropertyListFormatException | OutOfMemoryError e) {
            stringToDisplay = e.getLocalizedMessage();
        }

        mTextView.setText(stringToDisplay);
    }


}
