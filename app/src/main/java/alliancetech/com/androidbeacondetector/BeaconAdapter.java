package alliancetech.com.androidbeacondetector;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by asolano on 8/25/15.
 */
public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.ViewHolder>{

    private List<Beacon> beacons;
    private Context context;

    public BeaconAdapter(Context context, Collection<Beacon> beacons) {
        this.context = context;

        if(beacons instanceof List)
            this.beacons = (List) beacons;
        else
            this.beacons = new ArrayList<>(beacons);
    }

    @Override
    public BeaconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.beacon_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(BeaconAdapter.ViewHolder holder, int position) {
        Beacon beacon = beacons.get(position);

        holder.tvUUID.setText(beacon.getId1().toString());
        holder.tvMajor.setText(beacon.getId2().toString());
        holder.tvMinor.setText(beacon.getId3().toString());
        holder.tvAccuracy.setText(Double.toString(beacon.getTxPower()));
        holder.tvDistance.setText(String.valueOf(beacon.getDistance()));
        holder.tvRSSI.setText(Integer.toString(beacon.getRssi()));
    }

    @Override
    public int getItemCount() {
        return beacons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUUID;
        public TextView tvMajor;
        public TextView tvMinor;
        public TextView tvAccuracy;
        public TextView tvDistance;
        public TextView tvRSSI;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUUID = (TextView) itemView.findViewById(R.id.tvUUIDVal);
            tvMajor = (TextView) itemView.findViewById(R.id.tvMajorVal);
            tvMinor = (TextView) itemView.findViewById(R.id.tvMinorVal);
            tvAccuracy = (TextView) itemView.findViewById(R.id.tvAccuracyVal);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistanceVal);
            tvRSSI = (TextView) itemView.findViewById(R.id.tvRSSIVal);
        }
    }
}
