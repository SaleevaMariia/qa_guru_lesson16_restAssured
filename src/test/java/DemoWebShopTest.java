import io.restassured.path.xml.XmlPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class DemoWebShopTest {
    private String AUTH_COOKIE="NOPCOMMERCE.AUTH=651EEB2B029886F2E2D28A2FBE8B38FF647A33ABB80B6AC2E31F8D539" +
            "AE06C25E25D644ECC447CC8CE8D1D67C52F745A67A6511B7C106420EA0DADA8310583EB34B85CA93DF5FA22AADDC081" +
            "C1B5DA41F72B7D5B0B9B70C635A5C977300FA769CA67650C57C09193CE3FA7C178895E64D01AF98E6BFB84C254BD67C2D5" +
            "7BABA0D04F38CC0DE4F52764FE2F5BF6D19363CB88D8AC125EE7893CFC05E93392783E;)";

    @Test
    void unauthorizedUserAddProductToWishList(){
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_71_9_15=46" +
                        "&product_attribute_71_10_16=5" +
                        "&product_attribute_71_11_17=51" +
                        "&addtocart_71.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/71/2")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is("(1)"));
    }

    @Test
    void authorizedUserAddProductToWishList(){

        XmlPath getResponse = given()
                .cookie(AUTH_COOKIE)
                .get("http://demowebshop.tricentis.com/create-it-yourself-jewelry")
                .then()
                .extract().response().htmlPath();
        String wishListQtyStr = getResponse.getString("**.find { it.@class == 'wishlist-qty' }");
        int wishListQty = Integer.parseInt(wishListQtyStr.substring(1, wishListQtyStr.length()-1));

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_71_9_15=46" +
                        "&product_attribute_71_10_16=5" +
                        "&product_attribute_71_11_17=51" +
                        "&addtocart_71.EnteredQuantity=1")
                .cookie(AUTH_COOKIE)
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/71/2")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is("(" + ++wishListQty + ")"));
    }
}
