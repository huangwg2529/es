package org.buaa.es.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.sql.Date;

@Document(indexName = "paperes", type = "paper", shards = 1, replicas = 0, refreshInterval = "-1")
public class PaperEs implements Serializable {

    private static final long serialVersionUID = -1862015045650359064L;

    @Id
    private String paperid;
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
    @Field( type = FieldType.Date,format = DateFormat.custom, pattern = "yyyy-MM-dd")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
    private Date publishtime;

    public PaperEs() {

    }

    public PaperEs(String paperId, String title, String abstracts, String keyword, String authors, String institutionname, Date publishtime) {
        this.paperid = paperId;
        this.title = title;
        this._abstract = abstracts;
        this.keyword = keyword;
        this.authors = authors;
        this.institutionname = institutionname;
        this.publishtime = publishtime;
    }

    public String getPaperId() {
        return paperid;
    }

    public void setPaperId(String paperId) {
        this.paperid = paperId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstracts() {
        return _abstract;
    }

    public void setAbstracts(String abstracts) {
        this._abstract = abstracts;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPaperid() {
        return paperid;
    }

    public void setPaperid(String paperid) {
        this.paperid = paperid;
    }

    public String get_abstract() {
        return _abstract;
    }

    public void set_abstract(String _abstract) {
        this._abstract = _abstract;
    }

    public String getInstitutionname() {
        return institutionname;
    }

    public void setInstitutionname(String institutionname) {
        this.institutionname = institutionname;
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }
}
