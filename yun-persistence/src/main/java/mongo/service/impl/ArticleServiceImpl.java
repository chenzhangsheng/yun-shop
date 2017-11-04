package mongo.service.impl;


import com.github.pagehelper.PageInfo;
import common.ResultBean;
import common.exception.InvalidRequestRuntimeException;
import common.query.ArticleQuery;
import mongo.MongoClient;
import mongo.bean.Article;
import mongo.bean.SeqInfo;
import mongo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangshengchen on 2017/8/23.
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    protected MongoTemplate mongoTemplate;

    private Criteria createCriteria(Long id) {
        Map<String, Object> eqMap = new HashMap<String, Object>();
        eqMap.put("_id", id);
        return mongoClient.createCriteria(null,null,eqMap,null,null,null,null,null);
    }

    public Long insert(ArticleQuery article) {
        Long id=getNextId(Article.class.getSimpleName());
        article.setId(id);
        this.mongoTemplate.save(article);
        return id;
    }
    public void update(Article article){
        Query query = new Query();
        if(article.getId() == null){
            throw new InvalidRequestRuntimeException("input error : need acticle_id", ResultBean.INVALID_REQUST, HttpStatus.UNPROCESSABLE_ENTITY);

        }
        Criteria criteria = createCriteria(article.getId());
        query.addCriteria(criteria);
        this.mongoTemplate.save(article);
    }

    public Article get(Long id) {
        Query query = new Query();
        Criteria criteria = createCriteria(id);
        query.addCriteria(criteria);
        return this.mongoTemplate.findOne(query, Article.class);
    }
    public PageInfo getPage(ArticleQuery article, int start, int size){
        Map<String, Object> eqMap = new HashMap<String, Object>();
        Map<String, String> regexMap = new HashMap<String, String>();
        if(article.getClassifyId()!= null){
            eqMap.put("classifyId", article.getClassifyId());
        }
        if(article.getTitle()!= null){
            regexMap.put("title",article.getTitle());
        }
        Criteria criteria = mongoClient.createCriteria(null,null,eqMap,null,null,regexMap,null,null);
        Query query = new Query();
        query.addCriteria(criteria);
        query.skip(start);
        query.limit(size);
        PageInfo<Article> pageInfo = new PageInfo<Article>();
        pageInfo.setTotal(this.mongoTemplate.count(query,  Article.class));
        pageInfo.setPageNum(start);
        pageInfo.setPageSize(size);
        List<Article> lists = this.mongoTemplate.find(query, Article.class);
        pageInfo.setList(lists);
        return pageInfo;
    }
    public Long getNextId(String collName) {
        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("seqId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SeqInfo seq = mongoTemplate.findAndModify(query, update, options, SeqInfo.class);
        return seq.getSeqId();
    }
}
