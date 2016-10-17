package com.techfocus.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.techfocus.util.DirectoryWatcher;
import com.techfocus.util.ResourceLoader;

/**
 * 资源变化自动检测
 * @author jack
 */
public class AutoDetector {
    private static final Logger LOGGER = Logger.getLogger(AutoDetector.class);
    //已经被监控的文件
    private static final Set<String> FILE_WATCHERS = new HashSet<>();
    private static final Map<DirectoryWatcher, String> RESOURCES = new HashMap<>();
    private static final Map<DirectoryWatcher, ResourceLoader> RESOURCE_LOADERS = new HashMap<>();
    private static final Map<DirectoryWatcher.WatcherCallback, DirectoryWatcher> WATCHER_CALLBACKS = new HashMap<>();
    
    /**
     * 加载资源并自动检测资源变化
     * 当资源发生变化的时候重新自动加载
     * @param resourceLoader 资源加载逻辑
     * @param resourcePaths 多个资源路径，用逗号分隔
     */
    public static void loadAndWatch(ResourceLoader resourceLoader, String resourcePaths) {
        resourcePaths = resourcePaths.trim();
        if("".equals(resourcePaths)){
            LOGGER.info("没有资源可以加载");
            return;
        }
        LOGGER.info("开始加载资源");
        LOGGER.info(resourcePaths);
        long start = System.currentTimeMillis();
        List<String> result = new ArrayList<>();
        for(String resource : resourcePaths.split("[,，]")){
            try{
                resource = resource.trim();
                result.addAll(loadResource(resource, resourceLoader, resourcePaths));
            }catch(Exception e){
                LOGGER.error("加载资源失败："+resource, e);
            }
        }
        LOGGER.info("加载资源 "+result.size()+" 行");
        //调用自定义加载逻辑
        resourceLoader.clear();
        resourceLoader.load(result);        
        long cost = System.currentTimeMillis() - start;
        LOGGER.info("完成加载资源，耗时"+cost+" 毫秒");
    }
        /**
     * 加载资源
     * @param resource 资源路径
     * @param resourceLoader 资源自定义加载逻辑
     * @param resourcePaths 资源的所有路径，用于资源监控
     * @return 资源内容
     * @throws IOException 
     */
    private static List<String> loadResource(String resource, ResourceLoader resourceLoader, String resourcePaths) throws IOException {
        List<String> result = new ArrayList<>();
        Path path = Paths.get(resource);
        boolean exist = Files.exists(path);
        if(!exist){
            LOGGER.error("资源不存在："+resource);
            return result;
        }
        boolean isDir = Files.isDirectory(path);
        if(isDir){
            //处理目录
            result.addAll(loadAndWatchDir(path, resourceLoader, resourcePaths));
        }else{
            //处理文件
        	List<String> watchPaths = load(resource);
        	for(String watchPath:watchPaths){
        		loadAndWatchDir(Paths.get(watchPath), resourceLoader, resourcePaths);
//        		//发现新的监控目录
//        		if(resourcePaths.indexOf(watchPath) == -1){
//        			result.add(watchPath);               //记录发现新的监控目录
//        			resourcePaths += "," + watchPath;    //发现的新监控目录加入资源路径
//        			loadAndWatchDir(Paths.get(watchPath), resourceLoader, resourcePaths);
//        		}
        	}
//            result.addAll(load(resource));
            //监控文件
            watchFile(path.toFile(), resourceLoader, resourcePaths);
        }
        return result;//返回的结果只是用于打印监控了哪些文件、目录
    }
    /**
     * 递归加载目录下面的所有资源
     * 并监控目录变化
     * @param path 目录路径
     * @param resourceLoader 资源自定义加载逻辑
     * @param resourcePaths 资源的所有路径，用于资源监控
     * @return 目录所有资源内容
     */
    private static List<String> loadAndWatchDir(Path path, ResourceLoader resourceLoader, String resourcePaths) {
        final List<String> result = new ArrayList<>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    result.addAll(load(file.toAbsolutePath().toString()));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            LOGGER.error("加载资源失败："+path, ex);
        }
        
        if(FILE_WATCHERS.contains(path.toString())){
            //之前已经注册过监控服务，此次忽略
            return result;
        }
        FILE_WATCHERS.add(path.toString());
        DirectoryWatcher.WatcherCallback watcherCallback = new DirectoryWatcher.WatcherCallback(){

            private long lastExecute = System.currentTimeMillis();
            @Override
            public void execute(WatchEvent.Kind<?> kind, String path) {
                //一秒内发生的多个相同事件认定为一次，防止短时间内多次加载资源
                if(System.currentTimeMillis() - lastExecute > 1000){
                    lastExecute = System.currentTimeMillis();
                    if(path.endsWith(".apk") 
                    		&& "ENTRY_MODIFY".equals(kind.name())){
                    	//监测到有变化的 APK文件，执行测试
                    	LOGGER.info("-------------------auto test.."+ path+ ".......");
                    	try {
							Runtime.getRuntime().exec("sh -c java -jar Main.jar android standard.xml " + path);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	LOGGER.info("事件："+kind.name()+" ,路径："+path);
                    }
                    synchronized(AutoDetector.class){
                        DirectoryWatcher dw = WATCHER_CALLBACKS.get(this);
                        String paths = RESOURCES.get(dw);
                        ResourceLoader loader = RESOURCE_LOADERS.get(dw);
                        LOGGER.info("重新加载数据");
                        loadAndWatch(loader, paths);
                    }
                }
            }

        };
        DirectoryWatcher directoryWatcher = DirectoryWatcher.getDirectoryWatcher(watcherCallback,
                StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        directoryWatcher.watchDirectoryTree(path);
        
        WATCHER_CALLBACKS.put(watcherCallback, directoryWatcher);
        RESOURCES.put(directoryWatcher, resourcePaths);
        RESOURCE_LOADERS.put(directoryWatcher, resourceLoader);
        
        return result;
    }
    /**
     * 加载文件资源
     * @param path 文件路径
     * @return 文件内容
     */
    private static List<String> load(String path) {
        List<String> result = new ArrayList<>();
        try{
            InputStream in = null;
            LOGGER.info("加载资源："+path);
            if(path.startsWith("classpath:")){
                in = AutoDetector.class.getClassLoader().getResourceAsStream(path.replace("classpath:", ""));
            }else{
                in = new FileInputStream(path);
            }        
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"))){
                String line;
                while((line = reader.readLine()) != null){
                    line = line.trim();
                    if("".equals(line) || line.startsWith("#")){
                        continue;
                    }
                    result.add(line);
                }
            }
        }catch(Exception e){
            LOGGER.error("加载资源失败："+path, e);
        }
        return result;
    }
    /**
     * 监控文件变化
     * @param file 文件
     */
    private static void watchFile(final File file, ResourceLoader resourceLoader, String resourcePaths) {
        if(FILE_WATCHERS.contains(file.toString())){
            //之前已经注册过监控服务，此次忽略
            return;
        }
        FILE_WATCHERS.add(file.toString());
        LOGGER.info("监控文件："+file.toString());
        DirectoryWatcher.WatcherCallback watcherCallback = new DirectoryWatcher.WatcherCallback(){
            private long lastExecute = System.currentTimeMillis();
            @Override
            public void execute(WatchEvent.Kind<?> kind, String path) {
                if(System.currentTimeMillis() - lastExecute > 1000){
                    lastExecute = System.currentTimeMillis();
                    if(!path.equals(file.toString())){
                        return;
                    }
                    LOGGER.info("事件："+kind.name()+" ,路径："+path);
                    synchronized(AutoDetector.class){//处理完后再次监控
                        DirectoryWatcher dw = WATCHER_CALLBACKS.get(this);
                        String paths = RESOURCES.get(dw);
                        ResourceLoader loader = RESOURCE_LOADERS.get(dw);
                        LOGGER.info("重新加载数据");
                        loadAndWatch(loader, paths);
                    }
                }
            }

        };
        DirectoryWatcher fileWatcher = DirectoryWatcher.getDirectoryWatcher(watcherCallback, 
                StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
        fileWatcher.watchDirectory(file.getParent());        
        WATCHER_CALLBACKS.put(watcherCallback, fileWatcher);
        RESOURCES.put(fileWatcher, resourcePaths);
        RESOURCE_LOADERS.put(fileWatcher, resourceLoader);
    }
    public static void main(String[] args){
    	PropertyConfigurator.configure("config/log4j.properties");
        AutoDetector.loadAndWatch(new ResourceLoader(){

            @Override
            public void clear() {
                System.out.println("清空资源");
            }

            @Override
            public void load(List<String> lines) {
                for(String line : lines){
                    System.out.println(line);
                }
            }

            @Override
            public void add(String line) {
                System.out.println("add："+line);
            }

            @Override
            public void remove(String line) {
                System.out.println("remove："+line);
            }
        }, "watchDir.txt");
    }
}