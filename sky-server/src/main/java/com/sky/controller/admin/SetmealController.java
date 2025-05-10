package com.sky.controller.admin;

import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    // 新增套餐
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.id")
    @PostMapping()
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.save(setmealDTO);
        return Result.success();
    }
    //套餐分页查询
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询:{}",setmealPageQueryDTO);
      PageResult pageResult  =setmealService.PageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    //批量删除套餐
    @DeleteMapping()
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result delete(@RequestParam("ids") List<Long> ids){
        log.info("删除套餐:{}",ids);
        setmealService.delete(ids);
        return Result.success();
    }
    //根据id查询菜品
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable("id") Long id){
        log.info("根据id查询菜品：{}", id);
        SetmealVO setmealVO  = setmealService.getById(id);
        return Result.success(setmealVO);
    }
    //修改套餐
    @PutMapping()
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}", setmealDTO);
      setmealService.update(setmealDTO);
        return Result.success();
    }
    //套餐起售停售
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id){
        setmealService.startorStop(status, id);
        return Result.success();
    }
}
