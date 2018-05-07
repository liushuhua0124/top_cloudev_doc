package top.cloudev.doc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import top.cloudev.doc.domain.Document;
import top.cloudev.doc.dto.DocumentDTO;
import top.cloudev.doc.dao.DocumentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.Locale;

import static top.cloudev.doc.DocApplicationTests.Obj2Json;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 领域类 Document(文档) 的单元测试代码
 * Created by Mac.Manon on 2018/04/04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional// 使用@Transactional注解，确保每次测试后的数据将会被回滚
public class DocumentControllerTest {
    @Autowired
    DocumentRepository documentRepository;

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    // 默认初始化的一条文档数据
    private Document d1;

    // 期望返回的数据
    private String expectData;

    // 实际调用返回的结果
    private String responseData;

    // 列表查询传参
    private DocumentDTO dto;

    // 期望获得的结果数量
    private Long expectResultCount;

    private Long id = 0L;

    // 使用JUnit的@Before注解可在测试开始前进行一些初始化的工作
    @Before
    public void setUp() throws JsonProcessingException {
        /**---------------------测试用例赋值开始---------------------**/
        d1 = new Document();
        d1.setCategoryId(1L);
        d1.setName("百度");
        d1.setDocType((short)2);
        d1.setUrl("http://www.baidu.com");
        d1.setMemo("中国最大的搜索网站");
        d1.setAccessory("能搜索出一切想要的东西");
        d1.setCreatorUserId(1);
        documentRepository.save(d1);
        id = d1.getDocumentId();
        /**---------------------测试用例赋值结束---------------------**/

        // 获取mockMvc对象实例
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    /**
     * 测试列表
     *
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {

        //提示：构建"新增数据"提示：根据新增数据时客户端实际能提供的参数，依据"最少字段和数据正确的原则"构建
        //提示：构建"修改过的数据"提示：根据修改数据时客户端实际能提供的参数构建
        //提示：可以构建"非物理删除的数据"

        //添加数据:用例二(数据名称跟装配数据中的名称有部分关键字相同)
        /**---------------------测试用例赋值开始---------------------**/
        Document d2 = new Document();
        d2.setCategoryId(1L);
        d2.setName("菜鸟教程百度");
        d2.setDocType((short)2);
        d2.setUrl("http://www.runoob.com");
        d2.setMemo("学习网站");
        d2.setAccessory("有数据库,前段,后端");
        d2.setCreatorUserId(2);
        //提示：构造"修改过的数据"时需要给"最近修改时间"和"最近修改者"赋值
        //d2.setLastModificationTime(new Date());
        //d2.setLastModifierUserId(1);
        //提示：构造"非物理删除的数据"时需要给"已删除"、"删除时间"和"删除者"赋值
        //d2.setIsDeleted(true);
        //d2.setDeletionTime(new Date());
        //d2.setDeleterUserId(1);
        documentRepository.save(d2);
        /**---------------------测试用例赋值结束---------------------**/


        //添加数据:用例三(数据名称与用例1和用例2完全不同)
        /**---------------------测试用例赋值开始---------------------**/
        Document d3 = new Document();
        d3.setCategoryId(1L);
        d3.setName("CSDN");
        d3.setDocType((short)2);
        d3.setUrl("https://www.csdn.net/");
        d3.setMemo("交流网站");
        d3.setAccessory("一群朋友讨论技术");
        d3.setCreatorUserId(2);
        documentRepository.save(d3);
        /**---------------------测试用例赋值结束---------------------**/


        //添加数据:用例四(数据名称跟用例一一样)
        /**---------------------测试用例赋值开始---------------------**/
        Document d4 = new Document();
        d4.setCategoryId(2L);
        d4.setName("百度");
        d4.setDocType((short)2);
        d4.setUrl("https://www.baidu.com");
        d4.setMemo("中国最大的搜索网站");
        d4.setAccessory("能搜索出一切想要的东西");
        d4.setCreatorUserId(2);
        documentRepository.save(d4);
        /**---------------------测试用例赋值结束---------------------**/


        //修改数据:用例五
        /**---------------------测试用例赋值开始---------------------**/
        Document d5 = new Document();
        d5.setCategoryId(1L);
        d5.setName("被修改过的百度");
        d5.setDocType((short)2);
        d5.setUrl("https://www.baidu.com1");
        d5.setMemo("中国最大的搜索网站1");
        d5.setAccessory("能搜索出一切想要的东西1");
        d5.setLastModificationTime(new Date());
        d5.setLastModifierUserId(1);
        d5.setCreatorUserId(2);
        documentRepository.save(d5);
        /**---------------------测试用例赋值结束---------------------**/


        //删除数据:用例六
        /**---------------------测试用例赋值开始---------------------**/
        Document d6 = new Document();
        d6.setCategoryId(1L);
        d6.setName("被修改过的百度");
        d6.setDocType((short)2);
        d6.setUrl("https://www.baidu.com1");
        d6.setMemo("中国最大的搜索网站1");
        d6.setAccessory("能搜索出一切想要的东西1");
        d6.setLastModificationTime(new Date());
        d6.setLastModifierUserId(1);
        d6.setCreatorUserId(2);
        d6.setIsDeleted(true);
        d6.setDeletionTime(new Date());
        d6.setDeleterUserId(1);
        documentRepository.save(d6);

        /**
         * 测试无搜索列表
         */

        /**---------------------测试用例赋值开始---------------------**/
        Pageable pageable = new PageRequest(0, 10, Sort.Direction.ASC, "documentId");
        // 期望获得的结果数量(默认有两个测试用例，所以值应为"2L"，如果新增了更多测试用例，请相应设定这个值)
        expectResultCount = 5L;
        /**---------------------测试用例赋值结束---------------------**/

        // 直接通过dao层接口方法获得期望的数据
        Page<Document> pagedata = documentRepository.findByIsDeletedFalse(pageable);
        expectData = JsonPath.read(Obj2Json(pagedata), "$").toString();

        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/document/list/")
                                .accept(MediaType.APPLICATION_JSON)
                )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查返回的数据节点
                .andExpect(jsonPath("$.pagedata.totalElements").value(expectResultCount))
                .andExpect(jsonPath("$.dto.keyword").isEmpty())
                .andExpect(jsonPath("$.dto.name").isEmpty())
                .andExpect(jsonPath("$.dto.url").isEmpty())
                .andExpect(jsonPath("$.dto.memo").isEmpty())
                .andExpect(jsonPath("$.dto.accessory").isEmpty())
                .andReturn();

        // 提取返回结果中的列表数据及翻页信息
        responseData = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.pagedata").toString();

        System.out.println("=============无搜索列表期望结果：" + expectData);
        System.out.println("=============无搜索列表实际返回：" + responseData);

        Assert.assertEquals("错误，无搜索列表返回数据与期望结果有差异", expectData, responseData);


        /**
         * 测试标准查询
         */

        /**---------------------测试用例赋值开始---------------------**/
        dto = new DocumentDTO();
        //通过页面传入的keyword进行查询
        dto.setKeyword("百度");

        pageable = new PageRequest(0, 10, Sort.Direction.ASC, "documentId");

        // 期望获得的结果数量
        expectResultCount = 4L;
        /**---------------------测试用例赋值结束---------------------**/

        String keyword = dto.getKeyword().trim();

        // 直接通过dao层接口方法获得期望的数据
        Specification<Document> specification = new Specification<Document>() {
            @Override
            public Predicate toPredicate(Root<Document> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate name = cb.like(cb.upper(root.get("name")), "%" + keyword.toUpperCase() + "%");
                Predicate url = cb.like(cb.upper(root.get("url")), "%" + keyword.toUpperCase() + "%");
                Predicate memo = cb.like(cb.upper(root.get("memo")), "%" + keyword.toUpperCase() + "%");
                Predicate accessory = cb.like(cb.upper(root.get("accessory")), "%" + keyword.toUpperCase() + "%");
                Predicate isDeleted = cb.equal(root.get("isDeleted").as(Boolean.class), false);
                Predicate p = cb.and(isDeleted, cb.or(name, url, memo, accessory));
                return p;
            }
        };
        pagedata = documentRepository.findAll(specification, pageable);
        expectData = JsonPath.read(Obj2Json(pagedata), "$").toString();

        mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/document/list")
                                .param("keyword", dto.getKeyword())
                                .accept(MediaType.APPLICATION_JSON)
                )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查返回的数据节点
                .andExpect(jsonPath("$.pagedata.totalElements").value(expectResultCount))
                .andExpect(jsonPath("$.dto.keyword").value(dto.getKeyword()))
                .andExpect(jsonPath("$.dto.name").isEmpty())
                .andExpect(jsonPath("$.dto.url").isEmpty())
                .andExpect(jsonPath("$.dto.memo").isEmpty())
                .andExpect(jsonPath("$.dto.accessory").isEmpty())
                .andReturn();

        // 提取返回结果中的列表数据及翻页信息
        responseData = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.pagedata").toString();

        System.out.println("=============标准查询期望结果：" + expectData);
        System.out.println("=============标准查询实际返回：" + responseData);

        Assert.assertEquals("错误，标准查询返回数据与期望结果有差异", expectData, responseData);


        /**
         * 测试高级查询
         */

        /**---------------------测试用例赋值开始---------------------**/
        //通过四个字段进行查询
        dto = new DocumentDTO();
        dto.setName("CSDN");
        dto.setUrl("https://www.csdn.net/");
        dto.setMemo("交流网站");
        dto.setAccessory("一群朋友讨论技术");

        pageable = new PageRequest(0, 10, Sort.Direction.ASC, "documentId");

        // 期望获得的结果数量
        expectResultCount = 1L;
        /**---------------------测试用例赋值结束---------------------**/

        // 直接通过dao层接口方法获得期望的数据
        pagedata = documentRepository.findByNameContainingAndUrlContainingAndMemoContainingAndAccessoryContainingAndIsDeletedFalseAllIgnoringCase(dto.getName().trim(), dto.getUrl().trim(), dto.getMemo().trim(), dto.getAccessory().trim(), pageable);
        expectData = JsonPath.read(Obj2Json(pagedata), "$").toString();
        mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/document/list")
                                .param("name", dto.getName())
                                .param("url", dto.getUrl())
                                .param("memo", dto.getMemo())
                                .param("accessory", dto.getAccessory())
                                .accept(MediaType.APPLICATION_JSON)
                )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查返回的数据节点
                .andExpect(jsonPath("$.pagedata.totalElements").value(expectResultCount))
                .andExpect(jsonPath("$.dto.keyword").isEmpty())
                .andExpect(jsonPath("$.dto.name").value(dto.getName()))
                .andExpect(jsonPath("$.dto.url").value(dto.getUrl()))
                .andExpect(jsonPath("$.dto.memo").value(dto.getMemo()))
                .andExpect(jsonPath("$.dto.accessory").value(dto.getAccessory()))
                .andReturn();

        // 提取返回结果中的列表数据及翻页信息
        responseData = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.pagedata").toString();

        System.out.println("=============高级查询期望结果：" + expectData);
        System.out.println("=============高级查询实际返回：" + responseData);

        Assert.assertEquals("错误，高级查询返回数据与期望结果有差异", expectData, responseData);

    }


    /**
     * 测试新增文档:Post请求/document/create
     * 测试修改文档:Post请求/document/modify
     *
     * @throws Exception
     */
    @Test
    public void testSave() throws Exception {
        /**
         * 测试新增文档
         */

        /**
         *  用例1:全部参数使用合法中间值
         *
         *  用例2:name采用合法边界值man="白"
         *
         *  用例3:name采用合法边界值man="中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国"
         *
         *  用例4:name采用非法等价类：空值；
         *
         *  用例5:name采用非法边界值Max+:name="中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度";
         *
         *  用例6:name同项目下唯一性逻辑校验：name=“百度”(采用SetUp()中相同的值)；
         *
         *
         *
         *
         *
         *
         *
         *
         */

        /**---------------------测试用例赋值开始---------------------**/
        //全部参数使用合法中间值
        Document document = new Document();
        document.setCategoryId(1L);
        document.setDocumentId(2L);
        document.setName("微信");
        document.setDocType((short)2);
        document.setUrl("https://www.baidu");
        document.setMemo("是一款聊天软件");
        document.setAccessory("创始人是张小龙");

        Long operator = 1L;
        id++;
        /**---------------------测试用例赋值结束---------------------**/

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/document/create")
                        .param("categoryId",document.getCategoryId().toString())
                        .param("name",document.getName())
                        .param("docType",document.getDocType().toString())
                        .param("url",document.getUrl())
                        .param("memo",document.getMemo())
                        .param("accessory",document.getAccessory())
                        .param("operator",operator.toString())
        )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"document"
                .andExpect(content().string(containsString("document")))
                // 检查返回的数据节点
                .andExpect(jsonPath("$.document.documentId").value(id))
                .andExpect(jsonPath("$.document.categoryId").value(document.getCategoryId()))
                .andExpect(jsonPath("$.document.name").value(document.getName()))
                .andExpect(jsonPath("$.document.docType").value(document.getDocType().toString()))
                .andExpect(jsonPath("$.document.url").value(document.getUrl()))
                .andExpect(jsonPath("$.document.memo").value(document.getMemo()))
                .andExpect(jsonPath("$.document.accessory").value(document.getAccessory()))
                .andExpect(jsonPath("$.document.creationTime").isNotEmpty())
                .andExpect(jsonPath("$.document.creatorUserId").value(operator))
                .andExpect(jsonPath("$.document.lastModificationTime").isEmpty())
                .andExpect(jsonPath("$.document.lastModifierUserId").value(0))
                .andExpect(jsonPath("$.document.isDeleted").value(false))
                .andExpect(jsonPath("$.document.deletionTime").isEmpty())
                .andExpect(jsonPath("$.document.deleterUserId").value(0))
                .andReturn();


        //用例2:name采用合法边界值man="白"
        /**---------------------测试用例赋值开始---------------------**/
        document.setName("白");
        id++;
        /**---------------------测试用例赋值结束---------------------**/

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/document/create")
                        .param("categoryId",document.getCategoryId().toString())
                        .param("name",document.getName())
                        .param("docType",document.getDocType().toString())
                        .param("url",document.getUrl())
                        .param("memo",document.getMemo())
                        .param("accessory",document.getAccessory())
                        .param("operator",operator.toString())
        )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"document"
                .andExpect(content().string(containsString("document")))
                // 检查返回的数据节点
                .andExpect(jsonPath("$.document.documentId").value(id))
                .andExpect(jsonPath("$.document.categoryId").value(document.getCategoryId()))
                .andExpect(jsonPath("$.document.name").value(document.getName()))
                .andExpect(jsonPath("$.document.docType").value(document.getDocType().toString()))
                .andExpect(jsonPath("$.document.url").value(document.getUrl()))
                .andExpect(jsonPath("$.document.memo").value(document.getMemo()))
                .andExpect(jsonPath("$.document.accessory").value(document.getAccessory()))
                .andExpect(jsonPath("$.document.creationTime").isNotEmpty())
                .andExpect(jsonPath("$.document.creatorUserId").value(operator))
                .andExpect(jsonPath("$.document.lastModificationTime").isEmpty())
                .andExpect(jsonPath("$.document.lastModifierUserId").value(0))
                .andExpect(jsonPath("$.document.isDeleted").value(false))
                .andExpect(jsonPath("$.document.deletionTime").isEmpty())
                .andExpect(jsonPath("$.document.deleterUserId").value(0))
                .andReturn();


        //用例3:name采用合法边界值man="中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国"
        /**---------------------测试用例赋值开始---------------------**/
        document.setName("中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国百度中国");
        id++;
        /**---------------------测试用例赋值结束---------------------**/

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/document/create")
                        .param("categoryId",document.getCategoryId().toString())
                        .param("name",document.getName())
                        .param("docType",document.getDocType().toString())
                        .param("url",document.getUrl())
                        .param("memo",document.getMemo())
                        .param("accessory",document.getAccessory())
                        .param("operator",operator.toString())
        )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"document"
                .andExpect(content().string(containsString("document")))
                // 检查返回的数据节点
                .andExpect(jsonPath("$.document.documentId").value(id))
                .andExpect(jsonPath("$.document.categoryId").value(document.getCategoryId()))
                .andExpect(jsonPath("$.document.name").value(document.getName()))
                .andExpect(jsonPath("$.document.docType").value(document.getDocType().toString()))
                .andExpect(jsonPath("$.document.url").value(document.getUrl()))
                .andExpect(jsonPath("$.document.memo").value(document.getMemo()))
                .andExpect(jsonPath("$.document.accessory").value(document.getAccessory()))
                .andExpect(jsonPath("$.document.creationTime").isNotEmpty())
                .andExpect(jsonPath("$.document.creatorUserId").value(operator))
                .andExpect(jsonPath("$.document.lastModificationTime").isEmpty())
                .andExpect(jsonPath("$.document.lastModifierUserId").value(0))
                .andExpect(jsonPath("$.document.isDeleted").value(false))
                .andExpect(jsonPath("$.document.deletionTime").isEmpty())
                .andExpect(jsonPath("$.document.deleterUserId").value(0))
                .andReturn();


        //用例4:name采用非法等价类：空值；
        /**---------------------测试用例赋值开始---------------------**/
        document.setName("");
        /**---------------------测试用例赋值结束---------------------**/

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/document/create")
                        .param("categoryId",document.getCategoryId().toString())
                        .param("name",document.getName())
                        .param("docType",document.getDocType().toString())
                        .param("url",document.getUrl())
                        .param("memo",document.getMemo())
                        .param("accessory",document.getAccessory())
                        .param("operator",operator.toString())
        )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"formErrors"
                .andExpect(content().string(containsString("formErrors")))
                // 检查返回的数据节点
                .andExpect(content().string(containsString("\"code\" : \"Length\"")))
                .andReturn();


        //用例5:name采用非法边界值Max+:name="感恩这份刻骨的爱感谢这段刻骨的情它给人带来的不仅仅是幸福偶尔的小心痛也是它的精美礼物因为那是它在替你装点了完美的人生没有心痛的人生怎么能称谓完美的人生而那句痛并快乐着就是最好的诠释携手一生相伴到老幸福在于把一个人记住";
        /**---------------------测试用例赋值开始---------------------**/
        document.setName("感恩这份刻骨的爱感谢这段刻骨的情它给人带来的不仅仅是幸福偶尔的小心痛也是它的精美礼物因为那是它在替你装点了完美的人生没有心痛的人生怎么能称谓完美的人生而那句痛并快乐着就是最好的诠释携手一生相伴到老幸福在于把一个人记住");
        /**---------------------测试用例赋值结束---------------------**/
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/document/create")
                        .param("categoryId",document.getCategoryId().toString())
                        .param("name",document.getName())
                        .param("docType",document.getDocType().toString())
                        .param("url",document.getUrl())
                        .param("memo",document.getMemo())
                        .param("accessory",document.getAccessory())
                        .param("operator",operator.toString())
        )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"formErrors"
                .andExpect(content().string(containsString("formErrors")))
                // 检查返回的数据节点
                .andExpect(content().string(containsString("\"code\" : \"Length\"")))
                .andReturn();


        document.setName("百度");
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/document/create")
                        .param("categoryId",document.getCategoryId().toString())
                        .param("name",document.getName())
                        .param("docType",document.getDocType().toString())
                        .param("url",document.getUrl())
                        .param("memo",document.getMemo())
                        .param("accessory",document.getAccessory())
                        .param("operator",operator.toString())
        )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"errorMessage"
                .andExpect(content().string(containsString("\"errorMessage\" : \"[10002]")))
                .andReturn();






        /**
         * 测试修改文档
         */

        /**
         *  修改用例1:全部参数使用合法中间值
         *
         *  修改用例2:name采用合法边界值
         *
         *  修改用例3:name采用合法边界值
         *
         *  修改用例4:name采用合法边界值
         *
         *  修改用例5:name采用合法边界值
         *
         *  修改用例6:name采用非法等价类：空值，
         *
         *  修改用例7:name采用非法边界值
         *
         *  修改用例8:name同项目下唯一性逻辑校验
         *
         *
         */

        /**---------------------测试用例赋值开始---------------------**/
        document = new Document();
        document.setDocumentId(id);
        document.setCategoryId(1L);
        document.setName("111");
        document.setDocType((short)1);
        document.setUrl("http://www.asd.com");
        document.setMemo("aaaaaaaa");
        document.setAccessory("123456");

        Long operator2 = 1L;
        /**---------------------测试用例赋值结束---------------------**/

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/document/modify")
                        .param("documentId", id.toString())
                        .param("categoryId", document.getCategoryId().toString())
                        .param("name", document.getName())
                        .param("docType", document.getDocType().toString())
                        .param("url", document.getUrl())
                        .param("memo", document.getMemo())
                        .param("accessory", document.getAccessory())
                        .param("operator", operator2.toString())
        )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"document"
                .andExpect(content().string(containsString("document")))
                // 检查返回的数据节点
                .andExpect(jsonPath("$.document.documentId").value(id))
                .andExpect(jsonPath("$.document.categoryId").value(document.getCategoryId()))
                .andExpect(jsonPath("$.document.name").value(document.getName()))
                .andExpect(jsonPath("$.document.docType").value(document.getDocType().toString()))
                .andExpect(jsonPath("$.document.url").value(document.getUrl()))
                .andExpect(jsonPath("$.document.memo").value(document.getMemo()))
                .andExpect(jsonPath("$.document.accessory").value(document.getAccessory()))
                .andExpect(jsonPath("$.document.creationTime").isNotEmpty())
                .andExpect(jsonPath("$.document.creatorUserId").value(operator))
                .andExpect(jsonPath("$.document.lastModificationTime").isNotEmpty())
                .andExpect(jsonPath("$.document.lastModifierUserId").value(operator2))
                .andExpect(jsonPath("$.document.isDeleted").value(false))
                .andExpect(jsonPath("$.document.deletionTime").isEmpty())
                .andExpect(jsonPath("$.document.deleterUserId").value(0))
                .andReturn();
    }


    /**
     * 测试查询详情
     *
     * @throws Exception
     */
    @Test
    public void testView() throws Exception {

        //定义的全局id,直接用全局id获取
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/document/view/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"document"
                .andExpect(content().string(containsString("document")))
                // 检查返回的数据节点
                .andExpect(jsonPath("$.document.documentId").value(id))
                .andExpect(jsonPath("$.document.categoryId").value(d1.getCategoryId()))
                .andExpect(jsonPath("$.document.name").value(d1.getName()))
                .andExpect(jsonPath("$.document.docType").value(d1.getDocType().toString()))
                .andExpect(jsonPath("$.document.url").value(d1.getUrl()))
                .andExpect(jsonPath("$.document.memo").value(d1.getMemo()))
                .andExpect(jsonPath("$.document.accessory").value(d1.getAccessory()))
                .andExpect(jsonPath("$.document.creationTime").value(d1.getCreationTime()))
                .andExpect(jsonPath("$.document.creatorUserId").value(d1.getCreatorUserId()))
                .andExpect(jsonPath("$.document.lastModificationTime").value(d1.getLastModificationTime()))
                .andExpect(jsonPath("$.document.lastModifierUserId").value(d1.getLastModifierUserId()))
                .andExpect(jsonPath("$.document.isDeleted").value(d1.getIsDeleted()))
                .andExpect(jsonPath("$.document.deletionTime").value(d1.getDeletionTime()))
                .andExpect(jsonPath("$.document.deleterUserId").value(d1.getDeleterUserId()))
                .andReturn();
    }


    /**
     * 测试删除
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {

        //定义的全局id,直接用全局id获取
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/document/delete/{id}", id)
                        .param("operator", "2")
                        .accept(MediaType.APPLICATION_JSON)
        )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查返回的数据节点
                .andExpect(jsonPath("$.result").value("success"))
                .andReturn();

        // 验证数据库是否已经删除
        Document document = documentRepository.findOne(id);
        Assert.assertNotNull(document);
        Assert.assertEquals("错误，正确结果应该是true", true, document.getIsDeleted());
    }

}
