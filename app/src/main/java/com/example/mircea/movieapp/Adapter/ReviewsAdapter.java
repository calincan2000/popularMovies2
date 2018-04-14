package com.example.mircea.movieapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mircea.movieapp.R;
import com.example.mircea.movieapp.model.Review;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private static final String LOG = ReviewsAdapter.class.getSimpleName();
    private List<Review> mReviewsList;
    private Context mContext;
    private int previousExpandedPosition = -1;
    private int mExpandedPosition = -1;


    private final ReviewAdapterOnClickHandler mClickHandler;



    public interface ReviewAdapterOnClickHandler {
        void onClick(String ReviewItem);
    }

    // Add an interface called ListItemClickListener
    // Within that interface, define a void method called onListItemClick that takes an int as a parameter

    public ReviewsAdapter(Context context, List<Review> mReviewsList, ReviewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        this.mReviewsList = mReviewsList;
        this.mContext = context;
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
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewsAdapterViewHolder(view);
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
    public void onBindViewHolder(ReviewsAdapterViewHolder holder, final int position) {
       Review reviewItem= mReviewsList.get(position);
       /*// Log.i(LOG, "#" + position);
        //holder.listItemReviewView.setText(reviewItem);
        Picasso.get().load(reviewItem.getMoviePosterImageThumblail())
                .placeholder(R.drawable.user_placeholder1)
                .error(R.drawable.user_placeholder_error1)
                .into(holder.listItemReviewView);*/

        String review = mReviewsList.get(position).getReview();
        holder.listItemReviewView.setText(review);



      /* final boolean isExpanded = position == mExpandedPosition;
        holder.listItemReviewView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });*/


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return (null != mReviewsList ? mReviewsList.size() : 0);
    }


    /**
     * Cache of the children views for a list item.
     */

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        @BindView(R.id.thumbnail)
        TextView listItemReviewView;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String reviewItem = String.valueOf(adapterPosition);
            mClickHandler.onClick(reviewItem);


        }


    }

    /**
     * This method is used to set the movie forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     *
     * @param reviewData The new movie data to be displayed.
     */

    public void setReviewsData(List<Review> reviewData) {
        mReviewsList = reviewData;
        notifyDataSetChanged();
    }
}
