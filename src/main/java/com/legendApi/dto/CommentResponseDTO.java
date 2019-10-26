package com.legendApi.dto;

import com.legendApi.models.entities.CommentEntity;

public class CommentResponseDTO {
    public CommentResponseDTO() {}

    public CommentResponseDTO(CommentEntity commentEntity) {
        setContent(commentEntity.getContent());
        setDateCreated(commentEntity.getDateCreated().toString());
        setDateModified(commentEntity.getDateModified() != null ? commentEntity.getDateModified().toString() : null);
        setIsActive(commentEntity.getIsActive());
    }

    private String content;
    private boolean isActive;
    private String dateCreated;
    private String dateModified;
    private String creator;

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
}
