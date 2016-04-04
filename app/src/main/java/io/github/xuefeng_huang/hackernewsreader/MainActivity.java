package io.github.xuefeng_huang.hackernewsreader;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button btnGetNews;
    private ListView xmlList;
    private String mFileContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetNews = (Button)findViewById(R.id.btnParse);
        xmlList = (ListView)findViewById(R.id.xmlListView);

        xmlList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News record = (News)parent.getItemAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(record.getLink()));
                startActivity(intent);
            }
        });

        btnGetNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadData downloadData = new DownloadData();
                downloadData.execute("https://news.ycombinator.com/rss");
            }
        });


    }

    private class DownloadData extends AsyncTask<String, Void, String> {


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
            Parsexml parseApplication = new Parsexml(s);
            parseApplication.process();

            ArrayAdapter<News> arrayAdapter = new ArrayAdapter<>(
                    MainActivity.this,
                    R.layout.list_item,
                    parseApplication.getNews()
            );

            xmlList.setAdapter(arrayAdapter);
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
