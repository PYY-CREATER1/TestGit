package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    // 根据id查询套餐
    @Select("select * from setmeal where id = #{setmealId}")
    Setmeal getById(Long setmealId);

    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    //  分页查询套餐
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
   //删除套餐
    void deleteById(List<Long> ids);

    // 修改套餐
    @AutoFill(value = OperationType.UPDATE)
    @Update("update setmeal set category_id = #{categoryId}, name = #{name}, " +
            "price = #{price}, status = #{status}, description = #{description}, image = #{image}, " +
            "update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void update(Setmeal setmeal);
}
