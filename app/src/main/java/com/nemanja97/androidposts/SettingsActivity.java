package com.nemanja97.androidposts;

import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        CheckBoxPreference cbSortByDate = (CheckBoxPreference) findPreference(getString(R.string.prefer_sort_post_date_key));
        cbSortByDate.setOnPreferenceClickListener(cbListenerPostByDate);

        CheckBoxPreference cbSortByPopularity = (CheckBoxPreference) findPreference(getString(R.string.sort_post_popularity_key));
        cbSortByPopularity.setOnPreferenceClickListener(cbListenerPostByPopularity);

        CheckBoxPreference cbSortCommentByDate = (CheckBoxPreference) findPreference(getString(R.string.sort_comment_date_key));
        cbSortCommentByDate.setOnPreferenceClickListener(cbListenerCommentByDate);

        CheckBoxPreference cbSortCommentByPopularity = (CheckBoxPreference) findPreference(getString(R.string.sort_comment_popularity_key));
        cbSortCommentByPopularity.setOnPreferenceClickListener(cbListenerCommentByPopularity);

        if (cbSortByDate.isChecked() & cbSortByDate.isEnabled()) {
            setStatus(findPreference(getString(R.string.sort_post_popularity_key)), false);
        }

        if(cbSortByPopularity.isChecked() & cbSortByPopularity.isEnabled()){
            setStatus(findPreference(getString(R.string.prefer_sort_post_date_key)),false);
        }

        if(cbSortCommentByDate.isChecked() & cbSortCommentByDate.isEnabled()){
            setStatus(findPreference(getString(R.string.sort_comment_popularity_key)),false);
        }

        if(cbSortCommentByPopularity.isChecked() & cbSortCommentByPopularity.isEnabled()){
            setStatus(findPreference(getString(R.string.sort_comment_date_key)),false);
        }

    }

    private void setStatus(Preference pref, boolean stats) {
        pref.setEnabled(stats);
    }

    private Preference.OnPreferenceClickListener cbListenerPostByDate = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;

            setStatus( findPreference(getString(R.string.sort_post_popularity_key)), !checkBoxPreference.isChecked());
            //setStatus(findPreference(getString(R.string.pref_sort_post_by_date_key)), !checkBoxPreference2.isChecked());
            return true;
        }
    };

    private Preference.OnPreferenceClickListener cbListenerPostByPopularity = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
            setStatus(findPreference(getString(R.string.prefer_sort_post_date_key)), !checkBoxPreference.isChecked());
            return true;
        }
    };

    private Preference.OnPreferenceClickListener cbListenerCommentByDate = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
            setStatus(findPreference(getString(R.string.sort_comment_popularity_key)),!checkBoxPreference.isChecked());
            return true;
        }
    };

    private Preference.OnPreferenceClickListener cbListenerCommentByPopularity = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
            setStatus(findPreference(getString(R.string.sort_comment_date_key)), !checkBoxPreference.isChecked());
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
