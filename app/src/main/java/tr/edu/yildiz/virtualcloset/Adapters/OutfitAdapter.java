package tr.edu.yildiz.virtualcloset.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;
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
        Clothes clothes1;
        byte[] image1;

        if (outfits.get(position).getOverhead() != -1) {
            clothes1 = databaseHelper.getClothes(outfits.get(position).getOverhead());
            if (clothes1 != null) {
                image1 = clothes1.getPhoto();
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(image1, 0, image1.length);
                holder.overHead.setImageBitmap(bitmap1);
            }
        } else {
            clothes1 = null;
        }

        Clothes clothes2 = databaseHelper.getClothes(outfits.get(position).getUpper());
        if (clothes2 != null) {
            byte[] image2 = clothes2.getPhoto();
            Bitmap bitmap2 = BitmapFactory.decodeByteArray(image2, 0, image2.length);
            holder.upper.setImageBitmap(bitmap2);
        }

        Clothes clothes3 = databaseHelper.getClothes(outfits.get(position).getLower());
        if (clothes3 != null) {
            byte[] image3 = clothes3.getPhoto();
            Bitmap bitmap3 = BitmapFactory.decodeByteArray(image3, 0, image3.length);
            holder.lower.setImageBitmap(bitmap3);
        }

        Clothes clothes4 = databaseHelper.getClothes(outfits.get(position).getFoot());
        if (clothes4 != null) {
            byte[] image4 = clothes4.getPhoto();
            Bitmap bitmap4 = BitmapFactory.decodeByteArray(image4, 0, image4.length);
            holder.foot.setImageBitmap(bitmap4);
        }

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

        holder.shareOutfit.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");

            File f1 = null, f2 = null, f3 = null, f4 = null;

            if (clothes1 != null)
                f1 = new File(Environment.getExternalStorageDirectory() + File.separator + clothes1.getId() + ".png");
            if (clothes2 != null)
                f2 = new File(Environment.getExternalStorageDirectory() + File.separator + clothes2.getId() + ".png");
            if (clothes3 != null)
                f3 = new File(Environment.getExternalStorageDirectory() + File.separator + clothes3.getId() + ".png");
            if (clothes4 != null)
                f4 = new File(Environment.getExternalStorageDirectory() + File.separator + clothes4.getId() + ".png");

            ArrayList<Uri> files = new ArrayList<>();
            if (f1 != null) files.add(Uri.parse(f1.getAbsolutePath()));
            if (f2 != null) files.add(Uri.parse(f2.getAbsolutePath()));
            if (f3 != null) files.add(Uri.parse(f3.getAbsolutePath()));
            if (f4 != null) files.add(Uri.parse(f4.getAbsolutePath()));

            share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
            context.startActivity(Intent.createChooser(share, "Fotoğrafları arkadaşlarınla paylaş"));
        });
    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView overHead, upper, lower, foot;
        ImageButton deleteOutfit, shareOutfit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            overHead = itemView.findViewById(R.id.overHead);
            upper = itemView.findViewById(R.id.upper);
            lower = itemView.findViewById(R.id.lower);
            foot = itemView.findViewById(R.id.foot);
            deleteOutfit = itemView.findViewById(R.id.deleteOutfit);
            shareOutfit = itemView.findViewById(R.id.shareOutfit);

            deleteOutfit.setVisibility(View.VISIBLE);
            shareOutfit.setVisibility(View.VISIBLE);
        }
    }
}
