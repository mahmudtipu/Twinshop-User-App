package store.twinshop.virtualmarket;

public class PendingDetail {
    String foodName,foodPrice,foodQuantities,location,mobileNumber,userName;

    public PendingDetail() {
    }

    public PendingDetail(String foodName, String foodPrice, String foodQuantities, String location, String mobileNumber, String userName) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantities = foodQuantities;
        this.location = location;
        this.mobileNumber = mobileNumber;
        this.userName = userName;
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

    public String getFoodQuantities() {
        return foodQuantities;
    }

    public void setFoodQuantities(String foodQuantities) {
        this.foodQuantities = foodQuantities;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
