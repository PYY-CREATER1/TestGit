package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from sky_take_out.dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value =OperationType.INSERT)
    void insert(Dish dish);

    // 分页查询菜品
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    // 根据id查询菜品
    @Select("select * from dish d where d.id = #{id}")
    Dish getById(Long id);
//    @Delete("delete  from dish d where d.id = #{id}")
//    void deleteById(Long id);

    void deleteById(List<Long> ids);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     *
     * @param
     * @return
     */
    List<Dish> list(Dish dish);

    @Update("update dish set status = #{status} where id = #{id}")
    void startorStop(Integer status, Long id);
}