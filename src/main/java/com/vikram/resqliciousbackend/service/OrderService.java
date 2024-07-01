package com.vikram.resqliciousbackend.service;

import com.vikram.resqliciousbackend.dto.OrderDTO;
import com.vikram.resqliciousbackend.entity.Order;
import com.vikram.resqliciousbackend.entity.Restaurant;
import com.vikram.resqliciousbackend.entity.User;
import com.vikram.resqliciousbackend.exception.ResourceNotFoundException;
import com.vikram.resqliciousbackend.repository.OrderRepository;
import com.vikram.resqliciousbackend.repository.RestaurantRepository;
import com.vikram.resqliciousbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserDetailsService userDetailsService;
    private final RestaurantService restaurantService;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

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
                .userId(order.getId())
                .restaurantId(order.getId())
                .dishQuantities(order.getDishQuantities())
                .pickuptime(order.getPickuptime())
                .build();
    }

    public List<OrderDTO> getAllOrdersForRestaurant(Long restaurantId){
        List<Order> allOrdersForRestaurant = orderRepository.findAllByRestaurantId(restaurantId);
        return allOrdersForRestaurant.stream().map(order -> OrderDTO.builder()
                .id(order.getId())
                .userId(order.getId())
                .restaurantId(order.getId())
                .dishQuantities(order.getDishQuantities())
                .pickuptime(order.getPickuptime())
                .build()).toList();
    }

    public Order getOrderOrThrowException(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", "Order ID", orderId));
    }
}
