package store.twinshop.virtualmarket;

/**
 * Created by Tipu on 3/14/2018.
 */

public class ItemDetail {
    String FoodName;
    String Price;
    String CategoryId;
    String ImageLink;
    String Description;
    String SearchTag;
    String WsRate;
    String av;

    public ItemDetail(){

    }

    public ItemDetail(String FoodName, String Price, String CategoryId, String ImageLink, String Description, String SearchTag, String WsRate, String av) {
        this.FoodName = FoodName;
        this.Price = Price;
        this.CategoryId = CategoryId;
        this.ImageLink = ImageLink;
        this.Description = Description;
        this.SearchTag = SearchTag;
        this.WsRate = WsRate;
        this.av = av;
    }

    public String getFoodName() {
        return FoodName;
    }

    public String getPrice() {
        return Price;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public String getDescription() {
        return Description;
    }

    public String getSearchTag() {
        return SearchTag;
    }

    public String getWsRate() {
        return WsRate;
    }

    public String getAv() {
        return av;
    }
}
