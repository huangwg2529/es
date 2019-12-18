package org.buaa.es.service;

import org.buaa.es.entity.PaperEs;
import org.buaa.es.repository.PaperRepository;
import org.buaa.es.tool.Papers;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    PaperRepository paperRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private MyResultMapper myResultMapper;

    @Override
    public Papers getPaperByTitle(String title, int pageIndex, int pageSize, int sort) throws Exception {
        if(title==null || title.equals(""))
            throw new Exception("请输入检索词");

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("title", title));
        if(sort == 1) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.DESC));
        } else if(sort == 2) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.ASC));
        }
        SearchQuery searchQuery = builder.withPageable(pageable).build();

        long count = elasticsearchTemplate.count(searchQuery, PaperEs.class);

        return new Papers(elasticsearchTemplate.queryForList(searchQuery, PaperEs.class), count);
    }

    @Override
    public Papers getPaperByKeyword(String keyword, int pageIndex, int pageSize, int sort) throws Exception {
        if(keyword==null || keyword.equals(""))
            throw new Exception("请输入检索词");

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("keyword", keyword));
        if(sort == 1) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.DESC));
        } else if(sort == 2) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.ASC));
        }
        SearchQuery searchQuery = builder.withPageable(pageable).build();

        long count = elasticsearchTemplate.count(searchQuery, PaperEs.class);

        return new Papers(elasticsearchTemplate.queryForList(searchQuery, PaperEs.class), count);
    }

    @Override
    public Papers getPaperByAuthor(String authors, int pageIndex, int pageSize, int sort) throws Exception {
        if(authors==null || authors.equals(""))
            throw new Exception("请输入检索词");

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("authors", authors));
        if(sort == 1) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.DESC));
        } else if(sort == 2) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.ASC));
        }
        SearchQuery searchQuery = builder.withPageable(pageable).build();

        long count = elasticsearchTemplate.count(searchQuery, PaperEs.class);

        return new Papers(elasticsearchTemplate.queryForList(searchQuery, PaperEs.class), count);
    }

    public Papers getPaperByAbstracts(String abstracts, int pageIndex, int pageSize, int sort) throws Exception {
        if(abstracts==null || abstracts.equals(""))
            throw new Exception("请输入检索词");

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("_abstract", abstracts));
        if(sort == 1) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.DESC));
        } else if(sort == 2) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.ASC));
        }
        SearchQuery searchQuery = builder.withPageable(pageable).build();

        long count = elasticsearchTemplate.count(searchQuery, PaperEs.class);

        return new Papers(elasticsearchTemplate.queryForList(searchQuery, PaperEs.class), count);
    }

    public Papers getPaperByInstitution(String institution, int pageIndex, int pageSize, int sort) throws Exception {
        if(institution==null || institution.equals(""))
            throw new Exception("请输入检索词");
        // HighlightBuilder.Field field = new HighlightBuilder.Field("institutionname").preTags("<span> style=\"color:red\">").postTags("</span>");

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("institutionname", institution));
        if(sort == 1) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.DESC));
        } else if(sort == 2) {
            builder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.ASC));
        }
        SearchQuery searchQuery = builder.withPageable(pageable).build();

        long count = elasticsearchTemplate.count(searchQuery, PaperEs.class);

        return new Papers(elasticsearchTemplate.queryForList(searchQuery, PaperEs.class), count);
    }

    @Override
    public Papers getPaperByTitleAndKeyword(String title, String keyword, int pageIndex, int pageSize, int sort) throws Exception {
        if(title==null || title.equals("") || keyword==null || keyword.equals(""))
            throw new Exception("请输入检索词");

        // 拼接查询条件
        BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("keyword", keyword)).must(QueryBuilders.matchQuery("title", title));

        // 设置分页
        Pageable pageable = PageRequest.of(pageIndex, pageSize);


        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();

        long count = elasticsearchTemplate.count(searchQuery, PaperEs.class);

        return new Papers(elasticsearchTemplate.queryForList(searchQuery, PaperEs.class), count);
    }


    @Override
    public Papers getPaperByTitleAndTitle(String title1, String title2, int pageIndex, int pageSize, int sort) throws Exception {
        if(title1==null || title1.equals("") || title2==null || title2.equals(""))
            throw new Exception("请输入检索词");

        // 拼接查询条件
        BoolQueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", title1)).must(QueryBuilders.matchQuery("title", title2));

        // 设置分页
        Pageable pageable = PageRequest.of(pageIndex, pageSize);


        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();

        long count = elasticsearchTemplate.count(searchQuery, PaperEs.class);

        return new Papers(elasticsearchTemplate.queryForList(searchQuery, PaperEs.class), count);
    }


    /*
     * 每个参数可能是一个也可能是两个检索词
     * Flag = -1 表示不存在这个检索条件
     * Flag = 0 表示只有一个，否则是两个
     * FLag = 1 表示或，用 | 分割
     * Flag = 2 表示且，用 . 分割
     */
    @Override
    public Papers allSearch(String title, String author, String keyword, String institutionname, String abstracts, int pageIndex, int pageSize, int sort) throws Exception {
        // 处理 title
        String[] titles = new String[2];
        int titleFlag;
        if(title==null || title.equals(""))
            titleFlag = -1;
        else {
            if (title.contains(".")) {
                titles = title.split("\\.");
                titleFlag = 2;
            } else if (title.contains("|")) {
                System.out.println(title);
                titles = title.split("\\|");
                titleFlag = 1;
            } else {
                titleFlag = 0;
            }
        }
        // 处理 author
        int authorFlag;
        String[] authors = new String[2];
        if(author==null || author.equals(""))
            authorFlag = -1;
        else {
            if(author.contains(".")) {
                authors = author.split("\\.");
                authorFlag = 2;
            } else if(author.contains("|")) {
                authors = author.split("\\|");
                authorFlag = 1;
            } else {
                authorFlag = 0;
            }
        }
        // 处理 keyword
        String[] keywords = new String[2];
        int keywordFlag;
        if(keyword==null || keyword.equals(""))
            keywordFlag = -1;
        else {
            if(keyword.contains(".")) {
                keywords = keyword.split("\\.");
                keywordFlag = 2;
            } else if(keyword.contains("|")) {
                keywords = keyword.split("\\|");
                keywordFlag = 1;
            } else {
                keywordFlag = 0;
            }
        }
        // institutionname
        String[] institutions = new String[2];
        int institutionFlag;
        if(institutionname==null || institutionname.equals(""))
            institutionFlag = -1;
        else {
            if(institutionname.contains(".")) {
                institutions = institutionname.split("\\.");
                institutionFlag = 2;
            } else if(institutionname.contains("|")) {
                institutions = institutionname.split("\\|");
                institutionFlag = 1;
            } else {
                institutionFlag = 0;
            }
        }
        // abstract
        String[] abstractss = new String[2];
        int abstractFlag;
        if(abstracts==null || abstracts.equals(""))
            abstractFlag = -1;
        else {
            if(abstracts.contains(".")) {
                abstractss = abstracts.split("\\.");
                abstractFlag = 2;
            } else if(abstracts.contains("|")) {
                abstractss = abstracts.split("\\|");
                abstractFlag = 1;
            } else {
                abstractFlag = 0;
            }
        }
        // 无参的判断
        if(titleFlag==-1 && authorFlag==-1 && keywordFlag==-1 && abstractFlag==-1 && institutionFlag==-1)
            throw new Exception("请至少输入一个检索词");

        // 构建检索条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 处理 title
        if(titleFlag != -1) {
            if(titleFlag == 0)
                builder.must(QueryBuilders.matchQuery("title", title));
            else if (titleFlag == 1) {
                BoolQueryBuilder titleBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title", titles[0])).should(QueryBuilders.matchQuery("title", titles[1]));
                builder.must(titleBuilder);
            } else {
                BoolQueryBuilder titleBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", titles[0])).must(QueryBuilders.matchQuery("title", titles[1]));
                builder.must(titleBuilder);
            }
        }
        // 处理 author
        if(authorFlag != -1) {
            if(authorFlag == 0)
                builder.must(QueryBuilders.matchQuery("authors", author));
            else if (authorFlag == 1) {
                BoolQueryBuilder authorBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("authors", authors[0])).should(QueryBuilders.matchQuery("authors", authors[1]));
                builder.must(authorBuilder);
            } else {
                BoolQueryBuilder authorBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("authors", authors[0])).must(QueryBuilders.matchQuery("authors", authors[1]));
                builder.must(authorBuilder);
            }
        }
        // 处理 keyword
        if(keywordFlag != -1) {
            if(keywordFlag == 0)
                builder.must(QueryBuilders.matchQuery("keyword", keyword));
            else if (keywordFlag == 1) {
                BoolQueryBuilder tmpBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("keyword", keywords[0])).should(QueryBuilders.matchQuery("keyword", keywords[1]));
                builder.must(tmpBuilder);
            } else {
                BoolQueryBuilder tmpBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("keyword", keywords[0])).must(QueryBuilders.matchQuery("keyword", keywords[1]));
                builder.must(tmpBuilder);
            }
        }
        // 处理 abstracts
        if(abstractFlag != -1) {
            if(abstractFlag == 0)
                builder.must(QueryBuilders.matchQuery("_abstract", abstracts));
            else if (abstractFlag == 1) {
                BoolQueryBuilder tmpBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("_abstract", abstractss[0])).should(QueryBuilders.matchQuery("_abstract", abstractss[1]));
                builder.must(tmpBuilder);
            } else {
                BoolQueryBuilder tmpBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("_abstract", abstractss[0])).must(QueryBuilders.matchQuery("_abstract", abstractss[1]));
                builder.must(tmpBuilder);
            }
        }
        // 处理 institution name
        if(institutionFlag != -1) {
            if(institutionFlag == 0)
                builder.must(QueryBuilders.matchQuery("institutionname", institutionname));
            else if (institutionFlag == 1) {
                BoolQueryBuilder tmpBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("institutionname", institutions[0])).should(QueryBuilders.matchQuery("institutionname", institutions[1]));
                builder.must(tmpBuilder);
            } else {
                BoolQueryBuilder tmpBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("institutionname", institutions[0])).must(QueryBuilders.matchQuery("institutionname", institutions[1]));
                builder.must(tmpBuilder);
            }
        }

        // 设置排序
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(builder);
        if(sort == 1) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.DESC));
        } else if(sort == 2) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("publishtime").order(SortOrder.ASC));
        }
        // 设置分页
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").preTags("<span>").postTags("</span>");
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        SearchQuery searchQuery = nativeSearchQueryBuilder.withPageable(pageable).withHighlightBuilder(highlightBuilder).build();

        long count = elasticsearchTemplate.count(searchQuery, PaperEs.class);

        return new Papers(elasticsearchTemplate.queryForPage(searchQuery, PaperEs.class, myResultMapper).getContent(), count);
    }



}

/*

    BoolQueryBuilder: 拼装连接(查询)条件
    QueryBuilders: 简单的静态工厂”导入静态”使用。主要作用是查询条件(关系),如区间\精确\多值等条件
    termQuery: 不分词
    matchQuery：分词
    NativeSearchQueryBuilder: 将连接条件和聚合函数等组合
    SearchQuery:生成查询
    elasticsearchTemplate.query:进行查询

    boolQuery，可以设置多个条件的查询方式。它的作用是用来组合多个Query，有四种方式来组合，must，mustnot，filter，should。
    must代表返回的文档必须满足must子句的条件，会参与计算分值；
    filter代表返回的文档必须满足filter子句的条件，但不会参与计算分值；
    should代表返回的文档可能满足should子句的条件，也可能不满足，有多个should时满足任何一个就可以，通过minimum_should_match设置至少满足几个。
    mustnot代表必须不满足子句的条件。

    高亮
 */