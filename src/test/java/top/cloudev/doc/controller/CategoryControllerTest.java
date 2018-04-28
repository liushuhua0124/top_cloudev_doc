package top.cloudev.doc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import top.cloudev.doc.domain.Category;
import top.cloudev.doc.dto.CategoryDTO;
import top.cloudev.doc.dao.CategoryRepository;
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
 * 领域类 Category(文档分类) 的单元测试代码
 * Created by Mac.Manon on 2018/04/04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional// 使用@Transactional注解，确保每次测试后的数据将会被回滚
public class CategoryControllerTest {
    @Autowired
    CategoryRepository categoryRepository;

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    // 默认初始化的一条文档分类数据
    private Category c1;

    // 期望返回的数据
    private String expectData;

    // 实际调用返回的结果
    private String responseData;

    // 列表查询传参
    private CategoryDTO dto;

    // 期望获得的结果数量
    private Long expectResultCount;

    // 使用JUnit的@Before注解可在测试开始前进行一些初始化的工作
    @Before
    public void setUp() throws JsonProcessingException {
        /**---------------------测试用例赋值开始---------------------**/
        c1 = new Category();
        c1.setProjectId(1L);
        c1.setName("文档分类一");
        c1.setSequence(1);
        c1.setCreatorUserId(1);
        categoryRepository.save(c1);
        /**---------------------测试用例赋值结束---------------------**/

        // 获取mockMvc对象实例
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    /**
     * 测试列表
     * @throws Exception
     */
    @Test
    public void testList() throws Exception {

        // 添加分类：用例2(分类名称与装配数据中的分类名称有部分关键字相同)
        /**---------------------测试用例赋值开始---------------------**/
        Category c2 = new Category();
        c2.setProjectId(1L);
        c2.setName("文档分类二");
        c2.setSequence(2);
        c2.setCreatorUserId(2);
        categoryRepository.save(c2);
        /**---------------------测试用例赋值结束---------------------**/

        // 添加分类：用例3(分类名称与用例1和用例2完全不同)
        /**---------------------测试用例赋值开始---------------------**/
        Category c3 = new Category();
        c3.setProjectId(1L);
        c3.setName("项目资料归档");
        c3.setSequence(3);
        c3.setCreatorUserId(2);
        categoryRepository.save(c3);
        /**---------------------测试用例赋值结束---------------------**/

        // 添加分类：用例4(名称与用例1一样，但是所属项目不同)
        /**---------------------测试用例赋值开始---------------------**/
        Category c4 = new Category();
        c4.setProjectId(2L);
        c4.setName("文档分类一");
        c4.setSequence(1);
        c4.setCreatorUserId(2);
        categoryRepository.save(c4);
        /**---------------------测试用例赋值结束---------------------**/

        // 修改分类：用例5
        /**---------------------测试用例赋值开始---------------------**/
        Category c5 = new Category();
        c5.setProjectId(1L);
        c5.setName("被修改过的文档分类");
        c5.setSequence(4);
        c5.setCreatorUserId(2);
        c5.setLastModificationTime(new Date());
        c5.setLastModifierUserId(1);
        categoryRepository.save(c5);
        /**---------------------测试用例赋值结束---------------------**/

        // 删除分类：用例6
        /**---------------------测试用例赋值开始---------------------**/
        Category c6 = new Category();
        c6.setProjectId(1L);
        c6.setName("被删除过的文档分类");
        c6.setSequence(5);
        c6.setCreatorUserId(2);
        c6.setLastModificationTime(new Date());
        c6.setLastModifierUserId(1);
        c6.setIsDeleted(true);
        c6.setDeletionTime(new Date());
        c6.setDeleterUserId(1);
        categoryRepository.save(c6);
        /**---------------------测试用例赋值结束---------------------**/


        /**
         * 测试无搜索列表
         */

        /**---------------------测试用例赋值开始---------------------**/
        //TODO 将下面的null值换为测试参数
        Pageable pageable=new PageRequest(0,10, Sort.Direction.ASC,"sequence");
        // 期望获得的结果数量(默认有两个测试用例，所以值应为"2L"，如果新增了更多测试用例，请相应设定这个值)
        expectResultCount = 4L;
        /**---------------------测试用例赋值结束---------------------**/

        // 直接通过dao层接口方法获得期望的数据
        Page<Category> pagedata = categoryRepository.findByProjectIdAndIsDeletedFalse(c1.getProjectId(), pageable);
        expectData = JsonPath.read(Obj2Json(pagedata),"$").toString();

        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/category/list?projectId=1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查返回的数据节点
                .andExpect(jsonPath("$.pagedata.totalElements").value(expectResultCount))
                .andExpect(jsonPath("$.dto.keyword").isEmpty())
                .andExpect(jsonPath("$.dto.projectId").value(1))
                .andReturn();

        // 提取返回结果中的列表数据及翻页信息
        responseData = JsonPath.read(mvcResult.getResponse().getContentAsString(),"$.pagedata").toString();

        System.out.println("=============无搜索列表期望结果：" + expectData);
        System.out.println("=============无搜索列表实际返回：" + responseData);

        Assert.assertEquals("错误，无搜索列表返回数据与期望结果有差异",expectData,responseData);




        /**
         * 测试标准查询
         */

        /**---------------------测试用例赋值开始---------------------**/
        //TODO 将下面的null值换为测试参数
        dto = new CategoryDTO();
        dto.setKeyword("文档分类");
        dto.setProjectId(c1.getProjectId());

        pageable=new PageRequest(0,10, Sort.Direction.ASC,"sequence");

        // 期望获得的结果数量
        expectResultCount = 3L;
        /**---------------------测试用例赋值结束---------------------**/

        String keyword = dto.getKeyword().trim();

        // 直接通过dao层接口方法获得期望的数据
        pagedata = categoryRepository.findByNameContainingAllIgnoringCaseAndProjectIdAndIsDeletedFalse(keyword, c1.getProjectId(), pageable);
        expectData = JsonPath.read(Obj2Json(pagedata),"$").toString();

        mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.get("/category/list?projectId=1")
                                .param("keyword",dto.getKeyword())
                                .accept(MediaType.APPLICATION_JSON)
                )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查返回的数据节点
                .andExpect(jsonPath("$.pagedata.totalElements").value(expectResultCount))
                .andExpect(jsonPath("$.dto.keyword").value(dto.getKeyword()))
                .andExpect(jsonPath("$.dto.projectId").value(1))
                .andReturn();

        // 提取返回结果中的列表数据及翻页信息
        responseData = JsonPath.read(mvcResult.getResponse().getContentAsString(),"$.pagedata").toString();

        System.out.println("=============标准查询期望结果：" + expectData);
        System.out.println("=============标准查询实际返回：" + responseData);

        Assert.assertEquals("错误，标准查询返回数据与期望结果有差异",expectData,responseData);




    }


    /**
     * 测试新增文档分类:Post请求/category/create
     * 测试修改文档分类:Post请求/category/modify
     * @throws Exception
     */
    @Test
    public void testSave() throws Exception {
        /**
         * 测试新增文档分类
         */

        /**
         *  列出新增文档分类测试用例清单
         *
         *
         *   用例1:全部参数使用合法中间值
         *   ProjectId=1L；
         *   name="测试新增文档分类一";
         *   sequence="10";
         *   operator="1L";
         *
         *   用例2:name采用合法边界值Min：name="测";
         *   (其它参数沿用用例1的合法中间值)
         *
         *   用例3:name采用合法边界值Min+:name="测试";
         *
         *   用例4:name采用合法边界值Max:name="测试新增文档分类测试新增文档分类测试新增文档分类测试新增文档分类测试新增文档分类测试新增文档分类测试";
         *
         *   用例5:name采用合法边界值Max:name="测试新增文档分类测试新增文档分类测试新增文档分类测试新增文档分类测试新增文档分类测试新增文档分类测";
         *
         *   用例6:name采用非法等价类：空值；
         *
         *   用例7:name采用非法边界值Max+:name="测试新增文档分类测试新增文档分类测试新增文档分类测试新增文档分类测试新增文档分类测试新增文档分类测超长";
         *
         *   用例8:name同项目下唯一性逻辑校验：name=“文档分类一”(采用SetUp()中相同的值)；
         *
         *   用例9:sequence采用合法边界值Min：sequence=1；
         *
         *   用例10:sequence采用合法边界值Min+：sequence=2；
         *
         *   用例11:sequence采用合法边界值Max：sequence=Integer.MAX_VALUE；
         *
         *   用例12:sequence采用合法边界值Max-：sequence=Integer.MAX_VALUE-1；
         *
         *   用例13:sequence采用非法等价类：空值；
         *
         *   用例14:sequence采用非法边界值Min-：sequence=0；
         *
         *   用例15:sequence采用非法边界值：sequence=-1；
         *
         *   用例16:sequence采用非法边界值Max+：sequence=Integer.MAX_VALUE+1；
         *
         *   用例17:sequence采用非法等价类：abc(字符)；
         *
         */

         //TODO 列出新增文档分类测试用例清单

        /**---------------------测试用例赋值开始---------------------**/
        //TODO 将下面的null值换为测试参数
        Category category = new Category();
        category.setProjectId(null);
        category.setName(null);
        category.setSequence(null);

        Long operator = null;
        Long id = 4L;
        /**---------------------测试用例赋值结束---------------------**/

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/category/create")
                                .param("projectId",category.getProjectId().toString())
                                .param("name",category.getName())
                                .param("sequence",category.getSequence().toString())
                                .param("operator",operator.toString())
                )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"category"
                .andExpect(content().string(containsString("category")))
                // 检查返回的数据节点
                .andExpect(jsonPath("$.category.categoryId").value(id))
                .andExpect(jsonPath("$.category.projectId").value(category.getProjectId()))
                .andExpect(jsonPath("$.category.name").value(category.getName()))
                .andExpect(jsonPath("$.category.sequence").value(category.getSequence()))
                .andExpect(jsonPath("$.category.creationTime").isNotEmpty())
                .andExpect(jsonPath("$.category.creatorUserId").value(operator))
                .andExpect(jsonPath("$.category.lastModificationTime").isEmpty())
                .andExpect(jsonPath("$.category.lastModifierUserId").value(0))
                .andExpect(jsonPath("$.category.isDeleted").value(false))
                .andExpect(jsonPath("$.category.deletionTime").isEmpty())
                .andExpect(jsonPath("$.category.deleterUserId").value(0))
                .andReturn();


        /**
         * 测试修改文档分类
         */

         //TODO 列出修改文档分类测试用例清单

        /**---------------------测试用例赋值开始---------------------**/
        //TODO 将下面的null值换为测试参数
        category = new Category();
        category.setCategoryId(id);
        category.setProjectId(null);
        category.setName(null);
        category.setSequence(null);

        Long operator2 = null;
        /**---------------------测试用例赋值结束---------------------**/

        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/category/modify")
                        .param("categoryId",id.toString())
                        .param("projectId",category.getProjectId().toString())
                        .param("name",category.getName())
                        .param("sequence",category.getSequence().toString())
                        .param("operator",operator2.toString())
                )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"category"
                .andExpect(content().string(containsString("category")))
                // 检查返回的数据节点
                .andExpect(jsonPath("$.category.categoryId").value(id))
                .andExpect(jsonPath("$.category.projectId").value(category.getProjectId()))
                .andExpect(jsonPath("$.category.name").value(category.getName()))
                .andExpect(jsonPath("$.category.sequence").value(category.getSequence()))
                .andExpect(jsonPath("$.category.creationTime").isNotEmpty())
                .andExpect(jsonPath("$.category.creatorUserId").value(operator))
                .andExpect(jsonPath("$.category.lastModificationTime").isNotEmpty())
                .andExpect(jsonPath("$.category.lastModifierUserId").value(operator2))
                .andExpect(jsonPath("$.category.isDeleted").value(false))
                .andExpect(jsonPath("$.category.deletionTime").isEmpty())
                .andExpect(jsonPath("$.category.deleterUserId").value(0))
                .andReturn();
    }


    /**
     * 测试查询详情
     * @throws Exception
     */
    @Test
    public void testView() throws Exception
    {
        //TODO 下面id的值由testView方法执行时总共由testList、testSave和testView方法执行几次插入数据表决定当前的主键ID值
        Long id = 5L;

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/category/view/{id}",id)
                                .accept(MediaType.APPLICATION_JSON)
                )
                // 打印结果
                .andDo(print())
                // 检查状态码为200
                .andExpect(status().isOk())
                // 检查内容有"category"
                .andExpect(content().string(containsString("category")))
                // 检查返回的数据节点
                .andExpect(jsonPath("$.category.categoryId").value(id))
                .andExpect(jsonPath("$.category.projectId").value(c1.getProjectId()))
                .andExpect(jsonPath("$.category.name").value(c1.getName()))
                .andExpect(jsonPath("$.category.sequence").value(c1.getSequence()))
                .andExpect(jsonPath("$.category.creationTime").value(c1.getCreationTime()))
                .andExpect(jsonPath("$.category.creatorUserId").value(c1.getCreatorUserId()))
                .andExpect(jsonPath("$.category.lastModificationTime").value(c1.getLastModificationTime()))
                .andExpect(jsonPath("$.category.lastModifierUserId").value(c1.getLastModifierUserId()))
                .andExpect(jsonPath("$.category.isDeleted").value(c1.getIsDeleted()))
                .andExpect(jsonPath("$.category.deletionTime").value(c1.getDeletionTime()))
                .andExpect(jsonPath("$.category.deleterUserId").value(c1.getDeleterUserId()))
                .andReturn();
    }


    /**
     * 测试删除
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception
    {
        //TODO 下面id的值由testView方法执行时总共由testList、testSave、testView和testDelete方法执行几次插入数据表决定当前的主键ID值
        Long id = 6L;

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/category/delete/{id}",id)
                                .param("operator","2")
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
        Category category = categoryRepository.findOne(id);
        Assert.assertNotNull(category);
        Assert.assertEquals("错误，正确结果应该是true",true,category.getIsDeleted());
    }

}
