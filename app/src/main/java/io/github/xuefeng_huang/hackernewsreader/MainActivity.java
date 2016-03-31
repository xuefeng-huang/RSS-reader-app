package io.github.xuefeng_huang.hackernewsreader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView xmlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xmlTextView = (TextView)findViewById(R.id.xmlTextView);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("https://news.ycombinator.com/rss");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private String mFileContents;

        @Override
        protected String doInBackground(String... params) {
            mFileContents = downloadXMLFile(params[0]);
            if(mFileContents == null) {
                Log.d("DownloadData", "Downloading error");
            }
            return mFileContents;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("DownloadData", "result is: " + s);
            xmlTextView.setText(mFileContents);
        }

        private String downloadXMLFile(String urlPath) {
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                int response = connection.getResponseCode();
                Log.d("DownloadData", "response code is " + response);
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] charBuffer = new char[500];

                while (true) {
                    charRead = isr.read(charBuffer);
                    if(charRead <= 0) {
                        break;
                    }
                    //build the string
                    buffer.append(String.copyValueOf(charBuffer, 0, charRead));
                }

                return buffer.toString();
            } catch (IOException e) {
                Log.d("DownloadData", "IO exception" + e.getMessage());
            }

            return null;
        }
    }
}
