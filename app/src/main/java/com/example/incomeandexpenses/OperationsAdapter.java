package com.example.incomeandexpenses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OperationsAdapter extends RecyclerView.Adapter<OperationsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Operations> operations;
    Date date;

    OperationsAdapter(Context context, List<Operations> operations){
        this.operations = operations;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_operations, parent, false);
        return new ViewHolder(view);
    }

    private String getMonth(Calendar calendar){
        String tempReturn = "";
        int month = Integer.parseInt (String.valueOf(calendar.get(Calendar.MONTH)));
        if(month < 9){
            month++;
            tempReturn="0" + String.valueOf(month);
        } else if (month > 8 && month < 12) {
            tempReturn=String.valueOf(month++);
        }
        return tempReturn;
    }

    private String getDay(Calendar calendar){
        String tempReturn = "";
        int month = Integer.parseInt (String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        if(month < 10) {
        tempReturn="0" + month;
        }
        return tempReturn;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Operations operation = operations.get(position);
        holder.nameOperation.setText(String.valueOf(operation.getName()));
        holder.categoryOperation.setText(operation.getCategory());
        date = operation.getTimeStamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String tempTime = getDay(calendar) + "."+ getMonth(calendar) +"." +calendar.get(Calendar.YEAR);

        //String tempTime = operation.getTimeStamp().get(Calendar.DAY_OF_MONTH) + "." + GetMonth(position) + "." +  operation.getTimeStamp().get(Calendar.YEAR);

        holder.timestampOperation.setText(tempTime);
        if (operation.getTypeOperation()){
            holder.separator.setBackgroundColor(Color.parseColor("#008000"));
            holder.sumOperation.setTextColor(Color.parseColor("#008000"));
            holder.sumOperation.setText("+ " + operation.getSum());
        }
        else if (!operation.getTypeOperation()) {
            holder.separator.setBackgroundColor(Color.parseColor("#ff0000"));
            holder.sumOperation.setTextColor(Color.parseColor("#ff0000"));
            holder.sumOperation.setText("- " + operation.getSum());
        }

    }

    @Override
    public int getItemCount() {
        return operations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameOperation, categoryOperation, sumOperation, timestampOperation;
        View separator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOperation = itemView.findViewById(R.id.nameOperationTextView);
            categoryOperation = itemView.findViewById(R.id.categoryOperationTextView);
            sumOperation = itemView.findViewById(R.id.sumOperationTextView);
            timestampOperation = itemView.findViewById(R.id.timeStampTextView);
            separator = itemView.findViewById(R.id.separatorOnTheLeft);


        }
    }
}
