# elasticsearch for advanced search

系统分析大作业，详细写个 readme 记录下我肝了整整五天踩坑跳坑的结果。

## 1. elasticSearch

去官网下载安装，这里我用的6.4.1（好像说springboot默认6.4.3？）

修改 ```elasticsearch-6.4.1/config/elasticsearch.yml```，添加如下代码（不同版本可能不一样？反正我试了好些博客说的方案，最终确定这个完美解决问题）：

```yml
 # 注意每行开头都有空格
 network.host: 0.0.0.0
 http.cors.enabled: true
 http.cors.allow-origin: "*"
 transport.tcp.port: 9300
 transport.tcp.compress: true
```

新建一个用户，为其分配 es 目录的权限，然后切换到该用户，执行命令 ```sh bin/elasticsearch -d``` 后台启动。

此时在终端使用命令 ```curl localhost:9200``` 获得 json 结果表示成功。开放服务器 9200 端口后在自己电脑浏览器输入 ```ip:9200```也能获取相同结果。

## 2. 安装 ik 分词器

去 github 下载分词器，版本与 elasticsearch 严格对应。安装完后放在 es 的 plugins 目录下。重新启动 es，前台启动可发现启动日志里输出 ```[plugins] ik``` 表示成功。

## 3. 安装 kibana

这是 es 可视化套件。去官网下载 es 对应版本的 kibana，解压，在 ```kibana-6.4.1-linux-x86_64/config/kibana.yml``` 中添加：

```yml
 server.port: 5601
 server.host: "ip" # 服务器 ip
 elasticsearch.url: "http://localhost:9200"
```

开放服务器 5601 端口，然后浏览器访问 ```ip:5601``` 即可在 kibana 中对 es 进行管理。

```yml
get /index/_mapping # 查看索引信息
DELETE /index # 删库跑路
put /index/type/1 # 添加文档
{

} 
 # 查询所有（但是 kibana 并不能显示所有内容，好像有一个上限）
GET /index/type/_search
{
  "query": {
    "match_all" : {}
  }
}
GET /index/type/_search # 根据某字段进行查询
{
  "query": {
    "match" : {
    	"title": "xxx"
    }
  }
}
```

## 4. 创建索引

在 kibana 的开发者工具输入命令创建索引：

```json
PUT /paperes
  {
    "mappings": {
      "paper": {
        "properties": {
          "@timestamp": {
            "type": "date"
          },
          "@version": {
                "type": "text",
                "fields": {
                  "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                  }
                }
          },
          "paperid": {
                "type": "text",
                "fields": {
                  "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                  }
                }
              },
              "title": {
                "type": "text",
                "analyzer": "ik_max_word",
            "search_analyzer": "ik_max_word",
                "fields": {
                  "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                  }
                }
              },
              "_abstract": {
                "type": "text",
                "analyzer": "ik_max_word",
            "search_analyzer": "ik_max_word",
                "fields": {
                  "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                  }
                }
              },
              "authors": {
                "type": "text",
                "analyzer": "ik_max_word",
            "search_analyzer": "ik_max_word",
                "fields": {
                  "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                  }
                }
              },
              "institutionname": {
                "type": "text",
                "analyzer": "ik_max_word",
            "search_analyzer": "ik_max_word",
                "fields": {
                  "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                  }
                }
              },
              "keyword": {
                "type": "text",
                "analyzer": "ik_max_word",
            "search_analyzer": "ik_max_word",
                "fields": {
                  "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                  }
                }
              },
              "publishtime": {
                "type": "date",
                "format": "yyyy-MM-dd",
                "ignore_malformed":true # 忽略系统帮你自动判定日期格式自动转化，必须设置，不然后面 springboot 老出错
              }
          
        }
      }
    }
  }
```

在这里必须指定好分词器、date格式也要指定好，在这踩坑好久。

##　5. logstash

首先去官网下载安装 logstash，换上淘宝源，在 bin 目录下执行 ```./logstash-plugin install logstash-input-jdbc``` 安装 jdbc 插件，再去下载 mysql-connector-java 这么个驱动包，然后在 bin 目录下新建 ```mysql-es.conf``` :

```conf
input {
  jdbc {
    # 驱动包位置
    jdbc_driver_library => "/usr/local/logstash-6.5.4/mysql-connector-java-5.1.48.jar"
    # 驱动
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    # mysql 数据库地址
    jdbc_connection_string => "jdbc:mysql://localhost:3306/backend"
    # 数据库连接用户名
    jdbc_user => ""
    # 数据库连接用户密码
    jdbc_password => ""
    codec => plain { charset => "UTF-8"}
    # 执行sql语句文件位置
    # statement_filepath => "filename.sql"
    # 执行任务时间间隔，各字段含义（由左至右）分、时、天、月、年，全部为*默认含义为每分钟都更新
    schedule => "* * * * *"
    # 执行sql。es 不支持大写，所以我转了一波。不转不知道可不可以
    statement => "SELECT Paper.paperID as paperid, title, _abstract, keyword, authors, institutionName as institutionname, publishTime as publishtime from Paper, inInstitution where Paper.paperID=inInstitution.paperID"
    # 是否记录上次执行结果, 如果为真,将会把上次执行到的 tracking_column 字段的值记录下来,保存到 last_run_metadata_path 指定的文件中
    record_last_run => true
    # 指定文件,来记录上次执行到的 tracking_column 字段的值
    # 比如上次数据库有 10000 条记录,查询完后该文件中就会有数字 10000 这样的记录,下次执行 SQL 查询可以从 10001 条处开始.
    # 我们只需要在 SQL 语句中 WHERE MY_ID > :last_sql_value 即可. 其中 :last_sql_value 取得就是该文件中的值(10000).
    last_run_metadata_path => "/usr/local/logstash-6.5.4/record"


    # 使用递增列的值
    # use_column_value => true
    # 如果 use_column_value 为真,需配置此参数. track 的数据库 column 名,该 column 必须是递增的
    # tracking_column => paperID
    # 使用递增字段实现自增，但是我们设计 mysql 时没有考虑添加时间，用不了了比较尴尬。。
    # tracking_column => "update_time"
    # tracking_column_type => "timestamp"
  }
}
 
 
output {
  elasticsearch {
        hosts => "localhost:9200"
        # es 的索引
        index => "paperes"
        document_type => "paper"
	      # 数据库中的id
        document_id => "%{paperid}"

        # 配置模板文件
        # 看到有的博客说配置这个可以自动给字段配置分词器，但是失败了
		# template_overwrite => true
        # template => "/usr/local/logstash-6.5.4/bin/templete.json"
  }
}
```

执行 ```./bin/logstash -f mysql-es.conf```，慢慢等如果一直输出类似 ```(0.1s) sql 语句``` 的日志，说明他一直在同步中，没问题。

此时去 kibana 的 discover 就可以看到同步过来的数据了。

## 6. springboot

首先上依赖：

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
        </dependency>
```

添加 ```application.yml```：

```yml
spring:
  data:
    elasticsearch:
      cluster-name: elasticsearch # 记得改为 curl localhost:9200 里显示的 cluster-name
      cluster-nodes: ip:9300      # 9300 是专门给 java 用的，这里不是 9200
      repositories:
        enabled: true
```

添加配置类（忘了是处理啥的了，好像挺重要）：

```java
@SpringBootConfiguration
public class ElasticSearchConfig {
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
```

添加实体类：

```java
@Document(indexName = "paperes", type = "paper", shards = 1, replicas = 0, refreshInterval = "-1")
public class PaperEs implements Serializable {

    private static final long serialVersionUID = -1862015045650359064L;

    @Id
    private String paperid;
    // type 和分词器也必须严格一致
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String _abstract;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String keyword;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String authors;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String institutionname;
    // 下面的 custom 表示自定义日期格式，必须严格与上面创建索引时一致
    @Field( type = FieldType.Date,format = DateFormat.custom, pattern = "yyyy-MM-dd")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
    private Date publishtime;

    public PaperEs() {} // 这个必须要，否则查询返回的时候会报错（好几次忘了写）

```

添加 服务接口啥的就不写了。

这里提几个搜索的东西：

1. termQuery: 不分词，matchQuery：分词
2. SearchQuery:生成查询
3. NativeSearchQueryBuilder: 将连接条件和聚合函数等组合
4. elasticsearchTemplate.query:进行查询
5. withSort(SortBuilders.fieldSort("").order(SortOrder.DESC)) 排序
6. 高亮请参考 https://blog.csdn.net/wenguanjun/article/details/86687690

单独提一下查询条件的拼接：

boolQuery，可以设置多个条件的查询方式。它的作用是用来组合多个Query，有四种方式来组合，must，mustnot，filter，should。

1. must代表返回的文档必须满足must子句的条件，会参与计算分值；
2. filter代表返回的文档必须满足filter子句的条件，但不会参与计算分值；
3. should代表返回的文档可能满足should子句的条件，也可能不满足，有多个should时满足任何一个就可以，通过minimum_should_match设置至少满足几个。
4. mustnot代表必须不满足子句的条件。

BoolQueryBuilder 把它们拼接起来。

具体请看 allSearch 里多条件聚合检索。
