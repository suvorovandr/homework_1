package com.example.homework;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class mainFragment extends Fragment {

    private ArrayList<Integer> data;
    private Activity activity;
    private String dataKey = "data";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(dataKey,data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.list);

        data = new ArrayList<>(); // Пустой массив

        // Заполняем
        if (savedInstanceState != null) data = savedInstanceState.getIntegerArrayList(dataKey);
        else for (int i = 1; i <= 100; i++) data.add(i);

        int colCount; // кол-во ячеек
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            colCount = 3;
        else colCount = 4;

        if (recyclerView != null) { // adapter creating
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), colCount, RecyclerView.VERTICAL, false));
            MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(data);
            recyclerView.setAdapter(adapter);
        }

        Button btn = view.findViewById(R.id.Add);
        btn.setOnClickListener(new View.OnClickListener() {// new element after click
            @Override
            public void onClick(View v) {
                data.add(data.size() + 1);
                recyclerView.getAdapter().notifyItemInserted(data.size()-1);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }


    class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Integer> data;

        MyRecyclerViewAdapter(ArrayList<Integer> data) { // constructor
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // viewHolder creating
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.number.setText(String.valueOf(position + 1));

            if (position % 2 == 0) holder.number.setTextColor(Color.BLUE);
            else holder.number.setTextColor(Color.RED);

            holder.number.setOnClickListener(new View.OnClickListener() { // click on element
                @Override
                public void onClick(View v) {
                    TextView digit = v.findViewById(v.getId());
                    int number = Integer.parseInt((String) digit.getText());
                    ((connectionInterface) activity).onItemClick(number);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView number;

            ViewHolder(View itemView) {
                super(itemView);
                number = itemView.findViewById(R.id.info_text);

            }
        }


    }
}

