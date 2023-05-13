package com.example.android_homeassignment2.recycleview;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.example.android_homeassignment2.UserViewHandler;
import com.example.android_homeassignment2.entities.User;

public class UserAdapter extends Adapter<User> {
    private int positionToMark = -1;

    public UserAdapter(@LayoutRes int itemLayoutId, Context context) {
        super(itemLayoutId, context);
    }

    public void setPositionToMark(int positionToMark) {
        this.positionToMark = positionToMark;
    }

    @Override
    protected Adapter<User>.ViewHolder createViewHolder(View view) {
        return new UserViewHolder(view);
    }

    public class UserViewHolder extends ViewHolder {
        private UserViewHandler userViewHandler;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void showData(User data) {
            int position = getAdapterPosition();
            userViewHandler.showUserData(data, position + 1);
            if (position == positionToMark)
                userViewHandler.toMarkedUserStyle();
        }

        @Override
        protected void findViews(View itemView) {
            userViewHandler = new UserViewHandler();
            userViewHandler.findViews(itemView);
        }
    }
}
