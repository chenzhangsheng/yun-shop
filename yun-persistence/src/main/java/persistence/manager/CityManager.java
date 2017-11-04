package persistence.manager;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import domain.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import persistence.dao.CityDao;
import redis.RedisClient;


import java.util.List;
import java.util.Map;

/**
 * Created by zhangshengchen on 2017/10/20.
 */
@Service
public class CityManager {

    @Autowired
    private CityDao cityDao;

    @Autowired
    private RedisClient redisClient;

    public PageInfo findCityByName(Map map) {
        System.out.println( "redis="+redisClient.getRedisString("key"));
        PageHelper.startPage((Integer)map.get("pageNo"),(Integer)map.get("rowCount"));
        List<City> list = cityDao.findByName(map);
        PageInfo page = new PageInfo(list);
        return page;
    }
}
