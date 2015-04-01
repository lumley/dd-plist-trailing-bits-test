package com.lumley.testingddplistparser;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

import com.dd.plist.BinaryPropertyListParser;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListFormatException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.button_working_plist)
    Button mButtonLoadWorkingPlist;

    @InjectView(R.id.button_failing_plist)
    Button mButtonFailingPlist;

    @InjectView(R.id.button_failing_plist_fixed)
    Button mButtonFailingPlistFixed;

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
        final InputStream stream = getResources().openRawResource(R.raw.test_big_plist);

        loadContentsFrom(stream);
    }

    @OnClick(R.id.button_failing_plist)
    public void onLoadFailingPlistPressed() {
        final InputStream stream = getZipStreamFrom(getResources().openRawResource(R.raw.test_big_plist));

        loadContentsFrom(stream);
    }

    @OnClick(R.id.button_failing_plist_fixed)
    public void onLoadFailingPlistFixedPressed() {
        final InputStream stream = getZipStreamFrom(getResources().openRawResource(R.raw.test_big_plist));

        try {
            loadContentsFrom(readAll(stream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getZipStreamFrom(final InputStream inputStream) {
        InputStream zipInputStream = null;
        try {
            ByteArrayOutputStream bytesOutput = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutput = new GZIPOutputStream(bytesOutput);

            try {
                byte[] buffer = new byte[10240];
                for (int length; (length = inputStream.read(buffer)) != -1; ) {
                    gzipOutput.write(buffer, 0, length);
                }
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                }
                try {
                    gzipOutput.close();
                } catch (IOException ignore) {
                }
            }

            zipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytesOutput.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipInputStream;
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

    private void loadContentsFrom(byte[] byteArray) {
        String stringToDisplay;
        NSObject result;

        try {
            result = BinaryPropertyListParser.parse(byteArray);
            stringToDisplay = result.toXMLPropertyList();
        } catch (IOException | PropertyListFormatException | OutOfMemoryError e) {
            stringToDisplay = e.getLocalizedMessage();
        }

        mTextView.setText(stringToDisplay);
    }

    protected static byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[2048];
        int read;
        while ((read = in.read(buf, 0, buf.length)) != -1) {
            outputStream.write(buf, 0, read);
        }
        return outputStream.toByteArray();
    }

}
