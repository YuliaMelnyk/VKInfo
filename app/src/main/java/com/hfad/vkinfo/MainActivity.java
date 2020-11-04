package com.hfad.vkinfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.hfad.vkinfo.utils.NetworkUtils.generateURL;
import static com.hfad.vkinfo.utils.NetworkUtils.getResponceFromURL;

public class MainActivity extends AppCompatActivity {
    private EditText searchField;
    private Button searchButton;
    private TextView result, errorMessage;
    private ProgressBar loadingIndicator;


    private void showResultTextView() {
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorTextView() {
        result.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    class FlickrTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponceFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            String firstName = null;
            String lastName = null;
            String owner = null;
            String title = null;
            if (response != null && !response.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //JSONObject userInfo = jsonObject.getJSONObject("profile");

                    //firstName = userInfo.getString("first_name");
                    //lastName = userInfo.getString("last_name");

                    //for array of tags
                    JSONObject tagsInfo = jsonObject.getJSONObject("photos");

                    JSONArray jsonArray = tagsInfo.getJSONArray("photo");
                    String resultString = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tagInfo = jsonArray.getJSONObject(i);
                        owner = tagInfo.getString("owner");
                        title = tagInfo.getString("title");
                        resultString += "tag: " + owner + "\n" + "title: " + title + "\n" + "\n";
                    }
                    result.setText(resultString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //String resultString = "Name: " + firstName + "\n" + "Last name: " + lastName;

                //result.setText(resultString);

                showResultTextView();
            } else {
                showErrorTextView();
            }

            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchButton = findViewById(R.id.b_search_vk);
        result = findViewById(R.id.tv_name);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL generateURL = generateURL();
                //URL generateURL = generateURL(searchField.getText().toString());
                new FlickrTask().execute(generateURL);
            }
        });
    }


}
