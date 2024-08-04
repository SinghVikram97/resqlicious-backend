package com.vikram.resqliciousbackend.service;

import com.vikram.resqliciousbackend.dto.CartDTO;
import com.vikram.resqliciousbackend.dto.OrderDTO;
import com.vikram.resqliciousbackend.entity.*;
import com.vikram.resqliciousbackend.exception.ResourceNotFoundException;
import com.vikram.resqliciousbackend.repository.DishRepository;
import com.vikram.resqliciousbackend.repository.OrderRepository;
import com.vikram.resqliciousbackend.repository.RestaurantRepository;
import com.vikram.resqliciousbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserDetailsService userDetailsService;
    private final RestaurantService restaurantService;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        long userId = orderDTO.getUserId();
        long restaurantId = orderDTO.getRestaurantId();

        User user = userDetailsService.getUserOrThrowException(userId);
        Restaurant restaurant = restaurantService.getRestaurantOrThrowException(restaurantId);

        Order order = Order.builder()
                .user(user)
                .restaurant(restaurant)
                .pickuptime("7:30pm")
                .dishQuantities(orderDTO.getDishQuantities())
                .build();

        Order savedOrder = orderRepository.save(order);

        user.setOrder(order);
        restaurant.getOrders().add(order);

        userRepository.save(user);
        restaurantRepository.save(restaurant);

        Map<Long, Integer> dishQuantities = orderDTO.getDishQuantities();

        // DishId, Quantity
        dishQuantities.forEach((key, value) -> {
            Optional<Dish> dishOptional = dishRepository.findById(key);
            if (dishOptional.isPresent()) {
                Dish dish = dishOptional.get();
                Integer oldQuantity = dish.getQuantity();
                Integer newQuantity = oldQuantity - value;
                dish.setQuantity(newQuantity);
                dishRepository.save(dish);
            }
        });


        return OrderDTO.builder()
                .id(savedOrder.getId())
                .userId(savedOrder.getUser().getId())
                .restaurantId(savedOrder.getRestaurant().getId())
                .dishQuantities(savedOrder.getDishQuantities())
                .pickuptime(savedOrder.getPickuptime())
                .build();
    }

    public OrderDTO getOrder(Long orderId){
        Order order = getOrderOrThrowException(orderId);
        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .restaurantId(order.getRestaurant().getId())
                .dishQuantities(order.getDishQuantities())
                .pickuptime(order.getPickuptime())
                .build();
    }

    public List<OrderDTO> getAllOrdersForRestaurant(Long restaurantId){
        List<Order> allOrdersForRestaurant = orderRepository.findAllByRestaurantId(restaurantId);
        return allOrdersForRestaurant.stream().map(order -> OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .restaurantId(order.getRestaurant().getId())
                .dishQuantities(order.getDishQuantities())
                .pickuptime(order.getPickuptime())
                .build()).toList();
    }

    public Order getOrderOrThrowException(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "Order ID", orderId));
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderOrThrowException(id);

        try {
            Restaurant restaurant = order.getRestaurant();
            restaurant.getOrders().remove(order);

            User user = order.getUser();
            user.setOrder(null);

            userRepository.save(user);
            restaurantRepository.save(restaurant);

            orderRepository.deleteById(id);

        } catch (Exception e) {
            // Log the exception
            System.err.println("Failed to delete order: " + e.getMessage());
            throw new RuntimeException("Failed to delete order", e);
        }
    }

    public OrderDTO getOrderByUserId(long userId) {
        Optional<Order> orderOptional = orderRepository.findByUserId(userId);
        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
            return OrderDTO.builder()
                    .id(order.getId())
                    .userId(order.getUser().getId())
                    .restaurantId(order.getRestaurant().getId())
                    .dishQuantities(order.getDishQuantities())
                    .pickuptime(order.getPickuptime())
                    .build();
        }else{
            throw new ResourceNotFoundException("User Cart", "User ID", userId);
        }
    }
}
