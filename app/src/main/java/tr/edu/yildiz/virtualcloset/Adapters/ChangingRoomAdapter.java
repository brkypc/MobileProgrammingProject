package tr.edu.yildiz.virtualcloset.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.ChangingRoomActivity;
import tr.edu.yildiz.virtualcloset.Model.Clothes;
import tr.edu.yildiz.virtualcloset.R;

public class ChangingRoomAdapter extends RecyclerView.Adapter<ChangingRoomAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private final ArrayList<Clothes> clothes;
    public final int type;

    public ChangingRoomAdapter(Context context, ArrayList<Clothes> clothes, int type) {
        this.mInflater = LayoutInflater.from(context);
        this.clothes = clothes;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.room_item, parent, false);
        return new ChangingRoomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangingRoomAdapter.ViewHolder holder, int position) {
        Clothes c = clothes.get(position);
        byte[] image = c.getPhoto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        holder.photo.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(v -> {
            switch (type) {
                case 1:
                    ChangingRoomActivity.cOverHead = c;
                    break;
                case 2:
                    ChangingRoomActivity.cUpper = c;
                    break;
                case 3:
                    ChangingRoomActivity.cLower = c;
                    break;
                case 4:
                    ChangingRoomActivity.cFoot = c;
                    break;
            }
            ChangingRoomActivity.bitmap = bitmap;
            ChangingRoomActivity.selected = true;
            ChangingRoomActivity.dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return clothes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.rvImage);
        }
    }
}
