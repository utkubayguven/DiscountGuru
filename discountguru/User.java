import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.Period;

public class User extends Person {
    private LocalDate membershipDuration;

    @SerializedName("affiliate")
    private boolean isAffiliate;
    private String card;
    private List<Product> productsBasket;
    private double totalAmount;

    public User(String username, String password, String firstName, String lastName, String email, String phone, LocalDate membershipDuration, boolean isAffiliate) {
        super(username, password, firstName, lastName, email, phone);
        this.membershipDuration = membershipDuration;
        this.isAffiliate = isAffiliate;
        this.card = card;
        productsBasket = null;
        this.totalAmount = 0.0;
    }

    public LocalDate getMembershipDuration() {
        return this.membershipDuration;
    }

    public void setMembershipDuration(LocalDate membershipDuration) {
        this.membershipDuration = membershipDuration;
    }

    public boolean getAffiliate() {
        return this.isAffiliate;
    }

    public void setAffiliate(boolean isAffiliate) {
        this.isAffiliate = isAffiliate;
    }

    public String getCard() {
        return this.card;
    }

    public void setCard(String card) {
        this.card = card;
    }


    public List<Product> getProductsBasket() { return productsBasket; }

    //This  is a customized adapter class for JSON serialization and deserialization of LocalDate objects.
    private static class LocalDateAdapter implements JsonDeserializer<LocalDate>,
            JsonSerializer<LocalDate> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String dateStr = json.getAsString();
            return LocalDate.parse(dateStr, formatter);
        }
        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            String formattedDate = date.format(formatter);
            return new JsonPrimitive(formattedDate);
        }
    }

    //that is used to write a list of User objects to a JSON file.
    public static void writeUsersToJson(String filename, List<User> users) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .setPrettyPrinting()
                    .create();

            JsonObject jsonData = new JsonObject();
            JsonArray jsonUsers = new JsonArray();
            jsonData.add("users", jsonUsers);

            for (User user : users) {
                JsonObject userJson = gson.toJsonTree(user).getAsJsonObject();
                jsonUsers.add(userJson);
            }

            try (FileWriter writer = new FileWriter(filename)) {
                gson.toJson(jsonData, writer);
            }

            System.out.println("User data successfully written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This code block defines a method named loadUsersFromJson that is used to load User objects from a JSON file.
    public static List<User> loadUsersFromJson(String filename) {
        List<User> users = new ArrayList<>();

        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(new FileReader(filename)).getAsJsonObject();
            JsonArray jsonUsers = jsonObject.getAsJsonArray("users");

            for (JsonElement jsonElement : jsonUsers) {
                User user = gson.fromJson(jsonElement, User.class);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    

    // this method returns the total amount
    public double getTotalAmount() {
        return totalAmount;
    }

    //The purpose of this method is to validate the username and password by comparing them with the stored credentials of the current user.
    public static boolean isValidCredentials(String username, String password, User currentUser) {
        if (currentUser.getUsername().equals(username) && currentUser.getPassword().equals(password)) {
            return true;
        }
        return false;
    }


    //The purpose of this method is to calculate the number of years of membership duration based on the difference between the current date and the membership start date.
    public int calculateMembershipDuration() {
        LocalDate currentDate = LocalDate.now();
        LocalDate membershipDate = getMembershipDuration();

        Period period = Period.between(membershipDate, currentDate);
        int years = period.getYears();

        return years;
    }


    //The purpose of this method is to determine whether a user is considered a long-term customer based on their membership duration.
    public boolean isLongTermCustomer() {
        int year = calculateMembershipDuration();
        if(year >= 2) { return true; }
        else return false;
    }


    public int getDiscountPercentage() {
        int totalDiscountPercent = 0;

        if(getCard().equals("Gold")) {
            totalDiscountPercent += 30;// If the user has a "Gold" card, add 30 to the total discount percentage
        } else if(getCard().equals("Silver")) {
            totalDiscountPercent += 20;// If the user has a "Silver" card, add 20 to the total discount percentage

        }
        if(getAffiliate()) {
            totalDiscountPercent += 10;// If the user is an affiliate, add 10 to the total discount percentage
        }

        if(isLongTermCustomer()) {
            totalDiscountPercent += 5;// If the user is a long-term customer, add 5 to the total discount percentage
        }
        return totalDiscountPercent;
    }


    //This method is used to add a product to the basket, and it safely handles the case when the basket has not been created yet.
    public void addToBasket(Product product) {
        if(productsBasket == null) {
            productsBasket = new ArrayList<Product>();// If the basket has not been created yet, it creates a new ArrayList
        }
        productsBasket.add(product);
        totalAmount += product.getPrice();// Updates the total amount by adding the price of the product
    }


    //This method checks the content of the basket. If the basket is empty, it prints the message "Your basket is empty." to the console. 
    //If the basket is not empty, it prints the name, price, and total amount of each product to the console. 
    //The list of products in the basket is displayed with their corresponding serial numbers.
    public void viewBasket() {
        if (productsBasket == null) {
            System.out.println("Your basket is empty.");
        } else {
            System.out.println("Your basket contains:");
            for (int i = 0; i < productsBasket.size(); i++) {
                Product product = productsBasket.get(i);
                System.out.println((i + 1) + ". " + product.getName() + " - " + product.getPrice());
            }
            System.out.println("Total amount: " + getTotalAmount());
        }
    }

    //This method allows removing a product from the basket based on an order number.
    public boolean removeFromBasket(int orderNumber) {
        if (orderNumber >= 1 && orderNumber <= productsBasket.size()) {
            Product productToRemove = productsBasket.get(orderNumber - 1);
            productsBasket.remove(productToRemove);
            totalAmount -= productToRemove.getPrice();
            return true;
        } else {
            return false;
        }
    }

    
    //This method is used to display the membership details of a user on the console.
    public void viewMembershipDetails() {
        System.out.println("---------------------------------------------------------");
        System.out.println("Membership Details:");
        System.out.println("Username: " + getUsername());
        System.out.println("First Name: " + getFirstName());
        System.out.println("Last Name: " + getLastName());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone: " + getPhone());
        System.out.println("Membership Duration: " + getMembershipDuration());
        System.out.println("Affiliate Status: " + getAffiliate());
        System.out.println("Card: " + getCard());
        System.out.println("---------------------------------------------------------");
    }

    //This method lists users whose usernames contain a specified substring(input) and displays their membership details.
    public void listUsersWithUsername(String userName){
        if(getUsername().toLowerCase().contains(userName.toLowerCase())) {
            viewMembershipDetails();
        }
    }

    //this method takes new values for the username, password, first name, last name, email, phone number, membership duration, affiliate status, and card information as input parameters.
    public void updateUser(String newUsername, String newPassword, String newFirstName, String newLastName, String newEmail, String newPhone, String newMembershipDuration, boolean newAffiliate, String newCard) {
        setUsername(newUsername);
        setPassword(newPassword);
        setFirstName(newFirstName);
        setLastName(newLastName);
        setEmail(newEmail);
        setPhone(newPhone);
        setMembershipDuration(LocalDate.parse(newMembershipDuration));
        setAffiliate(newAffiliate);
        setCard(newCard);
    }
    

}

