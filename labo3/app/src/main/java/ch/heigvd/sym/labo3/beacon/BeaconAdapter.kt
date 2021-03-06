package ch.heigvd.sym.labo3.beacon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.sym.labo3.R
import org.altbeacon.beacon.Beacon

class BeaconAdapter : RecyclerView.Adapter<BeaconAdapter.ViewHolder>() {

    private var beacons: List<Beacon> = ArrayList()

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Get all the data TextViews
        val uuid: TextView = itemView.findViewById(R.id.uuid_data)
        val major: TextView = itemView.findViewById(R.id.major_data)
        val minor: TextView = itemView.findViewById(R.id.minor_data)
        val rssi: TextView = itemView.findViewById(R.id.rssi_data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_beacon, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: BeaconAdapter.ViewHolder, position: Int) {
        val beacon: Beacon = beacons[position]
        // Set data on the TextViews
        viewHolder.uuid.text = beacon.id1.toString()
        viewHolder.major.text = beacon.id2.toString()
        viewHolder.minor.text = beacon.id3.toString()
        viewHolder.rssi.text = beacon.rssi.toString()
    }

    override fun getItemCount(): Int {
        return beacons.size
    }

    fun setBeacons(value: Collection<Beacon>) {
        beacons = value.toList()
    }
}