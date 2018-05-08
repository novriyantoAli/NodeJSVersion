package net.ali.rhein.mvpbase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import net.ali.rhein.mvpbase.R;
import net.ali.rhein.mvpbase.models.ListOutletData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhein on 4/3/18.
 */

public class ListOutletAdapter extends RecyclerView.Adapter<ListOutletAdapter.MyHolder> implements Filterable {

    private Context context;
    private int rowLayout;
    private LayoutInflater inflater;
    private List<ListOutletData> listOutletDataList, newList;

    int currentPos = 0;

    public ListOutletAdapter(List<ListOutletData> data, int rowLayout, Context context){
        this.context                  = context;
        this.inflater                 = LayoutInflater.from(context);
        this.listOutletDataList       = data;
        this.rowLayout                = rowLayout;
        this.newList                  = data;
    }

    @Override
    public ListOutletAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textViewNamaProvinsi.setText(listOutletDataList.get(position).getNamaProvinsi());
        holder.textViewNamaKabupaten.setText(listOutletDataList.get(position).getNamaKabupaten());
        holder.textViewTanggalPlan.setText("Nama Outlet : "+ listOutletDataList.get(position).getNamaOutlet());
    }

    @Override
    public int getItemCount() {
        return listOutletDataList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    listOutletDataList = newList;
                }
                else {
                    ArrayList<ListOutletData> tempCompList = new ArrayList<>();
                    for (ListOutletData listOutletData : newList) {

                        if (listOutletData.getNamaOutlet().toLowerCase().contains(charString)) {
                            tempCompList.add(listOutletData);

                        }
                    }
                    listOutletDataList = tempCompList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listOutletDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listOutletDataList = (ArrayList<ListOutletData>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }


    static class MyHolder extends RecyclerView.ViewHolder {

        TextView textViewNamaProvinsi, textViewNamaKabupaten, textViewTanggalPlan;

        MyHolder(View itemView) {
            super(itemView);
            textViewNamaProvinsi    = (TextView)itemView.findViewById(R.id.text_view_nama_provinsi);
            textViewNamaKabupaten   = (TextView)itemView.findViewById(R.id.text_view_nama_kabupaten);
            textViewTanggalPlan     = (TextView)itemView.findViewById(R.id.text_view_tanggal_plan);
        }
    }
}
