package top.cloudev.doc.dto;

import java.io.Serializable;

/**
 * Category(文档分类) 的DTO数据传输对象
 * Created by Mac.Manon on 2018/04/04
 */

public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关键字(标准查询)
     */
    private String keyword;

    /**
     * 文档分类所属的项目
     */
    private Long projectId;

    /**
     * 空构造函数
     */
    public CategoryDTO() {
    }

    /**
     * 带参的构造函数
     *
     * @param keyword
     * @param projectId
     */
    public CategoryDTO(String keyword, Long projectId) {
        this.keyword = keyword;
        this.projectId = projectId;
    }

    /**
     * Getter,Setter
     */
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}