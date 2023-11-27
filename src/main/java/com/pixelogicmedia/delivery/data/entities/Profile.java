package com.pixelogicmedia.delivery.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Set;

@Entity
public class Profile extends AuditedEntity {

    @NotBlank
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn
    @NotNull
    private Connection connection;

    private String rootPath;

    @OneToMany(mappedBy = "profile", orphanRemoval=true)
    @Cascade(CascadeType.ALL)
    private Set<ProfileContact> contacts;

    private String emailSubjectTemplate;
    private String completionTemplateKey;
    private String failureTemplateKey;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }

    public Set<ProfileContact> getContacts() {
        return this.contacts;
    }

    public void setContacts(final Set<ProfileContact> contacts) {
        this.contacts = contacts;
    }

    public String getRootPath() {
        return this.rootPath;
    }

    public void setRootPath(final String rootPath) {
        this.rootPath = rootPath;
    }

    public String getEmailSubjectTemplate() {
        return emailSubjectTemplate;
    }

    public void setEmailSubjectTemplate(String subjectTemplate) {
        this.emailSubjectTemplate = subjectTemplate;
    }

    public String getCompletionTemplateKey() {
        return completionTemplateKey;
    }

    public void setCompletionTemplateKey(String completionTemplateKey) {
        this.completionTemplateKey = completionTemplateKey;
    }

    public String getFailureTemplateKey() {
        return failureTemplateKey;
    }

    public void setFailureTemplateKey(String failureTemplateKey) {
        this.failureTemplateKey = failureTemplateKey;
    }
}
