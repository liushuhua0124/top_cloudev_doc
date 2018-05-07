package top.cloudev.doc.domain;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Document(文档) 的领域层
 * Created by Mac.Manon on 2018/04/04
 */

@Entity
@DynamicUpdate(true)
@Table(name="tbl_document")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    public interface CheckCreate{};
    public interface CheckModify{};

    /**
     * 文档ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long documentId;

    /**
     * 文档所属分类
     */
    //@Range(min=value,max=value, groups={CheckCreate.class, CheckModify.class})
    @Column(nullable = false, name = "category_id")
    private Long categoryId;

    /**
     * 文档名称
     */
    @NotBlank(groups={CheckCreate.class, CheckModify.class})
    @Length(min = 1, max = 50, groups={CheckCreate.class, CheckModify.class})
    //@Pattern(regexp = "", groups={CheckCreate.class, CheckModify.class})
    @Column(nullable = false, name = "name", length = 50)
    private String name = "";

    /**
     * 文档类型
     */
    //@Range(min=value,max=value, groups={CheckCreate.class, CheckModify.class})
    @Column(nullable = false, name = "doc_type")
    private Short docType;

    /**
     * 文档访问网址
     */
    @NotBlank(groups={CheckCreate.class, CheckModify.class})
    @URL(groups={CheckCreate.class, CheckModify.class})
    @Column(nullable = false, name = "url", length = 255)
    private String url = "";

    /**
     * 文档描述
     */
    @NotBlank(groups={CheckCreate.class, CheckModify.class})
    @Length(min = 1, max = 50, groups={CheckCreate.class, CheckModify.class})
    //@Pattern(regexp = "", groups={CheckCreate.class, CheckModify.class})
    @Column(nullable = false, name = "memo", length = 50)
    private String memo = "";

    /**
     * 附件
     */
    @NotBlank(groups={CheckCreate.class, CheckModify.class})
    @Length(min = 1, max = 50, groups={CheckCreate.class, CheckModify.class})
    //@Pattern(regexp = "", groups={CheckCreate.class, CheckModify.class})
    @Column(nullable = false, name = "accessory", length = 50)
    private String accessory = "";

    /**
     * 创建时间
     */
    @Column(nullable = false, name = "creation_time", updatable=false)
    private Date creationTime = new Date();

    /**
     * 创建者
     */
    @Column(nullable = false, name = "creator_user_id", updatable=false)
    private long creatorUserId;

    /**
     * 最近修改时间
     */
    @Column(name = "last_modification_time")
    private Date lastModificationTime;

    /**
     * 最近修改者
     */
    @Column(name = "last_modifier_user_id")
    private long lastModifierUserId;

    /**
     * 已删除
     */
    @Column(nullable = false, name = "is_deleted")
    private Boolean isDeleted = false;

    /**
     * 删除时间
     */
    @Column(name = "deletion_time")
    private Date deletionTime;

    /**
     * 删除者
     */
    @Column(name = "deleter_user_id")
    private long deleterUserId;

    /**
     *空构造函数
     *
     */
    public Document(){
        super();
    }

    /**
     *带参构造函数
     *
     */
    public Document(Long categoryId,String name,Short docType,String url,String memo,String accessory,Date creationTime,long creatorUserId,Date lastModificationTime,long lastModifierUserId,Boolean isDeleted,Date deletionTime,long deleterUserId){
        super();
        this.categoryId = categoryId;
        this.name = name;
        this.docType = docType;
        this.url = url;
        this.memo = memo;
        this.accessory = accessory;
        this.creationTime = creationTime;
        this.creatorUserId = creatorUserId;
        this.lastModificationTime = lastModificationTime;
        this.lastModifierUserId = lastModifierUserId;
        this.isDeleted = isDeleted;
        this.deleterUserId = deleterUserId;
        this.deletionTime = deletionTime;
    }

    /**
     *Getter,Setter
     *
     */
    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getDocType() {
        return docType;
    }

    public void setDocType(Short docType) {
        this.docType = docType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public long getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public Date getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(Date lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public long getLastModifierUserId() {
        return lastModifierUserId;
    }

    public void setLastModifierUserId(long lastModifierUserId) {
        this.lastModifierUserId = lastModifierUserId;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(Date deletionTime) {
        this.deletionTime = deletionTime;
    }

    public long getDeleterUserId() {
        return deleterUserId;
    }

    public void setDeleterUserId(long deleterUserId) {
        this.deleterUserId = deleterUserId;
    }

    @Override
    public String toString() {
        return "Document [documentId=" + documentId + "," + "categoryId=" + categoryId + "," + "name=" + name + "," + "docType=" + docType + "," + "url=" + url + "," + "memo=" + memo + "," + "accessory=" + accessory + ",creationTime=" + creationTime + ",creatorUserId=" + creatorUserId + ",lastModificationTime=" + lastModificationTime + ",lastModifierUserId=" + lastModifierUserId + ",isDeleted=" + isDeleted + ",deletionTime=" + deletionTime + ",deleterUserId=" + deleterUserId + "]";
    }
}