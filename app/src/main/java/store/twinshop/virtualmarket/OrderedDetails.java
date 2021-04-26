package store.twinshop.virtualmarket;

public class OrderedDetails {
    String foodName, userName, foodQuantities, foodPrice, mobileNumber, location,wsRate;

    public OrderedDetails() {

    }

    public OrderedDetails(String foodName, String userName, String foodQuantities, String foodPrice, String mobileNumber, String location, String wsRate) {
        this.foodName = foodName;
        this.userName = userName;
        this.foodQuantities = foodQuantities;
        this.foodPrice = foodPrice;
        this.mobileNumber = mobileNumber;
        this.location = location;
        this.wsRate = wsRate;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFoodQuantities() {
        return foodQuantities;
    }

    public void setFoodQuantities(String foodQuantities) {
        this.foodQuantities = foodQuantities;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWsRate() {
        return wsRate;
    }

    public void setWsRate(String wsRate) {
        this.wsRate = wsRate;
    }
}
