package com.example.sdaassign32022;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/*
 * A simple {@link Fragment} subclass.
 * @author Chris Coughlan 2019
 */
public class ProductList extends Fragment  implements FlavorViewAdapter.OnItemListener  {

    private static final String TAG = "RecyclerViewActivity";
    private ArrayList<TSDesignAdapter> mDesign = new ArrayList<>();

    public ProductList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_product_list, container, false);
        // Create an ArrayList of AndroidFlavor objects
        mDesign.add(new TSDesignAdapter("Skull", "M-L-XL", R.drawable.skull));
        mDesign.add(new TSDesignAdapter("Cat", "M-L-XL-XXL", R.drawable.cat));
        mDesign.add(new TSDesignAdapter("Guitar", "XS-M-L-XL", R.drawable.guitar));
        mDesign.add(new TSDesignAdapter("Mushrooms", "S-M-L-XL", R.drawable.mushrooms));
        mDesign.add(new TSDesignAdapter("Weapons", "M-L-XL", R.drawable.weapons));
        mDesign.add(new TSDesignAdapter("Castle", "M-L-XL", R.drawable.castle));
        mDesign.add(new TSDesignAdapter("Baphomet", "S-M-XL", R.drawable.baphomet));
        mDesign.add(new TSDesignAdapter("Space", "M-L-XL", R.drawable.space));
        mDesign.add(new TSDesignAdapter("Samurai", "M-L-XL", R.drawable.samurai));
        mDesign.add(new TSDesignAdapter("Moon", "M-L-XL", R.drawable.moon));

        //start it with the view
        Log.d(TAG, "Starting recycler view");
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_view);
         //Passing also this to constructor as it contains implementation of interface
        FlavorViewAdapter recyclerViewAdapter = new FlavorViewAdapter(getContext(), mDesign, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }


    @Override
    public void onItemClick(int position) {
        // getting the item that was clicked thanks to its position in the list
        TSDesignAdapter clickedItem = mDesign.get(position);
        showToast(this.getView(), clickedItem);
    }

    public void showToast(View view, TSDesignAdapter clickedItem )
    {
        String version = clickedItem.getVersionName();
        CharSequence text = version;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText( this.getContext(), text, duration);
        toast.show();
    }
}
