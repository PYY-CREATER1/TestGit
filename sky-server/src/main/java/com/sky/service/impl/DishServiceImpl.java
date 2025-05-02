package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
  private DishMapper dishMapper;
  @Autowired
  private DishFlavorMapper dishFlavorMapper;
  @Autowired
  private SetmealDishMapper setmealDishMapper;
    //新增菜品
    @Override
    @Transactional
    public void save(DishDTO dishDTO) {
        //将dto转换为entity
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //插入菜品
        dishMapper.insert(dish);
        //获取插入的菜品id
        Long dishId = dish.getId();
        //插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //直接批量插入
            dishFlavorMapper.insertBatch(flavors);
        }

    }
    //菜品分页查询
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

         PageHelper.startPage(dishPageQueryDTO.getPage(),
                dishPageQueryDTO.getPageSize());
        Page<DishVO> pages = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(pages.getTotal(), pages.getResult()) ;
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //判读菜品是否停售，如果在出售，不能删除
        for (Long id : ids){
            Dish dish = dishMapper.getById(id);
           if (dish.getStatus() == StatusConstant.ENABLE){
               throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
           }
        }
        //判断菜品是否和套餐关联，如果关联了，不能删除
       List<Long> setmealIds  =setmealDishMapper.getById(ids);
        if (setmealIds.size() > 0 && setmealIds != null){
            //不能删除，菜品被套餐关联了
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品数据
//        for (Long id : ids){
//            dishMapper.deleteById(id);
//            //删除菜品口味
//            dishFlavorMapper.deleteById(id);
//        }
        //根据id集合对象删除菜品数据
        dishMapper.deleteById(ids);
        dishFlavorMapper.deleteById(ids);


    }

    //根据id查询菜品数据
    @Override
    public DishVO getByIdwithFlavor(Long id) {
        //根据id查询对应的菜品
        Dish dish = dishMapper.getById(id);
        //根据菜品id查询对应的口味
        List<DishFlavor> flavors = dishFlavorMapper.getByIdwithFlavor(id);
        //组装数据
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    //修改菜品
    @Override
    public DishVO updatewithFlavor(DishDTO dishDTO) {
        //修改菜品表的数据
      Dish dish = new Dish();
      BeanUtils.copyProperties(dishDTO,dish);
      dishMapper.update(dish);
      //删除口味表的原数据
        dishFlavorMapper.deleteBy1Id(dishDTO.getId());
      //插入新的口味数据
      List<DishFlavor> flavors = dishDTO.getFlavors();
      if (flavors != null && flavors.size() > 0){
          flavors.forEach(dishFlavor -> {
              dishFlavor.setDishId(dishDTO.getId());
          });
      }
      //批量插入新数据
      dishFlavorMapper.insertBatch(flavors);
        return  null;
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByIdwithFlavor(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    //启售停售菜品
    @Override
    public void startorStop(Integer status, Long id) {
      dishMapper.startorStop(status,id);
    }
}
