package com.example.mircea.movieapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mircea.movieapp.R;
import com.example.mircea.movieapp.model.Trailers;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailersAdapterViewHolder> {

    private static final String LOG = TrailerAdapter.class.getSimpleName();
    private List<Trailers> mTrailersList;
    private Context mContext;


    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(String TrailerItem);
    }

    // Add an interface called ListItemClickListener
    // Within that interface, define a void method called onListItemClick that takes an int as a parameter

    public TrailerAdapter(Context context, List<Trailers> mTrailersList, TrailerAdapterOnClickHandler clickHandler) {
       mClickHandler=clickHandler;
       this.mTrailersList =mTrailersList;
       this.mContext=context;
    }
    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */

    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailersAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */


    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder holder, int position) {
        Trailers trailerItem= mTrailersList.get(position);
       // Log.i(LOG, "#" + position);
       // holder.listItemReviewView.setText(trailerItem);
        Picasso.get().load(trailerItem.getMoviePosterImageThumblail())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(holder.listItemTrailerView);




    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return (null != mTrailersList ? mTrailersList.size() : 0);
    }


    /**
     * Cache of the children views for a list item.
     */

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        @BindView(R.id.thumbnail)
        ImageView listItemTrailerView;

        public TrailersAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String movieItem = String.valueOf(adapterPosition);
            mClickHandler.onClick(movieItem);

        }


    }
    /**
     * This method is used to set the movie forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     *
     * @param movieData The new movie data to be displayed.
     */

    public void setTrailersData(List<Trailers> movieData)
    {
        mTrailersList =movieData;
        notifyDataSetChanged();
    }
}
