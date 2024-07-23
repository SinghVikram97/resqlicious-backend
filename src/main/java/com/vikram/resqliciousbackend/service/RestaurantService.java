package com.vikram.resqliciousbackend.service;

import com.vikram.resqliciousbackend.dto.MenuDTO;
import com.vikram.resqliciousbackend.dto.RestaurantDTO;
import com.vikram.resqliciousbackend.entity.Menu;
import com.vikram.resqliciousbackend.entity.Restaurant;
import com.vikram.resqliciousbackend.entity.User;
import com.vikram.resqliciousbackend.exception.ResourceNotFoundException;
import com.vikram.resqliciousbackend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserDetailsService userDetailsService;
    private final MenuService menuService;
    @Transactional
    public RestaurantDTO addRestaurant(RestaurantDTO restaurantDTO) {
        Long userId = restaurantDTO.getUserId();

        User user = userDetailsService.getUserOrThrowException(userId);

        Restaurant restaurant = Restaurant.builder()
                .name(restaurantDTO.getName())
                .rating(restaurantDTO.getRating())
                .cuisine(restaurantDTO.getCuisine())
                .address(restaurantDTO.getAddress())
                .avgPrice(restaurantDTO.getAvgPrice())
                .description(restaurantDTO.getDescription())
                .phone(restaurantDTO.getPhone())
                .email(restaurantDTO.getEmail())
                .user(user)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        MenuDTO menuDTO = menuService.addMenu(MenuDTO.builder().restaurantId(savedRestaurant.getId()).build());
        Menu menu = menuService.getMenuOrThrowException(menuDTO.getId());


        savedRestaurant.setMenu(menu);

        Restaurant savedRestaurantWithMenuId = restaurantRepository.save(savedRestaurant);
        user.setRestaurant(savedRestaurantWithMenuId);
        menu.setRestaurant(restaurant);

        return RestaurantDTO.builder()
                .id(savedRestaurant.getId())
                .name(savedRestaurant.getName())
                .rating(savedRestaurant.getRating())
                .cuisine(savedRestaurant.getCuisine())
                .address(savedRestaurant.getAddress())
                .avgPrice(savedRestaurant.getAvgPrice())
                .menuId(nonNull(savedRestaurant.getMenu()) ? savedRestaurant.getMenu().getId() : null)
                .description(savedRestaurant.getDescription())
                .phone(savedRestaurant.getPhone())
                .email(savedRestaurant.getEmail())
                .userId(savedRestaurant.getUser().getId())
                .imageUrl(savedRestaurant.getImageUrl())
                .build();
    }

    public RestaurantDTO addRestaurantImage(Long id, String path, MultipartFile file) throws IOException {
        Restaurant restaurant = getRestaurantOrThrowException(id);

        // File logic
        String originalFilename = file.getOriginalFilename();

        // Full path
        String filePath = path + File.separator + originalFilename;

        // Create folder if not created
        File f = new File(path);

        if(!f.exists()) {
            f.mkdir();
        }

        // File copy
        Files.copy(file.getInputStream(), Paths.get(filePath));

        restaurant.setImageUrl(originalFilename);


        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantDTO.builder()
                .id(savedRestaurant.getId())
                .name(savedRestaurant.getName())
                .rating(savedRestaurant.getRating())
                .cuisine(savedRestaurant.getCuisine())
                .address(savedRestaurant.getAddress())
                .avgPrice(savedRestaurant.getAvgPrice())
                .menuId(nonNull(savedRestaurant.getMenu()) ? savedRestaurant.getMenu().getId() : null)
                .description(savedRestaurant.getDescription())
                .phone(savedRestaurant.getPhone())
                .email(savedRestaurant.getEmail())
                .userId(savedRestaurant.getUser().getId())
                .imageUrl(savedRestaurant.getImageUrl())
                .build();

    }

    public RestaurantDTO getRestaurant(Long id) {
        Restaurant restaurant = getRestaurantOrThrowException(id);
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .rating(restaurant.getRating())
                .cuisine(restaurant.getCuisine())
                .avgPrice(restaurant.getAvgPrice())
                .menuId(nonNull(restaurant.getMenu()) ? restaurant.getMenu().getId() : null)
                .description(restaurant.getDescription())
                .phone(restaurant.getPhone())
                .email(restaurant.getEmail())
                .userId(restaurant.getUser().getId())
                .imageUrl(restaurant.getImageUrl())
                .build();
    }

    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO restaurantDTO) {
        Restaurant restaurant = getRestaurantOrThrowException(id);

        restaurant.setName(restaurantDTO.getName());
        restaurant.setRating(restaurantDTO.getRating());
        restaurant.setCuisine(restaurantDTO.getCuisine());
        restaurant.setAddress(restaurantDTO.getAddress());
        restaurant.setAvgPrice(restaurantDTO.getAvgPrice());
        restaurant.setDescription(restaurantDTO.getDescription());
        restaurant.setPhone(restaurantDTO.getPhone());
        restaurant.setEmail(restaurantDTO.getEmail());


        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantDTO.builder()
                .id(savedRestaurant.getId())
                .name(savedRestaurant.getName())
                .rating(savedRestaurant.getRating())
                .cuisine(savedRestaurant.getCuisine())
                .address(savedRestaurant.getAddress())
                .avgPrice(savedRestaurant.getAvgPrice())
                .menuId(nonNull(savedRestaurant.getMenu()) ? savedRestaurant.getMenu().getId() : null)
                .description(savedRestaurant.getDescription())
                .phone(savedRestaurant.getPhone())
                .email(savedRestaurant.getEmail())
                .userId(savedRestaurant.getUser().getId())
                .imageUrl(savedRestaurant.getImageUrl())
                .build();
    }

    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> allRestaurants = restaurantRepository.findAll();

        return allRestaurants.stream().map(restaurant -> RestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .rating(restaurant.getRating())
                .cuisine(restaurant.getCuisine())
                .avgPrice(restaurant.getAvgPrice())
                .menuId(nonNull(restaurant.getMenu()) ? restaurant.getMenu().getId() : null)
                .description(restaurant.getDescription())
                .phone(restaurant.getPhone())
                .email(restaurant.getEmail())
                .userId(restaurant.getUser().getId())
                .imageUrl(restaurant.getImageUrl())
                .build()).collect(Collectors.toList());
    }

    public Restaurant getRestaurantOrThrowException(long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "Restaurant ID", restaurantId));
    }

    public List<RestaurantDTO> getRestaurantByUserId(long userId){
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByUserId(userId);

        if(restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            RestaurantDTO restaurantDTO = RestaurantDTO.builder()
                    .id(restaurant.getId())
                    .name(restaurant.getName())
                    .address(restaurant.getAddress())
                    .rating(restaurant.getRating())
                    .cuisine(restaurant.getCuisine())
                    .avgPrice(restaurant.getAvgPrice())
                    .menuId(nonNull(restaurant.getMenu()) ? restaurant.getMenu().getId() : null)
                    .description(restaurant.getDescription())
                    .phone(restaurant.getPhone())
                    .email(restaurant.getEmail())
                    .userId(restaurant.getUser().getId())
                    .imageUrl(restaurant.getImageUrl())
                    .build();

            return List.of(restaurantDTO);
        }else{
            return Collections.emptyList();
        }
    }


}
