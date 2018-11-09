chapter10:
        jibx 用法：
        1. 使用 Ant 编译，创建build.xml，定位到下载的jibx包路径，进行编译
            运行配置：before launch 配置 ant 编译
        2. 使用命令生成：java -cp bin;D:\tools\jibx\lib\jibx-tools.jar org.jibx.binding.generator.BindGen -b binding.xml com.gmr.netty.chapter10.httpxml.pojo.Order
               运行命令：java -cp .;D:\dev\jibx\lib\jibx-run.jar com.gmr.netty.chapter10.httpxml.pojo.OrderTest
        3. 编辑类文件，利用jar中的函数，进行编译，生成对应的 binding.xml 文件
               这种情况，直接运行即可，因为编译过了