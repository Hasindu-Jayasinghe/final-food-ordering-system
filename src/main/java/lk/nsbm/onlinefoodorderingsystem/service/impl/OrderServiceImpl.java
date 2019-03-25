package lk.nsbm.onlinefoodorderingsystem.service.impl;

import lk.nsbm.onlinefoodorderingsystem.dto.*;
import lk.nsbm.onlinefoodorderingsystem.entity.*;
import lk.nsbm.onlinefoodorderingsystem.repository.OrderRepository;
import lk.nsbm.onlinefoodorderingsystem.repository.RestaurantRepository;
import lk.nsbm.onlinefoodorderingsystem.repository.UserRepository;
import lk.nsbm.onlinefoodorderingsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderDto addOrder(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }

        Orders orders = new Orders();
        orders.setOrderDate(LocalDate.now().toString());
        orders.setCost(orderDto.getCost());
        orders.setTypeOfOrder(orderDto.getTypeOfOrder());
        orders.setTime(orderDto.getTime());
        orders.setPaymentType(orderDto.getPaymentType());
        orders.setLocation(orderDto.getLocation());

        orders.setUser(userRepository.findById(orderDto.getUserDto().getUserId()).get());
        orders.setRestaurant(restaurantRepository.findById(orderDto.getRestaurantDto().getRestaurantId()).get());

        orders.setOrderDetails(orderDto.getOrderDetailDto()
                .stream()
                .map(orderDetail ->
                        new OrderDetail(orderDetail.getCount(), new Food(orderDetail.getFood().getFoodId()))).collect(Collectors.toList()));


        try {
            Orders save = orderRepository.save(orders);
            return new OrderDto(save.getOrderId());
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<OrderDto> getOrders() {

        List<OrderDto> ordersList = orderRepository.findAll()
                .stream()
                .map(order ->
                        new OrderDto(
                                order.getOrderId(),
                                LocalDate.parse(order.getOrderDate()),
                                order.getCost(),
                                order.getTypeOfOrder(),
                                order.getTime(),
                                order.getPaymentType(),
                                order.getLocation(),
                                new UserDto(order.getUser().getUserId(),order.getUser().getFirstName(),order.getUser().getLastName(),order.getUser().getUserType()),
                                new RestaurantDto(order.getRestaurant().getRestaurantId(),order.getRestaurant().getRestaurantName()),
                                order.getOrderDetails()
                                        .stream()
                                        .map(orderDetail ->
                                                new OrderDetailDto(0, orderDetail.getCount(), new FoodDto(orderDetail.getFood().getFoodName()))
                                        ).collect(Collectors.toList()))).collect(Collectors.toList());

        return ordersList;

    }

    @Override
    public List<OrderDto> getSelectedOrders(int id) {
        List<OrderDto> ordersList = orderRepository.getOrdersByUser_UserId(id)
                .stream()
                .map(order ->
                        new OrderDto(
                                order.getOrderId(),
                                LocalDate.parse(order.getOrderDate()),
                                order.getCost(),
                                order.getTypeOfOrder(),
                                order.getTime(),
                                order.getPaymentType(),
                                order.getLocation(),
                                new UserDto(order.getUser().getUserId(),order.getUser().getFirstName(),order.getUser().getLastName(),order.getUser().getUserType()),
                                new RestaurantDto(order.getRestaurant().getRestaurantId(),order.getRestaurant().getRestaurantName()),
                                order.getOrderDetails()
                                        .stream()
                                        .map(orderDetail ->
                                                new OrderDetailDto(0, orderDetail.getCount(), new FoodDto(orderDetail.getFood().getFoodName()))
                                        ).collect(Collectors.toList()))).collect(Collectors.toList());
        return ordersList;
    }
}

