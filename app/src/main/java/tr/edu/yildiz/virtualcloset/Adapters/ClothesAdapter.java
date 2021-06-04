package tr.edu.yildiz.virtualcloset.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.AddClothesActivity;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;
import tr.edu.yildiz.virtualcloset.R;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Clothes> clothes;
    private final LayoutInflater mInflater;
    private final DatabaseHelper databaseHelper;

    public ClothesAdapter(Context context, ArrayList<Clothes> clothes) {
        this.mInflater = LayoutInflater.from(context);
        this.clothes = clothes;
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.clothes_item, parent, false);
        return new ClothesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesAdapter.ViewHolder holder, int position) {
        Clothes c = clothes.get(position);
        byte[] image = c.getPhoto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        holder.type.setText(c.getType());
        holder.color.setBackgroundColor(Color.parseColor(c.getColor()));
        holder.pattern.setText(c.getPattern());
        holder.date.setText(c.getDate());
        holder.price.setText(c.getPrice());
        holder.photo.setImageBitmap(bitmap);

        holder.deleteClothes.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Kıyafeti silmek istiyor musunuz?");
            builder.setPositiveButton("Evet", (dialog, which) -> {
                Toast.makeText(context, "Kıyafet silindi", Toast.LENGTH_SHORT).show();
                databaseHelper.deleteClothes(clothes.get(position).getId());
                databaseHelper.updateDrawer(clothes.get(position).getDrawerNo(), clothes.size() - 1);
                clothes.remove(position);
                notifyDataSetChanged();
            });
            builder.setNegativeButton("Hayır", null);
            builder.show();
        });

        holder.editClothes.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddClothesActivity.class);
            intent.putExtra("drawerNo", clothes.get(position).getDrawerNo());
            intent.putExtra("clothesId", clothes.get(position).getId());
            intent.putExtra("update", "true");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return clothes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView type, color, pattern, date, price;
        ImageView photo;
        ImageButton deleteClothes, editClothes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.rvType);
            color = itemView.findViewById(R.id.rvColor);
            pattern = itemView.findViewById(R.id.rvPattern);
            date = itemView.findViewById(R.id.rvDate);
            price = itemView.findViewById(R.id.rvPrice);
            photo = itemView.findViewById(R.id.rvPhoto);
            deleteClothes = itemView.findViewById(R.id.deleteClothes);
            editClothes = itemView.findViewById(R.id.editClothes);
        }
    }
}
