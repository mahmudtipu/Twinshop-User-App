package store.twinshop.virtualmarket;

public class Detail {
    String foodName,foodPrice,foodQuantity,restaurantName, wsRate;

    public Detail() {
    }

    public Detail(String foodName, String foodPrice, String foodQuantity, String restaurantName, String wsRate) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
        this.restaurantName = restaurantName;
        this.wsRate = wsRate;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getWsRate() {
        return wsRate;
    }

    public void setWsRate(String wsRate) {
        this.wsRate = wsRate;
    }
}
