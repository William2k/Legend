package com.legendApi.dto;

import com.legendApi.models.entities.CommentEntity;

import java.util.List;

public class CommentResponseDTO {
    public CommentResponseDTO() {}

    public CommentResponseDTO(CommentEntity commentEntity) {
        setId(commentEntity.getId());
        setContent(commentEntity.getContent());
        setDateCreated(commentEntity.getDateCreated().toString());
        setDateModified(commentEntity.getDateModified() != null ? commentEntity.getDateModified().toString() : null);
        setIsActive(commentEntity.getIsActive());
        setCreator(commentEntity.getCreatorUsername());
    }

    private long id;
    private String content;
    private boolean isActive;
    private String dateCreated;
    private String dateModified;
    private String creator;
    private int level;
    private List<CommentResponseDTO> comments;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getContent() {return content;}
    public void setContent(String value) {content = value;}

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}

    public String getDateCreated() {return  dateCreated;}
    public void  setDateCreated(String value) {dateCreated = value;}

    public String getDateModified() {return  dateModified;}
    public void  setDateModified(String value) {dateModified = value;}

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public List<CommentResponseDTO> getComments() { return comments; }
    public void setComments(List<CommentResponseDTO> comments) { this.comments = comments; }
}
