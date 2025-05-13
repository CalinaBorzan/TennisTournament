package org.example.tennisapp.dto;

import java.util.Date;

public class TournamentDTO {
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;

    public TournamentDTO(Long id, String name, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TournamentDTO(org.example.tennisapp.entity.Tournament t) {
        this(t.getId(), t.getName(), t.getStartDate(), t.getEndDate());
    }


     public Long getId() {
        return id;
     }
     public void setId(Long id) {
        this.id = id;
     }
     public String getName() {
        return name;
     }
     public void setName(String name) {
        this.name = name;
     }
     public Date getStartDate() {
        return startDate;
     }
     public void setStartDate(Date startDate) {
        this.startDate = startDate;
     }
     public Date getEndDate() {
        return endDate;
     }
     public void setEndDate(Date endDate) {
        this.endDate = endDate;
     }

}
