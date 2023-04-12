package com.example.newyorktimesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.newyorktimesapp.R;

public class ArticleInfoFragment extends Fragment {

    private static final String ARG_HEADLINE = "headline";
    private static final String ARG_PUBLICATION_DATE = "publication_date";

    public ArticleInfoFragment() {
        // Required empty public constructor
    }

    public static ArticleInfoFragment newInstance(String headline, String publicationDate) {
        ArticleInfoFragment fragment = new ArticleInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HEADLINE, headline);
        args.putString(ARG_PUBLICATION_DATE, publicationDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_info, container, false);

        TextView headlineTextView = view.findViewById(R.id.article_info_title);
        TextView publicationDateTextView = view.findViewById(R.id.article_info_publication_date);

        if (getArguments() != null) {
            headlineTextView.setText(getArguments().getString(ARG_HEADLINE));
            publicationDateTextView.setText(getArguments().getString(ARG_PUBLICATION_DATE));
        }

        // Add the close button functionality
        Button closeButton = view.findViewById(R.id.article_info_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }
}
