## 运行：  
    gradle bootRun

## 打包：
    gradle bootRepackage：生成可执行jar包，在 target/libs中。
    可直接通过java -jar 运行。

## IDE支持 eclipse 和 Idea：
### eclipse:
    gradle eclipse : 生成eclipse工程文件
    然后就可以将工程导入到eclipse。如果项目的依赖包改变，可能需要重新生成一次。
### idea：
    gradle idea

