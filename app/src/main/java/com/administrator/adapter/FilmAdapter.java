package com.administrator.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.bean.Film;
import com.administrator.film.R;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ViewHolder> {

    private List<Film> films;

    public FilmAdapter(List<Film> films) {
        this.films = films;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filebean, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Film film = films.get(i);
        viewHolder.name.setText(film.getName());
        viewHolder.path.setText(film.getPath());
        viewHolder.pw.setText(film.getPassword());
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView name;
        AppCompatTextView path;
        AppCompatTextView pw;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            path = itemView.findViewById(R.id.path);
            pw = itemView.findViewById(R.id.pw);
        }
    }

    public void AddHeaderItem(List<Film> items){
        Log.d("TAG", "下拉刷新!");
        films.addAll(0, items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<Film> items){
        Log.d("TAG", "上拉刷新!");
        films.addAll(items);
        notifyDataSetChanged();
    }

}
