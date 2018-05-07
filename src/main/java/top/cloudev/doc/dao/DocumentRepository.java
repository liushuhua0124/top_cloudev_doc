package top.cloudev.doc.dao;

import top.cloudev.doc.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 领域类 Document(文档) 的DAO Repository接口层
 * Created by Mac.Manon on 2018/04/04
 */

//@RepositoryRestResource(path = "newpath")
public interface DocumentRepository extends JpaRepository<Document,Long>, JpaSpecificationExecutor {

    Page<Document> findByIsDeletedFalse(Pageable pageable);

    Page<Document> findByNameContainingAndUrlContainingAndMemoContainingAndAccessoryContainingAndIsDeletedFalseAllIgnoringCase(String name, String url, String memo, String accessory, Pageable pageable);

    //用于判断是否有相同名称的值
    List<Document> findByAndNameAndIsDeletedFalse(String name);
}