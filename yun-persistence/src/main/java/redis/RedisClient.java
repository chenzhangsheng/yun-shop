package redis;


import common.ResultBean;
import common.exception.InvalidRequestRuntimeException;
import common.utils.ObjectUtils;
import common.utils.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;



/**
 * Created by zhangshengchen on 2017/9/12.
 */
@Component
public class RedisClient {
    protected Logger log = Logger.getLogger(this.getClass());
    @Autowired
    public JedisPool jedisPool;

    public Jedis getResource() throws JedisException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException  e) {
            log.error("Jedis getResource:" + e.getMessage() + "_" + ExceptionUtils.getStackTrace(e));
        }
        return jedis;
    }

/**
 * 更新缓存时间
 * @param key String
 * @param cacheSeconds 超时时间，0为不超时
 * @return
 */
      public void setRedisObject(String key,int cacheSeconds){
          Jedis jedis = null;
          try {
               jedis = getResource();
                 if (cacheSeconds!=0){
                     jedis.expire(key,cacheSeconds);
                 }
          } catch (Exception e) {
            log.error("Jedis setObject error:" + e.getMessage() + "_" + ExceptionUtils.getStackTrace(e));
            throw new InvalidRequestRuntimeException("Jedis setObject error", ResultBean.SYS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
              }finally {
              jedis.close();
          }
      }

    /**
     * 缓存
     * @param key String
     * @param value Object对象
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public void setRedisObject(String key,Object value,int cacheSeconds){
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(getBytesKey(key),toBytes(value));
            if (cacheSeconds!=0){
                jedis.expire(key,cacheSeconds);
            }
        } catch (Exception e) {
            log.error("Jedis setObject error:" + e.getMessage() + "_" + ExceptionUtils.getStackTrace(e));
            throw new InvalidRequestRuntimeException("Jedis setObject error", ResultBean.SYS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            jedis.close();
        }
    }



   /**
    * 获取缓存
    * @param key
     @return 对象(反序列化)
    */
     public  Object getRedisObject(String key){
        Jedis jedis = null;
        try {
            jedis = getResource();
            byte[] bytes = jedis.get(getBytesKey(key));
            Object value  =  toObject(bytes);
            return  value;
        } catch (Exception e) {
            log.error("Jedis getObject error:" + e.getMessage() + "_" + ExceptionUtils.getStackTrace(e));
            throw new InvalidRequestRuntimeException("Jedis getObject error", ResultBean.SYS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }finally {
            jedis.close();
        }
      }
    /**
     * 获取缓存
     * @param key
     @return String
     */
    public  String getRedisString(String key){
        Jedis jedis = null;
        try {
            jedis = getResource();
            String value = jedis.get(key);
            return  value;
        } catch (Exception e) {
            log.error("Jedis getObject error:" + e.getMessage() + "_" + ExceptionUtils.getStackTrace(e));
            throw new InvalidRequestRuntimeException("Jedis getObject error", ResultBean.SYS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            jedis.close();
        }
    }


    /**
     * 将key转换为byte[]
     * @param object
     * @return
     */
    private  byte[] getBytesKey(Object object) {
        if(object instanceof String){
            return StringUtils.getBytes((String) object);
            }else {
            return ObjectUtils.serialize(object);
             }
        }
    /**
      * Object转换为byte[]类型
      * @param value Object对象
      * @return byte[]数组
     */
     private  byte[] toBytes(Object value) {
        return ObjectUtils.serialize(value);
       }

    /**
     * byte[]转换为object
     * @param bytes
     * @return
     */
     private static Object toObject(byte[] bytes) {
       return ObjectUtils.unserialize(bytes);
       }
}
