package tr.edu.yildiz.virtualcloset.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.ClothesActivity;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.MenuActivity;
import tr.edu.yildiz.virtualcloset.Model.Clothes;
import tr.edu.yildiz.virtualcloset.Model.Drawer;
import tr.edu.yildiz.virtualcloset.R;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Drawer> drawers;
    private final LayoutInflater mInflater;
    private final DatabaseHelper databaseHelper;

    public DrawerAdapter(Context context, ArrayList<Drawer> drawers) {
        this.mInflater = LayoutInflater.from(context);
        this.drawers = drawers;
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.drawer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  DrawerAdapter.ViewHolder holder, int position) {
        String count = "Kıyafet Sayısı\n" + drawers.get(position).getCount();
        holder.drawerName.setText(drawers.get(position).getName());
        holder.drawerCount.setText(count);
        holder.deleteDrawer.setOnClickListener(v -> {
            Toast.makeText(context, "Çekmece silindi", Toast.LENGTH_SHORT).show();
            drawers.remove(position);
            notifyDataSetChanged();
            databaseHelper.deleteDrawer(drawers.get(position).getId());
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClothesActivity.class);
                intent.putExtra("drawerNo", drawers.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView drawerName, drawerCount;
        ImageButton deleteDrawer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drawerName = itemView.findViewById(R.id.drawerName);
            drawerCount = itemView.findViewById(R.id.drawerCount);
            deleteDrawer = itemView.findViewById(R.id.deleteDrawer);
        }
    }
}
