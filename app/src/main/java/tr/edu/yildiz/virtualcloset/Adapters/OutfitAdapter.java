package tr.edu.yildiz.virtualcloset.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;
import tr.edu.yildiz.virtualcloset.Model.Drawer;
import tr.edu.yildiz.virtualcloset.Model.Outfit;
import tr.edu.yildiz.virtualcloset.R;

public class OutfitAdapter extends RecyclerView.Adapter<OutfitAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Outfit> outfits;
    private final LayoutInflater mInflater;
    private final DatabaseHelper databaseHelper;

    public OutfitAdapter(Context context, ArrayList<Outfit> outfits) {
        this.mInflater = LayoutInflater.from(context);
        this.outfits = outfits;
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public OutfitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.outfit_item, parent, false);
        return new OutfitAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitAdapter.ViewHolder holder, int position) {
        Clothes clothes1 = databaseHelper.getClothes(outfits.get(position).getOverhead());
        Clothes clothes2 = databaseHelper.getClothes(outfits.get(position).getFace());
        Clothes clothes3 = databaseHelper.getClothes(outfits.get(position).getUpper());
        Clothes clothes4 = databaseHelper.getClothes(outfits.get(position).getLower());
        Clothes clothes5 = databaseHelper.getClothes(outfits.get(position).getFoot());

        byte[] image1 = clothes1.getPhoto();
        byte[] image2 = clothes2.getPhoto();
        byte[] image3 = clothes3.getPhoto();
        byte[] image4 = clothes4.getPhoto();
        byte[] image5 = clothes5.getPhoto();
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(image1, 0, image1.length);
        Bitmap bitmap2 = BitmapFactory.decodeByteArray(image2, 0, image2.length);
        Bitmap bitmap3 = BitmapFactory.decodeByteArray(image3, 0, image3.length);
        Bitmap bitmap4 = BitmapFactory.decodeByteArray(image4, 0, image4.length);
        Bitmap bitmap5 = BitmapFactory.decodeByteArray(image5, 0, image5.length);

        holder.overHead.setImageBitmap(bitmap1);
        holder.face.setImageBitmap(bitmap2);
        holder.upper.setImageBitmap(bitmap3);
        holder.lower.setImageBitmap(bitmap4);
        holder.foot.setImageBitmap(bitmap5);

        holder.deleteOutfit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Kombini silmek istiyor musunuz?");
            builder.setPositiveButton("Evet", (dialog, which) -> {
                Toast.makeText(context, "Kombin silindi", Toast.LENGTH_SHORT).show();
                databaseHelper.deleteOutfit(outfits.get(position).getId());
                outfits.remove(position);
                notifyDataSetChanged();
            });
            builder.setNegativeButton("Hayır", null);
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView overHead, face, upper, lower, foot, deleteOutfit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            overHead = itemView.findViewById(R.id.overHead);
            face = itemView.findViewById(R.id.face);
            upper = itemView.findViewById(R.id.upper);
            lower = itemView.findViewById(R.id.lower);
            foot = itemView.findViewById(R.id.foot);
            deleteOutfit = itemView.findViewById(R.id.deleteOutfit);
        }
    }
}
