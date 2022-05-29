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


/**
 * Адаптер для операция, создан для RecyclerView
 */
public class OperationsAdapter extends RecyclerView.Adapter<OperationsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Operations> operations;
    Date date;

    // Интерфейс для получения нажатого элемента
    interface OnOperationClickListener{
        void onOperationClick(Operations operation, int position);
    }

    private final OnOperationClickListener onClickListener;

    OperationsAdapter(Context context, List<Operations> operations, OnOperationClickListener onOperationClickListener){
        this.onClickListener = onOperationClickListener;
        this.operations = operations;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_operations, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Operations operation = operations.get(holder.getAdapterPosition());
        holder.nameOperation.setText(String.valueOf(operation.getName()));
        holder.categoryOperation.setText(operation.getCategory());
        date = operation.getTimeStamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String tempTime = getDay(calendar) + "."+ getMonth(calendar) +"." +calendar.get(Calendar.YEAR);

        holder.timestampOperation.setText(tempTime);
        if (operation.getTypeOperation()){
            holder.separator.setBackgroundColor(Color.parseColor("#008000"));
            holder.sumOperation.setTextColor(Color.parseColor("#008000"));
            holder.sumOperation.setText("+ " + operation.getSum() + " ₽");
        }
        else if (!operation.getTypeOperation()) {
            holder.separator.setBackgroundColor(Color.parseColor("#ff0000"));
            holder.sumOperation.setTextColor(Color.parseColor("#ff0000"));
            holder.sumOperation.setText("- " + operation.getSum() + " ₽");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onOperationClick(operation, holder.getAdapterPosition());
            }
        });

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

    /*
    Регион вспомогательный
     */

    // Месяц
    private String getMonth(Calendar calendar){
        String tempReturn = "";
        int month = Integer.parseInt (String.valueOf(calendar.get(Calendar.MONTH)));
        if(month < 9){
            month++;
            tempReturn="0" + String.valueOf(month);
        } else if (month < 12) {
            month++;
            tempReturn=String.valueOf(month);
        }
        return tempReturn;
    }

    // День
    private String getDay(Calendar calendar){
        String tempReturn = "";
        int month = Integer.parseInt (String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        if(month < 10) {
            tempReturn="0" + month;
        }
        else tempReturn = String.valueOf(month);
        return tempReturn;
    }
}
