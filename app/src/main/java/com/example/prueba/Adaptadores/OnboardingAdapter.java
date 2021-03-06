package com.example.prueba.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prueba.Items.OnboardingItem;
import com.example.prueba.R;

import java.util.List;

public class OnboardingAdapter extends  RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>{
    //Declaramos los atributos
    private List<OnboardingItem> onboardingItems;
    //Método constructor
    public OnboardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }
    @NonNull
    @Override
   /*
    El RecyclerView llama a este método para reutilizar los ViewHolder creados,
    , es decir los infla con las nuevas filas visibles según el usuario desliza horinzontalmente.
     */
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onboarding, parent, false
                )
        );
    }
    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setImageOnBoarding(onboardingItems.get(position));
    }
    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder{
        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageOnBoarding;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageOnBoarding = itemView.findViewById(R.id.imageOnBoarding);
        }

        public void setImageOnBoarding(OnboardingItem onBoardingItem){
            textTitle.setText(onBoardingItem.getTitle());
            textDescription.setText(onBoardingItem.getDescription());
            imageOnBoarding.setImageResource(onBoardingItem.getImage());
        }
    }
}
