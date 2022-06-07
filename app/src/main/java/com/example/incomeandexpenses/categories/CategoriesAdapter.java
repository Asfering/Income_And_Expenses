package com.example.incomeandexpenses.categories;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.incomeandexpenses.R;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Класс для RecyclerView с категориями
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    // Переменные
    private LayoutInflater inflater;
    Date date;

    // Списки
    private List<Categories> categories;

    // Интерфейс для получения нажатого элемента
    interface OnCategoryClickListener {
        void onCategoryClick(Categories categories, int position);
    }

    // Обработчик нажатия
    private final OnCategoryClickListener onClickListener;


    ///////////////////////////////////////////////// Регион основной


    // Конструктор
    CategoriesAdapter(Context context, List<Categories> categories, CategoriesAdapter.OnCategoryClickListener onOperationClickListener) {
        this.onClickListener = onOperationClickListener;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
    }

    // Передача вьюхи
    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_categories, parent, false);
        return new CategoriesAdapter.ViewHolder(view);
    }

    // Заполнение вьюхи
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories category = categories.get(holder.getAdapterPosition());          // Категория
        holder.nameCategory.setText(String.valueOf(category.getName()));            // Имя
        holder.sumCategory.setText(category.getSum() + " ₽");                       // Сумма

        // Проценты
        String percentage = new DecimalFormat("#0.00").format(category.getPercentage());
        holder.percentage.setText(percentage + " %");

        // Цвет
        switchColors(holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onCategoryClick(category, holder.getAdapterPosition());
            }
        });
    }

    // Количество элементов в RecyclerView
    @Override
    public int getItemCount() {
        return categories.size();
    }

    // Вьюха
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameCategory, sumCategory, percentage;
        View separator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategory = itemView.findViewById(R.id.nameCategory);
            sumCategory = itemView.findViewById(R.id.sumCategory);
            percentage = itemView.findViewById(R.id.percentageOfAll);
            separator = itemView.findViewById(R.id.separatorCategory);
        }
    }


    ///////////////////////////////////////////////// Конец региона


    ///////////////////////////////////////////////// Регион вспомогательный


    // Цвета категорий (можно потом заменить на strings)
    private void switchColors(ViewHolder holder) {
        switch (holder.nameCategory.getText().toString()) {
            case "Продукты":
                //Violet
                changingColors(holder,"#EE82EE");
                break;
            case "Общественный транспорт":
                //Sky blue
                changingColors(holder,"#87CEEB");
                break;
            case "Связь и интернет":
                //DARK CYAN
                changingColors(holder,"#008B8B");
                break;
            case "Косметика и уход":
                //thistle
                changingColors(holder,"#D8BFD8");
                break;
            case "Рестораны и кафе":
                //Orange
                changingColors(holder,"#FF4500");
                break;
            case "Лекарства":
                //CYAN
                changingColors(holder,"#00FFFF");
                break;
            case "Быт":
                //Yellow
                changingColors(holder,"#FFFF00");
                break;
            case "Квитанции":
                //Dark grey
                changingColors(holder,"#A9A9A9");
                break;
            case "Хобби":
                //SteelBlue
                changingColors(holder,"#4682B4");
                break;
            case "Одежда":
                //Fuchsia
                changingColors(holder,"#FF00FF");
                break;
            case "Строительные работы":
                //chocolate
                changingColors(holder,"#D2691E");
                break;
            case "Личный транспорт":
                //BLUE
                changingColors(holder,"#0000FF");
                break;
            case "Канцелярия":
                //indigo
                changingColors(holder,"#4B0082");
                break;
            case "Напитки":
                //Misty Rose
                changingColors(holder,"#FFE4E1");
                break;
            case "Развлечения":
                //Blue violet
                changingColors(holder,"#8A2BE2");
                break;
            case "Подарки":
                //GOLD
                changingColors(holder,"#FFD700");
                break;
            case "Прочее":
                //Silver
                changingColors(holder,"#C0C0C0");
                break;
            case "БезКатегории":
                //RED
                changingColors(holder,"#FF0000");
                break;
            case "Зарплата":
                //Green
                changingColors(holder,"#008000");
                break;
            case "Инвестиции":
                //LIME
                changingColors(holder,"#00FF00");
                break;
            case "Иждивение":
                //Green Yellow
                changingColors(holder,"#ADFF2F");
                break;
            case "Гос. пособия":
                //DodgerBlue
                changingColors(holder,"#1E90FF");
                break;
            case "Кэшбэк на покупки":
                //Aquamarine
                changingColors(holder,"#7FFFD4");
                break;
        }
    }

    // Вспомогательный войд для смены цвета категории
    private void changingColors(ViewHolder holder, String color){
        holder.separator.setBackgroundColor(Color.parseColor(color));
        holder.sumCategory.setTextColor(Color.parseColor(color));
    }


    ///////////////////////////////////////////////// Конец региона
}
