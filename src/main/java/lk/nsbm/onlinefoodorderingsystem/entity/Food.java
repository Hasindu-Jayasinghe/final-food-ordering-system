package lk.nsbm.onlinefoodorderingsystem.entity;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Food {
    @Id
    private int foodId;
    private String foodName;
    private double price;
    @Lob
    private String image;
    @ManyToOne
    @Cascade(value = CascadeType.DELETE)
    private FoodTitle foodTitle;
    @ManyToOne
    @Cascade(value = CascadeType.DELETE)
    private Restaurant restaurantList;

    public Food() {
    }

    public Food(int foodId) {
        this.foodId = foodId;
    }

    public Food(int foodId, String foodName, double price, String image, FoodTitle foodTitle) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.image = image;
        this.foodTitle = foodTitle;
    }

    public Food(int foodId, String foodName, double price, FoodTitle foodTitle, Restaurant restaurantList) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
        this.foodTitle = foodTitle;
        this.restaurantList = restaurantList;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Restaurant getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(Restaurant restaurantList) {
        this.restaurantList = restaurantList;
    }

    public FoodTitle getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(FoodTitle foodTitle) {
        this.foodTitle = foodTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
