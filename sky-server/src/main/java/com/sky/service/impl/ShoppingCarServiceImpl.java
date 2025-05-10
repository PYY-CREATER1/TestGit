package com.sky.service.impl;


import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCarServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    //添加购物车
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断在购物车中是否存在
        ShoppingCart shoppingCart = new ShoppingCart();
        Long id = BaseContext.getCurrentId();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(id);
        List<ShoppingCart> list   = shoppingCartMapper.list(shoppingCart);
        if(list != null && list.size() > 0){
            //如果存在，数量加1
          ShoppingCart Cart = list.get(0);
          Cart.setNumber(Cart.getNumber()+1);
          shoppingCartMapper.update(shoppingCart);
        }else {
            //判断添加的套餐还是菜品
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null){
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            }else {
                Long setmealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

        //如果不存在，添加到购物车，数量默认为1

    }

    //查看购物车
    @Override
    public List<ShoppingCart> list() {
        //获取用户id
        Long id = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder().
                userId(id).build();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list ;
    }

    //清空购物车
    @Override
    public void clean() {
        Long id = BaseContext.getCurrentId();
        shoppingCartMapper.deleteById(id);
    }
}
