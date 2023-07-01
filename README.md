# DiscountGuru Shopping System Project
Welcome to the DiscountGuru Shopping System Project. This application, built with Java, simulates a simple online shopping system where users and administrators can perform various operations. Users can explore the product catalog, add items to their basket, and manage their basket items, while administrators have the ability to manage products and users. Data is stored in JSON files using Google's Gson library, providing persistence across multiple uses of the application.

## Features
- **User Login:** Users can login with their predefined credentials.
![User Console](images/user.jpg)
- **Product Browsing and Management:** Users can view the product catalog, which includes details like the product name, category, and price. They can add desired products to their basket, view their basket's contents, and remove items from their basket as needed.
- **Admin Login and Management:** Admins have the ability to login with their predefined credentials. They can perform operations like adding, updating, and removing products. Furthermore, they can view a list of all users and their details.
![Admin Console](images/admin.jpg)

## Structure
The DiscountGuru Shopping System consists of the following main files:
- `Person.java`: This class represents a person with attributes such as username, password, first name, last name, email, and phone number.
- `User.java`: Extending the Person class, this class represents a user in the system. It includes additional properties such as a basket to hold products, as well as methods for adding and removing items from the basket, calculating discount percentages based on user status, validating user credentials, checking if a user is a long-term customer, and writing or reading user data to or from a JSON file.
- `Admin.java`: Also an extension of the Person class, this class represents an admin in the system. In addition to the base class properties, it includes methods to add, remove, and update products and users, as well as reading or writing admin data to or from a JSON file.
- `Product.java`: The Product class in Java represents a product with attributes such as id, name, category, and price. It includes methods for loading product data from a JSON file, displaying product details, updating product properties, allowing user selection, searching for products, and writing product data back to a JSON file.
-Main.java: This is the driver class that includes the main method to run the application. It's responsible for creating the user interface, handling user interactions, and orchestrating the overall execution of the program.
- `products.json`: This JSON file stores a list of  products.
- `users.json`: This JSON file stores a list of users.
- `admins.json`: This JSON file stores a list of adminis.

## How to Run
1. Make sure Java Development Kit (JDK) is installed on your system. If not, download and install it from the official Oracle website.
2. This project uses the Gson library to parse and generate JSON. You need to download this library and add it to your project. You can find the Gson library on the official Gson GitHub repository. After downloading, you can add it to your project following the instructions specific to your IDE.
3. Compile and run the `Main.java` file to initiate the application. You will be prompted to log in as a user or an admin. Once logged in, you can navigate through the system and perform various operations according to your user type.
