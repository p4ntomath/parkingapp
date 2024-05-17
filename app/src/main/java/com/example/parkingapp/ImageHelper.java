package com.example.parkingapp;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageHelper {
    Context context;
    List<Integer> Left = new ArrayList<>();
    List<Integer> Right = new ArrayList<>();


    public ImageHelper(Context context) {
        this.context = context;
        this.Left = getLeftImages(context);
        this.Right = getRightImages(context);
    }





        public int getRandomLeftImage(){
            Random random = new Random();
            int index = random.nextInt(Left.size());
            int image = Left.get(index) == null ? R.drawable.cartopviewleft : Left.get(index);
            return image;
        }
        public int getRandomRightImage(){
            Random random = new Random();
            int index = random.nextInt(Right.size());
            int image = Right.get(index) == null ? R.drawable.cartopviewright : Right.get(index);
            return image;
        }
        public static ArrayList<Integer> getLeftImages (Context context){
            ArrayList<Integer> images = new ArrayList<>();
            Resources resources = context.getResources();
            TypedArray typedArray = resources.obtainTypedArray(R.array.leftCars);
            for (int i = 0; i < typedArray.length(); i++) {
                images.add(typedArray.getResourceId(i, 0));
            }
            typedArray.recycle();
            return images;
        }
    public static ArrayList<Integer> getRightImages(Context context) {
        ArrayList<Integer> images = new ArrayList<>();
        Resources resources = context.getResources();
        TypedArray typedArray = resources.obtainTypedArray(R.array.rightCars);
        for (int i = 0; i < typedArray.length(); i++) {
            images.add(typedArray.getResourceId(i, 0));
        }
        typedArray.recycle();
        return images;
    }

}