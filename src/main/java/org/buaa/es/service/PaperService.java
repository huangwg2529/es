package org.buaa.es.service;

import org.buaa.es.tool.Papers;
import org.springframework.stereotype.Component;

@Component
public interface PaperService {

    public Papers getPaperByTitle(String title, int pageIndex, int pageSize, int sort) throws Exception;

    public Papers getPaperByKeyword(String keyword, int pageIndex, int pageSize, int sort) throws Exception;

    public Papers getPaperByAuthor(String authors, int pageIndex, int pageSize, int sort) throws Exception;

    public Papers getPaperByAbstracts(String abstracts, int pageIndex, int pageSize, int sort) throws Exception;

    public Papers getPaperByInstitution(String institution, int pageIndex, int pageSize, int sort) throws Exception;

    public Papers getPaperByTitleAndKeyword(String title, String keyword, int pageIndex, int pageSize, int sort) throws Exception;

    public Papers getPaperByTitleAndTitle(String title1, String title2, int pageIndex, int pageSize, int sort) throws Exception;

    public Papers allSearch(String title, String author, String keyword, String institutionname, String abstracts, int pageIndex, int pageSize, int sort) throws Exception;

}
