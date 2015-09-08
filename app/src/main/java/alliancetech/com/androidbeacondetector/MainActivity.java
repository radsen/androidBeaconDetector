package alliancetech.com.androidbeacondetector;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends FragmentActivity implements BeaconConsumer, DialogInterface.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ALT_BEACON = "m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";
    private static final String ESTIMOTE_BEACON = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";

    private BeaconManager beaconManager;
    private BeaconListFragment beaconFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!mBluetoothAdapter.isEnabled()){

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.txt_information);
            dialog.setMessage(R.string.txt_bluetooth_required);
            dialog.setNegativeButton(android.R.string.cancel, this);
            dialog.setPositiveButton(android.R.string.ok, this);
            dialog.create();
            dialog.show();

        }

        beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout(ESTIMOTE_BEACON));

        beaconManager.bind(this);

        beaconFragment = (BeaconListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.beaconListFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    double range = beaconFragment.getRange();
                    Beacon beacon = beacons.iterator().next();
                    if(beacon.getDistance() < range)
                        beaconFragment.setBeacons(beacon);
                    //Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                }
            }


        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case AlertDialog.BUTTON_NEGATIVE:
                dialog.dismiss();
            break;
            case AlertDialog.BUTTON_POSITIVE:
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);
            break;
        }
    }
}
