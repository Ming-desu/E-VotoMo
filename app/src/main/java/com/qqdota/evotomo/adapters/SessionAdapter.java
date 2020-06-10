package com.qqdota.evotomo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.qqdota.evotomo.R;
import com.qqdota.evotomo.models.Code;
import com.qqdota.evotomo.models.SessionResponse;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SessionResponse> sessionResponses;
    private Context context;
    private ISessionAdapter listener;
    private int lastPosition = -1;

    /**
     * Class Constructor
     * @param sessionResponses the list of sessions
     * @param context the application context
     * @param listener the interface listener to provide extensive customization
     */
    public SessionAdapter(List<SessionResponse> sessionResponses, Context context, ISessionAdapter listener) {
        this.sessionResponses = sessionResponses;
        this.context = context;
        this.listener = listener;
    }

    /**
     * The setter method for the session responses
     * @param sessionResponses the new session responses
     */
    public void setSessionResponses(List<SessionResponse> sessionResponses) {
        List<SessionResponse> oldSessionResponses = this.sessionResponses;

        // Check first if the old one is the same ...
        if (!isListSame(oldSessionResponses, sessionResponses)) {
            this.sessionResponses = sessionResponses;
            notifyDataSetChanged();
        }
    }

    /**
     * The method to add a session response at the top of list
     * @param sessionResponse the session response to be added
     */
    public void insertSession(SessionResponse sessionResponse) {
        sessionResponses.add(0, sessionResponse);
        notifyItemInserted(0);
    }

    /**
     * The method to set the session code to its corresponding session
     * @param code the code to be added
     */
    public void setSessionCode(Code code) {
        // Iterate through each and every session response
        for (int i = 0; i < sessionResponses.size(); i++) {
            // Check if the session id from the code is the same with the id of the session response
            if (sessionResponses.get(i).getId() == code.getSessionId()) {
                sessionResponses.get(i).setCode(code);
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * The method to compare whether the two list is the same or not.
     * @param oldSessionResponses the previous session responses
     * @param newSessionResponses the newly arrive session responses
     * @return
     */
    private boolean isListSame(List<SessionResponse> oldSessionResponses, List<SessionResponse> newSessionResponses) {
        if (oldSessionResponses == null || newSessionResponses == null)
            return false;

        if (oldSessionResponses.size() != newSessionResponses.size())
            return false;

        // Currently we are only checking if the id is the same or not
        for (int i = 0; i < oldSessionResponses.size(); i++)
            if (oldSessionResponses.get(i).getId() != newSessionResponses.get(i).getId())
                return false;

        return true;
    }

    /**
     * The method responsible for the layout to be display
     * @param parent the parent of the view
     * @param viewType the type of the layout
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get the layout inflater from the parent context
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the appropriate UI for this component
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.uc_session_with_code, parent, false);
            return new SessionWithCodeViewHolder(view);
        }

        View view = inflater.inflate(R.layout.uc_session, parent, false);
        return new SessionViewHolder(view);
    }

    /**
     * The method responsible for binding the data to the UI itself
     * @param holder the view holder of the custom layout
     * @param position the position of this item
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final SessionResponse sessionResponse = sessionResponses.get(position);

        // We are just checking which type of layout to render
        if (holder.getItemViewType() == 0) {
            SessionViewHolder viewHolder = (SessionViewHolder)holder;
            viewHolder.setSessionResponse(sessionResponse);
        }
        else {
            SessionWithCodeViewHolder viewHolder = (SessionWithCodeViewHolder)holder;
            viewHolder.setSessionResponse(sessionResponse);
        }

        setAnimation(holder.itemView, position);
    }

    /**
     * The method to animate each item on the recycler view
     * @param viewToAnimate the item to be animate
     * @param position the position of the itemm
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    /**
     * The method to determine which type of layout that the adapter should render
     * @param position the position of the item
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        SessionResponse sessionResponse = sessionResponses.get(position);
        int type = 0;

        // We are just checking if the session code is currently empty
        if (sessionResponse.getCode() != null)
            type = 1;
        return type;
    }

    /**
     * The method that will get the total items of the session responses
     * @return the count of the session responses
     */
    @Override
    public int getItemCount() {
        return sessionResponses == null ? 0 : sessionResponses.size();
    }

    /**
     * The method to handle the fast scrolling on mobile
     * since it could mess around with the reusable of the view
     * @param holder the view holder to be detached
     */
    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        // Clears the animation
        holder.itemView.clearAnimation();
    }

    /**
     * The view holder class for the session view holder
     */
    public class SessionViewHolder extends RecyclerView.ViewHolder {
        private SessionResponse sessionResponse;
        private TextView textTitle;
        private TextView textDate;
        private TextView textAddedBy;
        private Button buttonViewMore;
        private Button buttonGenerateCode;

        /**
         * Class Constructor
         * @param itemView the view layout to be rendered
         */
        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeUIComponents(itemView);
        }

        /**
         * The setter method of the session response
         * @param sessionResponse the session response bounded to this view
         */
        public void setSessionResponse(SessionResponse sessionResponse) {
            this.sessionResponse = sessionResponse;

            // Setup the view with the data from session response
            setupView();
        }

        /**
         * The method that will initialize all the UI components
         * @param itemView the parent of the UI components
         */
        private void initializeUIComponents(@NonNull View itemView) {
            textTitle = itemView.findViewById(R.id.text_view_title);
            textDate = itemView.findViewById(R.id.text_view_date);
            textAddedBy = itemView.findViewById(R.id.text_view_added_by);
            buttonViewMore = itemView.findViewById(R.id.button_view_more);
            buttonGenerateCode = itemView.findViewById(R.id.button_generate_code);
        }

        /**
         * The method that will populate the UI components with their corresponding values
         */
        private void setupView() {
            textTitle.setText(sessionResponse.getName());
            textDate.setText(sessionResponse.getVoteStart() + " - " + sessionResponse.getVoteEnd());
            textAddedBy.setText("Added by: " + sessionResponse.getUser().getFullName());

            buttonViewMore.setOnClickListener(new ViewMoreOnClickListener(sessionResponse));
            buttonGenerateCode.setOnClickListener(new GenerateCodeOnClickListener(sessionResponse));
        }

        /**
         * The class interface for the view more click listener
         */
        private final class ViewMoreOnClickListener implements View.OnClickListener {

            private SessionResponse sessionResponse;

            /**
             * Class Constructor
             * @param sessionResponse the session response associated to this class
             */
            public ViewMoreOnClickListener(SessionResponse sessionResponse) {
                this.sessionResponse = sessionResponse;
            }

            @Override
            public void onClick(View v) {
                listener.onClickViewMore(sessionResponse);
            }
        }

        /**
         * The class interface for the generate code click listener
         */
        private final class GenerateCodeOnClickListener implements View.OnClickListener {

            private SessionResponse sessionResponse;

            /**
             * Class Constructor
             * @param sessionResponse the session response associated to this class
             */
            public GenerateCodeOnClickListener(SessionResponse sessionResponse) {
                this.sessionResponse = sessionResponse;
            }

            @Override
            public void onClick(View v) {
                listener.onClickGenerateCode(sessionResponse);
            }
        }
    }

    /**
     * The view holder class for the session with code view holder
     */
    public class SessionWithCodeViewHolder extends RecyclerView.ViewHolder {
        private SessionResponse sessionResponse;
        private TextView textTitle;
        private TextView textDate;
        private TextView textAddedBy;
        private TextView textCode;
        private Button buttonViewMore;
        private Button buttonGenerateCode;

        /**
         * Class Constructor
         * @param itemView the view layout to be rendered
         */
        public SessionWithCodeViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeUIComponents(itemView);
        }

        /**
         * The setter method of the session response
         * @param sessionResponse the session response bounded to this view
         */
        public void setSessionResponse(SessionResponse sessionResponse) {
            this.sessionResponse = sessionResponse;
            setupView();
        }

        /**
         * The method that will initialize all the UI components
         * @param itemView the parent of the UI components
         */
        private void initializeUIComponents(@NonNull View itemView) {
            textTitle = itemView.findViewById(R.id.text_view_title);
            textDate = itemView.findViewById(R.id.text_view_date);
            textAddedBy = itemView.findViewById(R.id.text_view_added_by);
            textCode = itemView.findViewById(R.id.text_view_code);
            buttonViewMore = itemView.findViewById(R.id.button_view_more);
            buttonGenerateCode = itemView.findViewById(R.id.button_generate_code);
        }

        /**
         * The method that will populate the UI components with their corresponding values
         */
        private void setupView() {
            textTitle.setText(sessionResponse.getName());
            textDate.setText(sessionResponse.getVoteStart() + " - " + sessionResponse.getVoteEnd());
            textAddedBy.setText("Added by: " + sessionResponse.getUser().getFullName());
            textCode.setText(sessionResponse.getCode().getCode());

            buttonViewMore.setOnClickListener(new ViewMoreOnClickListener(sessionResponse));
            buttonGenerateCode.setOnClickListener(new GenerateCodeOnClickListener(sessionResponse));
        }

        /**
         * The class interface for the view more click listener
         */
        private final class ViewMoreOnClickListener implements View.OnClickListener {

            private SessionResponse sessionResponse;

            /**
             * Class Constructor
             * @param sessionResponse the session response associated to this class
             */
            public ViewMoreOnClickListener(SessionResponse sessionResponse) {
                this.sessionResponse = sessionResponse;
            }

            @Override
            public void onClick(View v) {
                listener.onClickViewMore(sessionResponse);
            }
        }

        /**
         * The class interface for the generate code click listener
         */
        private final class GenerateCodeOnClickListener implements View.OnClickListener {

            private SessionResponse sessionResponse;

            /**
             * Class Constructor
             * @param sessionResponse the session response associated to this class
             */
            public GenerateCodeOnClickListener(SessionResponse sessionResponse) {
                this.sessionResponse = sessionResponse;
            }

            @Override
            public void onClick(View v) {
                listener.onClickGenerateCode(sessionResponse);
            }
        }
    }

    /**
     * The interface for the session adapter
     */
    public interface ISessionAdapter {
        void onClickGenerateCode(SessionResponse sessionResponse);
        void onClickViewMore(SessionResponse sessionResponse);
    }
}
