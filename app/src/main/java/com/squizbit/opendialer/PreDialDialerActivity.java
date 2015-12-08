package com.squizbit.opendialer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.squizbit.opendialer.fragment.DialerFragment;
import com.squizbit.opendialer.models.Preferences;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * An activity which displays a predialed number in a modal style app.
 */
public class PreDialDialerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG_DIALER_FRAGMENT =
            "com.squizbit.opendialer.PreDialDialerActivity.TAG_DIALER_FRAGMENT";

    //region View fields
    @InjectView(R.id.dialer_container)
    LinearLayout mDialerContainer;
    @InjectView(R.id.floatingActionButtonMain)

    FloatingActionButton mFloatingActionButtonMain;
    private DialerFragment mDialerFragment;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.predial_dialer_view);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(mDialerContainer.getId(), DialerFragment.newInstance(), TAG_DIALER_FRAGMENT)
                    .commit();
        }

        mFloatingActionButtonMain.setOnClickListener(this);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DialerFragment) {
            mDialerFragment = (DialerFragment) fragment;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getData() != null) {
            mDialerFragment.setNumber(getIntent().getData().getSchemeSpecificPart());
        }
    }

    @Override
    public void onClick(View v) {
        Preferences preferences = new Preferences(this);
        String number = mDialerFragment.getNumber();
        preferences.setLastDialedNumber(number);
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
            return;
        }

        finish();
    }
}
