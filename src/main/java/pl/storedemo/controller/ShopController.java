package pl.storedemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.storedemo.entities.*;
import pl.storedemo.repositories.*;
import pl.storedemo.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ShopController {

    @Autowired
    ShopService shopService;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartItemServiceImpl cartItemService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    DelivererRepository delivererRepository;

    @Autowired
    DelivererServiceImpl delivererService;

    @GetMapping("/")
    public String gotToIndex(ModelMap model) {
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "index";
    }

    @GetMapping("/allStores")
    public String showAllStores(ModelMap model) {
        model.addAttribute("shops", shopService.getAllShops());
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "allStoresView";
    }

    @GetMapping("/allProducts")
    public String showAllProducts(ModelMap model) {
        model.addAttribute("products", productService.getlAllProducts());
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "allProductsView.html";
    }

    @GetMapping("/search")
    public String searchForCheapestProduct(ModelMap model,
                                           @RequestParam("productName") String productName,
                                           @RequestParam("city") String city,
                                           @RequestParam("amount") String amount) {
        String info = "Sorry,";
        String message = "but no such product found in chosen city :(";
        List<Product> emptyList = new ArrayList<>();
        if (productService.IfProductExistsByGivenProductName(productName, city)) {
            if (!productService.getProductsInStoresByProductNameAndCityAndAmount(productName, city, Integer.parseInt(amount)).isEmpty()) {
                message = "All search results: ";
                info = "Available products with lowest price:";
                model.addAttribute("searchedProducts", productService.getCheapestProductsByProductNameAndCityAndAmount(productName, city, Integer.parseInt(amount)));
                model.addAttribute("message", message);
                model.addAttribute("info", info);
                model.addAttribute("allProducts", productService.getProductsInStoresByProductNameAndCity(productName, city));
            } else {
                message = "All search results: ";
                info = "We couldn't find products matching your amount request. Please find some cheapest products we found that you might be interested in: ";
                model.addAttribute("searchedProducts", productService.getCheapestProductsByProductNameAndCity(productName, city));
                model.addAttribute("message", message);
                model.addAttribute("info", info);
                model.addAttribute("allProducts", productService.getProductsInStoresByProductNameAndCity(productName, city));
            }
        } else {
            model.addAttribute("searchedProducts", emptyList);
            model.addAttribute("message", message);
            model.addAttribute("info", info);
            model.addAttribute("allProducts", productService.getProductsInStoresByProductNameAndCity(productName, city));
        }
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "productWithMinPrice";
    }

    @GetMapping("/store")
    public String goToStore(ModelMap model, @RequestParam("id") String productId,
                            @RequestParam("productName") String productName,
                            @RequestParam("city") String city,
                            @RequestParam("amount") String amount) {
        Product product = productService.findProductById(Long.valueOf(productId));
        String welcome = "Welcome to " + product.getShop().getName();
        model.addAttribute("searchedProducts", productService.getCheapestProductsByProductNameAndCityAndAmount(productName, city, Integer.parseInt(amount)));
        model.addAttribute("allProducts", productService.getlAllProductsByStoreIdWithoutGivenProduct(product.getShop().getId(), product)); //without this product
        model.addAttribute("welcome", welcome);
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "store";
    }

    @GetMapping("/storeProduct")
    public String goToStoreProduct(ModelMap model, @RequestParam("id") String productId) {
        Product product = productService.findProductById(Long.valueOf(productId));
        String welcome = "Welcome to " + product.getShop().getName();
        model.addAttribute("welcome", welcome);
        model.addAttribute("allProducts", productService.getlAllProductsByStoreId(product.getShop().getId()));
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "store";
    }

    @GetMapping("/addToCart")
    public String addProductToCart(ModelMap model,
                                   @RequestParam("id") String productId,
                                   @RequestParam("amount") String amount) {
        Product product = productService.findProductById(Long.valueOf(productId));

        String message = "";
        if (cartItemService.getAllItemsFromCart().isEmpty()) {
            if (!productService.isAvailable(Long.valueOf(productId), Integer.parseInt(amount))) {
                message = "Product is unavailable. (amount)";
            } else {
                Optional<CartItem> any = cartItemService.getAllItemsFromCart().stream()
                        .filter(cartItem -> cartItem.getProduct().getId().equals(Long.valueOf(productId)))
                        .findAny();

                if (any.isEmpty()) {
                    CartItem cartItem = new CartItem(product, Integer.parseInt(amount));
                    cartItemService.createNewCartItem(cartItem);
                    productService.diminishProductAmountAvailable(product.getId(), Integer.parseInt(amount));
                    message = "Product added to cart.";
                } else {
                    CartItem cartItem = any.get();
                    productService.diminishProductAmountAvailable(product.getId(), Integer.parseInt(amount));
                    cartItem.setAmount(cartItem.getAmount() + Integer.parseInt(amount));
                    cartItem.setTotalPrice(cartItem.getAmount() * cartItem.getProduct().getPrice());
                    cartItemRepository.save(cartItem);
                    message = "Product amount increased";
                }
            }
        } else {
            if (cartItemService.checkIfShopExistsInCart(product.getShop().getId())) {
                if (!productService.isAvailable(Long.valueOf(productId), Integer.parseInt(amount))) {
                    message = "Product is unavailable. (amount)";
                } else {
                    Optional<CartItem> any = cartItemService.getAllItemsFromCart().stream()
                            .filter(cartItem -> cartItem.getProduct().getId().equals(Long.valueOf(productId)))
                            .findAny();

                    if (any.isEmpty()) {
                        CartItem cartItem = new CartItem(product, Integer.parseInt(amount));
                        cartItemService.createNewCartItem(cartItem);
                        productService.diminishProductAmountAvailable(product.getId(), Integer.parseInt(amount));
                        message = "Product added to cart.";
                    } else {
                        CartItem cartItem = any.get();
                        productService.diminishProductAmountAvailable(product.getId(), Integer.parseInt(amount));
                        cartItem.setAmount(cartItem.getAmount() + Integer.parseInt(amount));
                        cartItem.setTotalPrice(cartItem.getAmount() * cartItem.getProduct().getPrice());
                        cartItemRepository.save(cartItem);
                        message = "Product amount increased";
                    }
                }
            } else {
                message = "You cannot add product from store to cart.";
            }
        }
        String welcome = "Welcome to " + product.getShop().getName();
        model.addAttribute("welcome", welcome);
        model.addAttribute("allProducts", productService.getlAllProductsByStoreId(product.getShop().getId()));
        model.addAttribute("message", message);
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "store";
    }

    @GetMapping("/cart")
    public String addProductInCart(ModelMap model) {
        model.addAttribute("disable", cartItemService.getAllItemsFromCart().isEmpty());
        model.addAttribute("cart", cartItemService.getAllItemsFromCart());
        model.addAttribute("total", cartItemService.countTotalPriceInCart());
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "cartView";
    }

    @GetMapping("/add")
    public String addAmountOfProductInCart(ModelMap model, @RequestParam("id") String cartItemId) {
        CartItem cartItem = cartItemService.findById(Long.valueOf(cartItemId));

        Product product = productService.findProductById(cartItem.getProduct().getId());
        if (productService.isAvailable(product.getId())) {
            productService.diminishProductAmountAvailable(product.getId(), 1);
            cartItem.setAmount(cartItem.getAmount() + 1);
            cartItem.setTotalPrice(cartItemService.round(cartItem.getAmount() * cartItem.getProduct().getPrice()));
            cartItemRepository.save(cartItem);
        }
        model.addAttribute("disable", cartItemService.getAllItemsFromCart().isEmpty());
        model.addAttribute("cart", cartItemService.getAllItemsFromCart());
        model.addAttribute("total", cartItemService.countTotalPriceInCart());
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "cartView";
    }

    @GetMapping("/remove")
    public String removeAmountOfProductInCart(ModelMap model, @RequestParam("id") String cartItemId) {
        CartItem cartItem = cartItemService.findById(Long.valueOf(cartItemId));

        Product product = productService.findProductById(cartItem.getProduct().getId());
        if (cartItem.getAmount() > 0) {
            productService.increaseProductAmountAvailable(product.getId(), 1);
            cartItem.setAmount(cartItem.getAmount() - 1);
            cartItem.setTotalPrice(cartItemService.round(cartItem.getAmount() * cartItem.getProduct().getPrice()));
            cartItemRepository.save(cartItem);
        }
        model.addAttribute("disable", cartItemService.getAllItemsFromCart().isEmpty());
        model.addAttribute("cart", cartItemService.getAllItemsFromCart());
        model.addAttribute("total", cartItemService.countTotalPriceInCart());
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "cartView";
    }

    @GetMapping("/delete")
    public String deleteProductFromCart(ModelMap model, @RequestParam("id") String cartItemId) {
        CartItem cartItem = cartItemService.findById(Long.valueOf(cartItemId));
        Product product = productService.findProductById(cartItem.getProduct().getId());
        product.setAmountAvailable(product.getAmountAvailable() + cartItem.getAmount());
        productRepository.save(product);
        cartItemRepository.delete(cartItem);
        model.addAttribute("disable", cartItemService.getAllItemsFromCart().isEmpty());
        model.addAttribute("cart", cartItemService.getAllItemsFromCart());
        model.addAttribute("total", cartItemService.countTotalPriceInCart());
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "cartView";
    }

    @GetMapping("/emptyCart")
    public String emptyCart(ModelMap model) {
        List<CartItem> allItemsFromCart = cartItemService.getAllItemsFromCart();
        for (CartItem cartItem : allItemsFromCart) {
            Product productById = productService.findProductById(cartItem.getProduct().getId());
            productById.setAmountAvailable(productById.getAmountAvailable() + cartItem.getAmount());
            productRepository.save(productById);
            cartItemRepository.delete(cartItem);
        }
        model.addAttribute("info", "Your cart is empty");
        return "cartViewEmpty";
    }

    @GetMapping("/confirm")
    public String confirmCart(ModelMap model) {
        List<CartItem> allItemsFromCart = cartItemService.getAllItemsFromCart();
        String message = "You cannot create order with empty cart.";
        if (!allItemsFromCart.isEmpty()) {
            OrderCart newOrder = new OrderCart(allItemsFromCart);
            orderService.createNewOrder(newOrder);

            message = "Your order has been created.";
            model.addAttribute("order", orderService.getOrderById(newOrder.getId()));
            model.addAttribute("total", cartItemService.countTotalPriceInCart());
            model.addAttribute("cart", cartItemService.getAllItemsFromCart());
            model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        }
        model.addAttribute("message", message);
        return "confirmCart";
    }

    @GetMapping("/register")
    public String continueVew(ModelMap model,
                              @RequestParam("id") String orderId) {
        String message = "Please register";
        model.addAttribute("message", message);
        model.addAttribute("order", orderService.getOrderById(Long.valueOf(orderId)));
        model.addAttribute("total", cartItemService.countTotalPriceInCart());
        model.addAttribute("cart", cartItemService.getAllItemsFromCart());
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "registerView";
    }

    @PostMapping("/register")
    public String goToRegister(ModelMap model,
                               @RequestParam("id") String orderId,
                               @RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("address") String address,
                               @RequestParam("city") String city) {
        OrderCart orderById = orderService.getOrderById(Long.valueOf(orderId));
        String message = "Registration successfull.";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setAddress(address);
        user.setCity(city);
        userRepository.save(user);

        User createdUser = userService.getUserByUserName(username);

        orderById.setUser(createdUser);
        orderById.setTotalCost(cartItemService.countTotalPriceInCart());
        orderRepository.save(orderById);

        List<OrderCart> userOrders = new ArrayList<>();
        userOrders.add(orderById);
        user.setOrders(userOrders);
        userRepository.save(user);

        boolean disableButton = false;

        if (!cartItemService.checkIfCityExistsInCart(city)) {
            message = "You cannot order from different city - please change your order address.";
            disableButton = true;
        }

        model.addAttribute("disableButton", disableButton);
        model.addAttribute("user", userService.getUserByUserName(username));
        model.addAttribute("order", orderService.getOrderById(Long.valueOf(orderId)));
        model.addAttribute("total", cartItemService.countTotalPriceInCart());
        model.addAttribute("cart", cartItemService.getAllItemsFromCart());
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        model.addAttribute("message", message);
        return "view";
    }


    @GetMapping("/delivery")
    public String delivery(ModelMap model, @RequestParam("id") String orderId,
                           @RequestParam("username") String username) {

        //cartItemService.emptyCart();

        OrderCart orderById = orderService.getOrderById(Long.valueOf(orderId));

        String cityFromOrder = orderService.getCityFromOrder(orderById);
        delivererService.searchFreeDeliverer(orderById, cityFromOrder);

        DelivererImpl delivererByOrderId = delivererService.getDelivererByOrderId(orderById.getId());
        String message = "Your order will be delivered by: " + delivererByOrderId.getName();

//        if (RESULT OF SEARCH BOOLEAN) {
//            deliveryInfo = "Sorry, but no deliverers are available at the moment. We will let you know as soon as we find free deliverer.";
//        }

        model.addAttribute("user", userService.getUserByUserName(username));
        model.addAttribute("order", orderById);
        model.addAttribute("message", message);
        model.addAttribute("cartItemsCount", cartItemService.countAllItemsInCart());
        return "deliveryView";
    }


    @GetMapping("/all")
    public String getOrders(ModelMap model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("users", userService.getAlUsers());
        model.addAttribute("deliverers", delivererService.getAllDeliverers());
        return "check";
    }
}
