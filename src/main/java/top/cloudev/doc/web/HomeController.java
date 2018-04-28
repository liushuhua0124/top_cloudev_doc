package top.cloudev.doc.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  我写的第一个REST控制器接口,仅用于演示
 *  Created by Mac.Manon on 2018/04/28
 */

@RestController
public class HomeController {

    /**
     * hello Spring cloud
     * GET /hello
     * @return
     */
    @GetMapping("/hello")
    public String hello(){
        return "hello Spring cloud!";
    }
}
