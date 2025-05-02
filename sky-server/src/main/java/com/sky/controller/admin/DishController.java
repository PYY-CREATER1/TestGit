package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /*
    * 新增菜品
    * */
    @PostMapping()
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}", dishDTO);
        String key = "dish_"+ dishDTO.getCategoryId();
        redisTemplate.delete(key);
        dishService.save(dishDTO);
        return Result.success();
    }

    //菜品分页查询
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}", dishPageQueryDTO);
      PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    //删除菜品
    @DeleteMapping()
    public Result delete(@RequestParam("ids")List<Long> ids){
        log.info("删除菜品：{}", ids);
        //将所有缓存数据清理掉
       CleanDish("dish_*");
        dishService.delete(ids);
        return Result.success();
    }
    //根据id查询菜品
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable("id") Long id){
        log.info("根据id查询菜品：{}", id);
       DishVO dishVO  = dishService.getByIdwithFlavor(id);
       return Result.success(dishVO);
    }

    //菜品的起售或停售
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status, Long id){
        dishService.startorStop(status, id);
       //将所有的菜品缓存数据清理掉，所有以dish_开头的key
       CleanDish("dish_*");
        return Result.success();
    }

    //根据id修改菜品
    @PutMapping()
    public Result<DishVO> update(@RequestBody DishDTO dishDTO){
        log.info("根据id修改菜品：{}", dishDTO);
        //将所有缓存数据清理掉
        CleanDish("dish_*");
      DishVO dishVO   = dishService.updatewithFlavor(dishDTO);
        return Result.success(dishVO);
    }


    private void CleanDish(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
