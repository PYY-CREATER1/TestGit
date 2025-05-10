package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import java.util.List;

public interface SetmealService {

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

    //新增套餐
    void save(SetmealDTO setmealDTO);

    //套餐分页查询
    PageResult PageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    //删除套餐
    void delete(List<Long> ids);

    //根据id查询套餐
    SetmealVO getById(Long id);

    //修改套餐
    void update(SetmealDTO setmealDTO);

    //起售或这停售套餐
    void startorStop(Integer status, Long id);
}
