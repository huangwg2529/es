package org.buaa.es.tool;

import org.buaa.es.entity.PaperEs;

import java.util.List;

public class Papers {
    private List<PaperEs> papers;
    private long total;

    public Papers(List<PaperEs> papers, long total) {
        this.papers = papers;
        this.total = total;
    }

    public List<PaperEs> getPapers() {
        return papers;
    }

    public void setPapers(List<PaperEs> papers) {
        this.papers = papers;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}