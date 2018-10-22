package com.example.writeyourquote.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.writeyourquote.util.Commons;
import com.example.writeyourquote.R;
import com.example.writeyourquote.activity.ListQuotesActivity;
import com.example.writeyourquote.activity.WriteQuoteActivity;
import com.example.writeyourquote.model.Quote;

import java.util.List;

public class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.MyViewHolder> {

    private List<Quote> quoteList;
    private String quoteId = "";
    private Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView quoteDescription, quoteAuthorName;
        ImageView imgShareQuote, imgOptions;

        public MyViewHolder(View view) {
            super(view);
            quoteDescription = view.findViewById(R.id.quoteDescription);
            quoteAuthorName = view.findViewById(R.id.quoteAuthorName);
            imgShareQuote = view.findViewById(R.id.imgShareQuote);
            imgOptions = view.findViewById(R.id.imgOptions);
        }
    }


    public QuoteListAdapter(Activity context, List<Quote> quoteList) {
        this.quoteList = quoteList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quote_item_view, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Quote quote = quoteList.get(position);
        holder.quoteDescription.setText(quote.getQuotation());
        holder.quoteAuthorName.setText(quote.getAuthorName());

        holder.imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quoteId = quote.getId() + "";
                showPopupMenu(holder.imgOptions);
            }
        });

        holder.imgShareQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Quote");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, quote.getQuotation());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context.startActivity(Intent.createChooser(sharingIntent, "Share Quote"), null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popupMenu.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit:
                    Bundle bundle = new Bundle();
                    bundle.putString("quoteId", quoteId);
                    Intent nextCall = new Intent(context, WriteQuoteActivity.class);
                    nextCall.putExtras(bundle);
                    context.startActivityForResult(nextCall, 100);

                    break;
                case R.id.delete:
                    Toast.makeText(context, context.getResources().getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Commons.removeQuoteBasedOnId(quoteId);
                        ((ListQuotesActivity) context).showListOfQuotes();
                    }
                break;
            }
            return false;
        }
    }

}