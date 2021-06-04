package tr.edu.yildiz.virtualcloset.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.AddEventActivity;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Event;
import tr.edu.yildiz.virtualcloset.R;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Event> events;
    private final LayoutInflater mInflater;
    private final DatabaseHelper databaseHelper;

    public EventAdapter(Context context, ArrayList<Event> events) {
        this.mInflater = LayoutInflater.from(context);
        this.events = events;
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_item, parent, false);
        return new EventAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);

        holder.name.setText(event.getName());
        holder.type.setText(event.getType());
        holder.date.setText(event.getDate());
        holder.outfit.setText("Kombin " + event.getOutfitNo());
        holder.location.setText(event.getLocation());

        holder.location.setOnClickListener(v -> {
            String format = "geo:0,0?q=" + event.getLatitude() + "," + event.getLongitude();
            Uri uri = Uri.parse(format);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });

        holder.deleteEvent.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Etkinliği silmek istiyor musunuz?");
            builder.setPositiveButton("Evet", (dialog, which) -> {
                Toast.makeText(context, "Etkinlik silindi", Toast.LENGTH_SHORT).show();
                databaseHelper.deleteEvent(events.get(position).getId());
                events.remove(position);
                notifyDataSetChanged();
            });
            builder.setNegativeButton("Hayır", null);
            builder.show();
        });

        holder.editEvent.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEventActivity.class);
            intent.putExtra("eventId", events.get(position).getId());
            intent.putExtra("update", "true");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, type, date, outfit, location;
        ImageButton deleteEvent, editEvent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            type = itemView.findViewById(R.id.itemType);
            date = itemView.findViewById(R.id.itemDate);
            outfit = itemView.findViewById(R.id.itemOutfit);
            location = itemView.findViewById(R.id.itemLocation);
            deleteEvent = itemView.findViewById(R.id.deleteEvent);
            editEvent = itemView.findViewById(R.id.editEvent);
        }
    }
}
