package persistence.dao;


import common.MyMapper;
import domain.City;

import java.util.List;
import java.util.Map;

/**
 * 城市 DAO 接口类
 *
 * Created by bysocket on 07/02/2017.
 */
public interface CityDao extends MyMapper<City> {

    /**
     * 根据城市名称，查询城市信息
     *
     * @param map 城市名
     */
    List<City> findByName(Map<String, Object> map);
}
