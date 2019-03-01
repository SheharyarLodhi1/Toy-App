package com.next.sheharyar.playspace;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.next.sheharyar.playspace.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText mSearchBoxEditText;
    TextView mUrlDisplayTextView, mSearchResultsTextView, mDisplayErrorMessage;
    ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText)findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView)findViewById(R.id.tv_url_display);
        mDisplayErrorMessage = (TextView)findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.pb_loading_indicattor);
        mSearchResultsTextView = (TextView)findViewById(R.id.tv_github_search_results_json);
    }

    void makeGithubSearchQuery(){
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        if (mSearchBoxEditText == null && mSearchBoxEditText.equals("")){
            Toast.makeText(this, " Please Enter something to search ..", Toast.LENGTH_SHORT).show();
        } else {
            mUrlDisplayTextView.setText(githubSearchUrl.toString());
            new GithubQueryTask().execute(githubSearchUrl);
        }


        // TODO (4) Create a new GithubQueryTask and call its execute method, passing in the url to query
    }

    private void showJsonDataView(){
        mDisplayErrorMessage.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mDisplayErrorMessage.setVisibility(View.VISIBLE);
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
    }



    // TODO (1) Create a class called GithubQueryTask that extends AsyncTask<URL, Void, String>
    public class GithubQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        // TODO (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        // TODO (3) Override onPostExecute to display the results in the TextView

        @Override
        protected void onPostExecute(String githubsearchResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (githubsearchResults != null && !githubsearchResults.equals("")){
                showJsonDataView();
                mSearchResultsTextView.setText(githubsearchResults);
            } else {
                showErrorMessage();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_search){
//            String message = "You clicked on Search Button";
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            makeGithubSearchQuery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
