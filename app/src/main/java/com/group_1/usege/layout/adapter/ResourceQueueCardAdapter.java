package com.group_1.usege.layout.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group_1.usege.R;
import com.group_1.usege.manipulation.impl.IClickItemImageListener;
import com.group_1.usege.model.Image;

import java.util.List;

public class ResourceQueueCardAdapter extends RecyclerView.Adapter<ResourceQueueCardAdapter.ResourceQueueCardViewHolder> {
    Context context;
    private List<Image> resourceQueueImageList;

    public ResourceQueueCardAdapter(List<Image> list, Context context, IClickItemImageListener iClickItemImageListener) {
        this.resourceQueueImageList = list;
        this.context = context;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ResourceQueueCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resource_queue_image, parent, false);
        return new ResourceQueueCardViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ResourceQueueCardViewHolder holder, int position) {
        Image image = resourceQueueImageList.get(position);
        if (image == null) return;
        Uri uri = image.getUri();
        Glide.with(context).load(uri).into(holder.resourceQueueImageView);
        holder.resourceQueueImageView.setTag("RESOURE_QUEUE_IMAGE_VIEW");
        holder.resourceQueueImageView.setOnLongClickListener(v -> {
            ClipData.Item item = new ClipData.Item(uri);
            ClipData dragData = new ClipData((CharSequence) v.getTag(), new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(v);
            v.startDragAndDrop(dragData, myShadow, v, 0);
            v.setVisibility(View.INVISIBLE);
            return true;
        });

//        holder.resourceQueueImageView.setOnDragListener( (v, e) -> {
//            switch(e.getAction()) {
//
//                case DragEvent.ACTION_DRAG_STARTED:
//                    if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
//                        ((ImageView)v).setColorFilter(Color.BLUE);
//                        v.invalidate();
//                        return true;
//
//                    }
//                    return false;
//
//                case DragEvent.ACTION_DRAG_ENTERED:
//                    ((ImageView)v).setColorFilter(Color.GREEN);
//                    v.invalidate();
//                    return true;
//
//                case DragEvent.ACTION_DRAG_LOCATION:
//                    return true;
//
//                case DragEvent.ACTION_DRAG_EXITED:
//                    ((ImageView)v).setColorFilter(Color.BLUE);
//                    v.invalidate();
//                    return true;
//
//                case DragEvent.ACTION_DROP:
//
//                    // Get the item containing the dragged data.
//                    ClipData.Item item = e.getClipData().getItemAt(0);
//
//                    // Get the text data from the item.
//                    CharSequence dragData = item.getText();
//
//                    // Display a message containing the dragged data.
//                    System.out.println("Drag data is" + dragData);
//
//                    // Turn off color tints.
//                    ((ImageView)v).clearColorFilter();
//
//                    // Invalidate the view to force a redraw.
//                    v.invalidate();
//
//                    // Return true. DragEvent.getResult() returns true.
//                    return true;
//
//                case DragEvent.ACTION_DRAG_ENDED:
//
//                    ((ImageView)v).clearColorFilter();
//
//                    v.invalidate();
//
//                    return true;
//                default:
//                    Log.e("DragDrop Example","Unknown action type received by View.OnDragListener.");
//                    break;
//            }
//
//            return false;
//        });
    }

    @Override
    public int getItemCount() {
        if (resourceQueueImageList != null) {
            return resourceQueueImageList.size();
        }
        return 0;
    }

    public class ResourceQueueCardViewHolder extends RecyclerView.ViewHolder {
        private ImageView resourceQueueImageView;
        public ResourceQueueCardViewHolder(@NonNull View itemView) {
            super(itemView);
            resourceQueueImageView = itemView.findViewById(R.id.resource_queue_image_view);

        }
    }
}
