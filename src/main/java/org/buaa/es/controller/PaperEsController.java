package org.buaa.es.controller;

import org.buaa.es.service.PaperService;
import org.buaa.es.tool.Papers;
import org.buaa.es.tool.RetResponse;
import org.buaa.es.tool.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/search/"})
public class PaperEsController {
    @Autowired
    private PaperService paperService;

    @CrossOrigin
    @RequestMapping(value = {"title"}, method = RequestMethod.GET)
    public RetResult<Object> getPaperByTitle(String title, int pageIndex, int pageSize, int sort) {
        try {
            Papers papers = paperService.getPaperByTitle(title, pageIndex, pageSize, sort);
            return RetResponse.makeOKRsp(papers);
        } catch (Exception e) {
            return RetResponse.makeErrRsp(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = {"keyword"}, method = RequestMethod.GET)
    public RetResult<Object> getPaperByKeyword(String keyword, int pageIndex, int pageSize, int sort) {
        try {
            Papers papers = paperService.getPaperByKeyword(keyword, pageIndex, pageSize, sort);
            return RetResponse.makeOKRsp(papers);
        } catch (Exception e) {
            return RetResponse.makeErrRsp(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = {"author"}, method = RequestMethod.GET)
    public RetResult<Object> getPaperByauthor(String author, int pageIndex, int pageSize, int sort) {
        try {
            Papers papers = paperService.getPaperByAuthor(author, pageIndex, pageSize, sort);
            return RetResponse.makeOKRsp(papers);
        } catch (Exception e) {
            return RetResponse.makeErrRsp(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = {"institution"}, method = RequestMethod.GET)
    public RetResult<Object> getPaperByInstitution(String institutionname, int pageIndex, int pageSize, int sort) {
        try {
            Papers papers = paperService.getPaperByInstitution(institutionname, pageIndex, pageSize, sort);
            return RetResponse.makeOKRsp(papers);
        } catch (Exception e) {
            return RetResponse.makeErrRsp(e.getMessage());
        }
    }

    @RequestMapping({"abstracts"})
    public RetResult<Object> getPaperByAbstract(String abstracts, int pageIndex, int pageSize, int sort) {
        try {
            Papers papers = paperService.getPaperByAbstracts(abstracts, pageIndex, pageSize, sort);
            return RetResponse.makeOKRsp(papers);
        } catch (Exception e) {
            return RetResponse.makeErrRsp(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = {"titlekeyword"}, method = RequestMethod.GET)
    public RetResult<Object> getPaperByTitleAndKeyword(String title, String keyword, int pageIndex, int pageSize, int sort) {
        try {
            Papers papers = paperService.getPaperByTitleAndKeyword(title, keyword, pageIndex, pageSize, sort);
            return RetResponse.makeOKRsp(papers);
        } catch (Exception e) {
            return RetResponse.makeErrRsp(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = {"titles"}, method = RequestMethod.GET)
    public RetResult<Object> getPaperByTitleAndTitle(String title1, String title2, int pageIndex, int pageSize, int sort) {
        try {
            Papers papers = paperService.getPaperByTitleAndKeyword(title1, title2, pageIndex, pageSize, sort);
            return RetResponse.makeOKRsp(papers);
        } catch (Exception e) {
            return RetResponse.makeErrRsp(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = {"allsearch"}, method = RequestMethod.GET)
    public RetResult<Object> getPaperByAll(String title, String author, String keyword, String institutionname, String abstracts, int pageIndex, int pageSize, int sort) {
        try {
            Papers papers = paperService.allSearch(title, author, keyword, institutionname, abstracts, pageIndex, pageSize, sort);
            return RetResponse.makeOKRsp(papers);
        } catch (Exception e) {
            return RetResponse.makeErrRsp(e.getMessage());
        }
    }



}
