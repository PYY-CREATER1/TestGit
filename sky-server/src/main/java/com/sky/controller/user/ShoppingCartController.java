package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
//  用户端购物车
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车,商品信息为 {}",shoppingCartDTO );
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    // 查询购物车
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        log.info("查询购物车");
     List<ShoppingCart> list = shoppingCartService.list();
        return Result.success(list);
    }
    // 清空购物车
    @DeleteMapping("/clean")
    public Result clean(){
        shoppingCartService.clean();
        return Result.success();
    }
}
