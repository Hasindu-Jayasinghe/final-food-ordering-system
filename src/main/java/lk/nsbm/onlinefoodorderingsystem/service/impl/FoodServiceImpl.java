package lk.nsbm.onlinefoodorderingsystem.service.impl;

import lk.nsbm.onlinefoodorderingsystem.dto.FoodDto;
import lk.nsbm.onlinefoodorderingsystem.dto.FoodTitleDto;
import lk.nsbm.onlinefoodorderingsystem.dto.RestaurantDto;
import lk.nsbm.onlinefoodorderingsystem.entity.Food;
import lk.nsbm.onlinefoodorderingsystem.entity.FoodTitle;
import lk.nsbm.onlinefoodorderingsystem.entity.Restaurant;
import lk.nsbm.onlinefoodorderingsystem.repository.FoodRepository;
import lk.nsbm.onlinefoodorderingsystem.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveFood(FoodDto foodDto) {
        if (foodDto == null) {
            return false;
        }

        Food food = new Food();
        food.setFoodId(foodDto.getFoodId());
        food.setFoodName(foodDto.getFoodName());
        food.setFoodTitle(new FoodTitle(foodDto.getFoodTitle().getTitleId()));
        food.setPrice(foodDto.getPrice());
        food.setImage(foodDto.getImage());
        food.setRestaurantList(new Restaurant(foodDto.getRestaurantDtoList().getRestaurantId()));

        try {
            foodRepository.save(food);
        }catch (Exception e){
            return false;
        }
        return true;
    }


    @Override
    public List<FoodDto> getAllFoods() {
        List<FoodDto> foodDtoList = foodRepository.findAll().stream().map(
                food -> new FoodDto(food.getFoodId(), food.getFoodName(), food.getPrice(),
                        new FoodTitleDto(food.getFoodTitle().getTitleId(), food.getFoodTitle().getName()),
                        food.getImage(), new RestaurantDto(food.getRestaurantList().getRestaurantId(),food.getRestaurantList().getRestaurantName(),food.getRestaurantList().getLocation()))).collect(Collectors.toList());
        return foodDtoList;
    }

    @Override
    public FoodDto getFood(int id) {
        Food food = foodRepository.findById(id).get();

        FoodDto foodDto = new FoodDto();
        foodDto.setFoodId(food.getFoodId());
        foodDto.setFoodName(food.getFoodName());
        foodDto.setFoodTitle(new FoodTitleDto(food.getFoodTitle().getTitleId(),food.getFoodTitle().getName()));
        foodDto.setImage(food.getImage());
        foodDto.setRestaurantDtoList(new RestaurantDto(food.getRestaurantList().getRestaurantId(),food.getRestaurantList().getRestaurantName()));
        foodDto.setPrice( food.getPrice() );

        return foodDto;
    }

    @Override
    public boolean deleteFood(int id) {
        if (foodRepository.existsById(id)) {
            foodRepository.delete(foodRepository.findById(id).get());
            return true;
        }
        return false;
    }

    @Override
    public List<FoodDto> getFoodDtoList(int restaurantId) {
        List<Food> foodList = foodRepository.findByRestaurantId(restaurantId);

        return foodList.stream().map(food -> new FoodDto(
                food.getFoodId(),
                food.getFoodName(),
                food.getPrice(),
                new FoodTitleDto(food.getFoodTitle().getTitleId(), food.getFoodTitle().getName()),
                food.getImage(),
                new RestaurantDto(food.getRestaurantList().getRestaurantId())
        )).collect(Collectors.toList());
    }

}
