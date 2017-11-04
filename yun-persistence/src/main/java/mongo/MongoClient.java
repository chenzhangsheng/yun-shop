package mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by zhangshengchen on 2017/9/15.
 */
@Component
public class MongoClient {


    public static final Logger logger = LoggerFactory.getLogger(MongoClient.class);


    public Criteria createCriteria(Map<String, Object> gtMap,
                                   Map<String, Object> ltMap, Map<String, Object> eqMap,
                                   Map<String, Object> gteMap, Map<String, Object> lteMap,
                                   Map<String, String> regexMap, Map<String, Collection> inMap,
                                   Map<String, Object> neMap) {
        Criteria c = new Criteria();
        List<Criteria> listC= new ArrayList<Criteria>();
        Set<String> _set = null;
        if (gtMap != null && gtMap.size() > 0) {
            _set = gtMap.keySet();
            for (String _s : _set) {
                listC.add( Criteria.where(_s).gt(gtMap.get(_s))); //gt 大于
            }
        }
        if (ltMap != null && ltMap.size() > 0) {
            _set = ltMap.keySet();
            for (String _s : _set) {
                listC.add(  Criteria.where(_s).lt(ltMap.get(_s)));//lt 小于
            }
        }
        if (eqMap != null && eqMap.size() > 0) {
            _set = eqMap.keySet();
            for (String _s : _set) {
                listC.add(  Criteria.where(_s).is(eqMap.get(_s)));//is 等于
            }
        }
        if (gteMap != null && gteMap.size() > 0) {
            _set = gteMap.keySet();
            for (String _s : _set) {
                listC.add( Criteria.where(_s).gte(gteMap.get(_s))); //gt 大于等于
            }
        }
        if (lteMap != null && lteMap.size() > 0) {
            _set = lteMap.keySet();
            for (String _s : _set) {
                listC.add(  Criteria.where(_s).lte(lteMap.get(_s))); //gt 小于等于
            }
        }

        if (regexMap != null && regexMap.size() > 0) {
            _set = regexMap.keySet();
            for (String _s : _set) {
                listC.add(  Criteria.where(_s).regex(regexMap.get(_s)));
            }
        }

        if (inMap != null && inMap.size() > 0) {
            _set = inMap.keySet();
            for (String _s : _set) {
                listC.add(  Criteria.where(_s).in(inMap.get(_s)));
            }
        }
        if (neMap != null && neMap.size() > 0) {
            _set = neMap.keySet();
            for (String _s : _set) {
                listC.add( Criteria.where(_s).ne(neMap.get(_s)));
            }
        }
        if(listC.size() > 0){
            Criteria [] cs = new Criteria[listC.size()];
            c.andOperator(listC.toArray(cs));
        }
        return c;
    }


}
