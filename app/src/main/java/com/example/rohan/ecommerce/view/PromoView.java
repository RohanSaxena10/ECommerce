package com.example.rohan.ecommerce.view;

/**
 * Created by Rohan on 11/10/17.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.instantsearch.ui.views.AlgoliaHitView;
import com.example.rohan.ecommerce.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.rohan.ecommerce.view.PriceView.SYMBOL_MONEY;

public class PromoView extends TextView implements AlgoliaHitView {
    private final Context context;

    public PromoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    @SuppressLint("DefaultLocale") /* formatting only used for rounding numbers */
    public void onUpdateView(JSONObject result) {
        try {
            double salePrice = result.getDouble("salePrice");
            double promoPrice = result.getDouble("promoPrice");

            if (salePrice == promoPrice) {
                setVisibility(GONE);
            } else {
                setVisibility(VISIBLE);

                double diffPrice = salePrice - promoPrice;
                double percentSaving = 100 * diffPrice / salePrice;
                String diffText = " " + SYMBOL_MONEY + String.format("%.2f", diffPrice);
                String percentText = " (" + Math.round(percentSaving) + "%)";

                final String savings = "Savings";
                final int savingsLength = savings.length();

                final SpannableStringBuilder finalText = new SpannableStringBuilder(savings);
                finalText.append(diffText);
                finalText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrice)),
                        savingsLength, savingsLength + diffText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                finalText.append(percentText);

                setText(finalText, BufferType.SPANNABLE);
            }

        } catch (JSONException e) {
            Toast.makeText(context, "Error parsing result:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
