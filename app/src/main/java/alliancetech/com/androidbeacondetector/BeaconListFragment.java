package alliancetech.com.androidbeacondetector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by asolano on 8/25/15.
 */
public class BeaconListFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{

    private static final String TAG = BeaconListFragment.class.getSimpleName();

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private BeaconAdapter adapter;

    private Collection<Beacon> beacons;
    private SeekBar mSeekBarDistance;
    private TextView mTvSeekBarLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.beacon_list_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mSeekBarDistance = (SeekBar) view.findViewById(R.id.sbDistanceFilter);
        mTvSeekBarLabel = (TextView) view.findViewById(R.id.tvSbLabel);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSeekBarDistance.setOnSeekBarChangeListener(this);

        this.beacons = new ArrayList<>();
        adapter = new BeaconAdapter(getActivity(), this.beacons);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        setDistanceValue(mSeekBarDistance.getProgress());
    }

    public void setBeacons(final Beacon beacon) {

        if(this.beacons.contains(beacon)){
            int index = ((List)this.beacons).indexOf(beacon);
            ((List)this.beacons).set(index, beacon);
        }else{
            this.beacons.add(beacon);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, "onProgressChanged - progress: " + progress);
        setDistanceValue(progress);
        filterBeaconsByDistance(progress);
    }

    private void setDistanceValue(int progress) {
        String text = String.format(getString(R.string.txt_filter_distance), progress);
        mTvSeekBarLabel.setText(text);
    }

    private void filterBeaconsByDistance(int distance) {
        if(!this.beacons.isEmpty()){
            Collection<Beacon> removableBeacons = new ArrayList<>();
            for(Beacon beacon : this.beacons){
                if(beacon.getDistance() > distance){
                    removableBeacons.add(beacon);
                }
            }

            beacons.removeAll(removableBeacons);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStopTrackingTouch");
    }

    public double getRange() {
        return mSeekBarDistance.getProgress();
    }
}
