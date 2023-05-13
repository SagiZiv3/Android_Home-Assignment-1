package com.example.android_homeassignment2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_homeassignment2.R;
import com.example.android_homeassignment2.UserViewHandler;
import com.example.android_homeassignment2.entities.User;
import com.example.android_homeassignment2.recycleview.UserAdapter;

import java.util.Collection;

public class HighScoresFragment extends Fragment {
    private ItemSelectedCallback selectedCallback;
    private UserAdapter userAdapter;
    private RecyclerView listView;
    private View emptyTablePrompt;
    private FrameLayout currentUserView;
    private UserViewHandler userViewHandler;
    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_scores, container, false);
        initializeListView(view);

        emptyTablePrompt = view.findViewById(R.id.list_LBL_emptyTable);

        initializeCurrentUserView(inflater, view);

        registerCallbacks();
        return view;
    }

    public void scrollTo(int position) {
        listView.smoothScrollToPosition(position);
    }

    public void setSelectedCallback(ItemSelectedCallback selectedCallback) {
        this.selectedCallback = selectedCallback;
    }

    public void showCurrentUser(@NonNull User user) {
        currentUserView.setVisibility(View.VISIBLE);
        userViewHandler.showUserData(user, 11);
        userViewHandler.toMarkedUserStyle();
        currentUser = user;
    }

    public void hideCurrentUserView() {
        currentUserView.setVisibility(View.GONE);
    }

    public void showUsers(Collection<User> users, int positionToMark) {
        if (users.size() > 0) {
            userAdapter.setPositionToMark(positionToMark);
            userAdapter.addItems(users);
        } else {
            emptyTablePrompt.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    private void registerCallbacks() {
        userAdapter.setItemSelectedCallback(position -> {
            if (selectedCallback != null)
                selectedCallback.onItemSelected(position, userAdapter.getItem(position));
        });
        userViewHandler.setOnClickListener(v -> {
            if (selectedCallback != null)
                selectedCallback.onItemSelected(11, currentUser);
        });
    }

    private void initializeCurrentUserView(@NonNull LayoutInflater inflater, @NonNull View view) {
        userViewHandler = new UserViewHandler();
        currentUserView = view.findViewById(R.id.list_LAY_currentUser);
        View v = inflater.inflate(R.layout.list_user, currentUserView, true);
        userViewHandler.findViews(v);
        userViewHandler.toMarkedUserStyle();
        currentUserView.setVisibility(View.INVISIBLE);
    }

    private void initializeListView(@NonNull View view) {
        listView = view.findViewById(R.id.list_LST_names);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userAdapter = new UserAdapter(R.layout.list_user, view.getContext());
        listView.setAdapter(userAdapter);
    }

    public interface ItemSelectedCallback {
        void onItemSelected(int itemIndex, User selectedItem);
    }
}
