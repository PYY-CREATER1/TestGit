package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    // 根据套餐id查询套餐菜品关系

    List<Long> getById(List<Long> DishIds);


    // 批量插入套餐菜品数据
    void insertBatch(List<SetmealDish> dishList);

    // 根据套餐id删除套餐菜品关系
    void deleteBySetmealId(List<Long> ids);

    // 根据套餐id查询套餐菜品
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getsetmealDish(Long id);

    //更新套餐菜品
    void update(List<SetmealDish> setmealDishes);
}
