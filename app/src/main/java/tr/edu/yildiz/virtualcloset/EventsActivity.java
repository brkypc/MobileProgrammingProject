package tr.edu.yildiz.virtualcloset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import tr.edu.yildiz.virtualcloset.Adapters.ClothesAdapter;
import tr.edu.yildiz.virtualcloset.Adapters.DrawerAdapter;
import tr.edu.yildiz.virtualcloset.Adapters.EventAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Drawer;
import tr.edu.yildiz.virtualcloset.Model.Event;

public class EventsActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ArrayList<Event> events;

    RecyclerView rvEvents;
    EventAdapter eventAdapter;
    TextView noEvent;

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        databaseHelper = new DatabaseHelper(this);
        events = databaseHelper.getEvents();

        defineAddEvent();

        if(events != null) {
            defineRv();
            swipeToRefresh();
        }
        else {
            noEvent = findViewById(R.id.noEvent);
            noEvent.setVisibility(View.VISIBLE);
        }
    }

    private void swipeToRefresh() {
        refreshLayout = findViewById(R.id.refreshEvents);
        refreshLayout.setOnRefreshListener(() -> {
           eventAdapter.dataChanged();
           refreshLayout.setRefreshing(false);
        });
    }

    private void defineRv() {
        rvEvents = findViewById(R.id.rvEvents);
        rvEvents.setHasFixedSize(true);
        //recyclerView.setItemViewCacheSize(drawers.size());

        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        rvEvents.addItemDecoration(decoration);

        eventAdapter = new EventAdapter(this, events);
        rvEvents.setAdapter(eventAdapter);

        Animation recycler_animation = AnimationUtils.loadAnimation(this, R.anim.recycler_animation);
        rvEvents.startAnimation(recycler_animation);
    }

    private void defineAddEvent() {
        ImageButton addEvent = findViewById(R.id.addEvents);
        addEvent.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEventActivity.class);
            startActivity(intent);
        });
    }
}