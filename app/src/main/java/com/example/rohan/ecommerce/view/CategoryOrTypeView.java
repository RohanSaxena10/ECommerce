package com.example.rohan.ecommerce.view;

/**
 * Created by Rohan on 11/10/17.
 */
import android.content.Context;
import android.text.Spannable;
import android.util.AttributeSet;

import com.algolia.instantsearch.helpers.Highlighter;
import com.algolia.instantsearch.ui.views.AlgoliaHitView;

import org.json.JSONException;
import org.json.JSONObject;


public class CategoryOrTypeView extends NotNullView implements AlgoliaHitView {
    public CategoryOrTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onUpdateView(JSONObject result) {
        try {
            String category = result.getString("category");
            final String attributeToHighlight = !isNull(category) ? "category" : "type";

            final Spannable highlightedAttribute = Highlighter.getDefault().renderHighlightColor(result, attributeToHighlight, getContext());
            setText(highlightedAttribute);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
