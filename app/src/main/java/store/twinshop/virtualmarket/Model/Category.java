package store.twinshop.virtualmarket.Model;

public class Category {
    private String Name;
    private String Image;
    private String Location;
    private String Availability;

    public Category() {
    }

    public Category(String name, String image, String location, String availability) {
        Name = name;
        Image = image;
        Location = location;
        Availability = availability;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }
}
