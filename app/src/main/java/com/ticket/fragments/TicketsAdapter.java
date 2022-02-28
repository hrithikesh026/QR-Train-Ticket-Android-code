package com.ticket.fragments;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.WriterException;
import com.ticket.R;
import com.ticket.objects.Tickets;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.Viewholder> {

    private Context context;
    private ArrayList<Tickets> TicketsArrayList;

    // Constructor
    public TicketsAdapter(Context context, ArrayList<Tickets> TicketsArrayList) {
        this.context = context;
        this.TicketsArrayList = TicketsArrayList;
    }

    @NonNull
    @Override
    public TicketsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        Tickets model = TicketsArrayList.get(position);
        holder.train_no.setText(""+model.getTrain_no());
        holder.train_name.setText(model.getTrain_name());
        holder.source.setText(model.getSource());
        holder.destination.setText(model.getDestination());
        // initializing a variable for default display.
        Display display = holder.manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        holder.qrgEncoder = new QRGEncoder(model.getJsonString(), null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            holder.bitmap = holder.qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            holder.qrImage.setImageBitmap(holder.bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return TicketsArrayList.size();
    }

    // View holder class for initializing of 
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView source,destination,train_no,train_name;
        private ImageView qrImage;
        WindowManager manager;
        Bitmap bitmap;
        QRGEncoder qrgEncoder;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            source = itemView.findViewById(R.id.source);
            destination = itemView.findViewById(R.id.destination);
            train_name = itemView.findViewById(R.id.train_name);
            train_no = itemView.findViewById(R.id.train_no);
            qrImage = itemView.findViewById(R.id.qrImageView);
            manager = (WindowManager) itemView.getContext().getSystemService(WINDOW_SERVICE);
        }
    }
}
