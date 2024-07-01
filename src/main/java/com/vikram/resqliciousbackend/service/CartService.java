package com.vikram.resqliciousbackend.service;

import com.vikram.resqliciousbackend.dto.CartDTO;
import com.vikram.resqliciousbackend.entity.Cart;
import com.vikram.resqliciousbackend.entity.Restaurant;
import com.vikram.resqliciousbackend.entity.User;
import com.vikram.resqliciousbackend.exception.ResourceNotFoundException;
import com.vikram.resqliciousbackend.repository.CartRepository;
import com.vikram.resqliciousbackend.repository.RestaurantRepository;
import com.vikram.resqliciousbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    private final UserDetailsService userDetailsService;

    private final RestaurantService restaurantService;

    public CartDTO createCart(CartDTO cartDTO) {
        long userId = cartDTO.getUserId();
        long restaurantId = cartDTO.getRestaurantId();

        User user = userDetailsService.getUserOrThrowException(userId);
        Restaurant restaurant = restaurantService.getRestaurantOrThrowException(restaurantId);

        Cart cart = Cart.builder()
                .dishQuantities(cartDTO.getDishQuantities())
                .user(user)
                .restaurant(restaurant)
                .build();

        Cart savedCart = cartRepository.save(cart);

        user.setCart(cart);
        restaurant.getCarts().add(cart);

        userRepository.save(user);
        restaurantRepository.save(restaurant);

        return CartDTO.builder()
                .id(savedCart.getId())
                .userId(savedCart.getUser().getId())
                .restaurantId(savedCart.getRestaurant().getId())
                .dishQuantities(savedCart.getDishQuantities())
                .build();
    }

    @Transactional
    public CartDTO updateCart(Long cartId, CartDTO cartDTO) {
        Cart cart = getCartOrThrowException(cartId);

        // Update existing dish quantities and remove entries not present in cartDTO
        Map<Long, Integer> updatedDishQuantities = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : cartDTO.getDishQuantities().entrySet()) {
            Long dishId = entry.getKey();
            Integer quantity = entry.getValue();
            updatedDishQuantities.put(dishId, quantity);
        }

        // Remove entries from cart.getDishQuantities() that are not in updatedDishQuantities
        cart.getDishQuantities().entrySet().removeIf(entry -> !updatedDishQuantities.containsKey(entry.getKey()));

        // Set the updated dish quantities
        cart.setDishQuantities(updatedDishQuantities);

        // Save the updated cart
        Cart savedCart = cartRepository.save(cart);

        return CartDTO.builder()
                .id(savedCart.getId())
                .userId(savedCart.getUser().getId())
                .restaurantId(savedCart.getRestaurant().getId())
                .dishQuantities(savedCart.getDishQuantities())
                .build();
    }


    @Transactional
    public void deleteCart(Long cartId) {
        Cart cart = getCartOrThrowException(cartId);

        try {
            Restaurant restaurant = cart.getRestaurant();
            restaurant.getCarts().remove(cart);

            User user = cart.getUser();
            user.setCart(null);

            userRepository.save(user);
            restaurantRepository.save(restaurant);

            cartRepository.deleteById(cartId);

        } catch (Exception e) {
            // Log the exception
            System.err.println("Failed to delete cart: " + e.getMessage());
            throw new RuntimeException("Failed to delete cart", e);
        }
    }

    public CartDTO getCartByCartId(Long cartId) {
        Cart cart = getCartOrThrowException(cartId);
        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .restaurantId(cart.getRestaurant().getId())
                .dishQuantities(cart.getDishQuantities())
                .build();
    }

    public CartDTO getCartByUserId(Long userId){
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isPresent()){
            Cart cart = cartOptional.get();
            return CartDTO.builder()
                    .id(cart.getId())
                    .userId(cart.getUser().getId())
                    .restaurantId(cart.getRestaurant().getId())
                    .dishQuantities(cart.getDishQuantities())
                    .build();
        }else{
            throw new ResourceNotFoundException("User Cart", "User ID", userId);
        }
    }

    public Cart getCartOrThrowException(long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "Cart ID", cartId));
    }
}
