package com.lock.peter.nfcopen;


import android.os.Bundle;

import com.parse.ui.ParseLoginDispatchActivity;

public class MainActivity extends ParseLoginDispatchActivity {
    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Class<?> getTargetClass() {
        return MainMenu.class;
    }
}
