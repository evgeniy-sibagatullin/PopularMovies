package com.seriously.android.popularmovies.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.seriously.android.popularmovies.R;
import com.seriously.android.popularmovies.adapter.MovieTypePagerAdapter;
import com.seriously.android.popularmovies.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityBinding mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mBinding.viewPager.setAdapter(new MovieTypePagerAdapter(getSupportFragmentManager(), this));
        mBinding.tabs.setupWithViewPager(mBinding.viewPager);
    }
}
