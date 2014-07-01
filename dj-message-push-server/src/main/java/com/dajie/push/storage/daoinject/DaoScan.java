package com.dajie.push.storage.daoinject;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wills on 3/11/14.
 */
public class DaoScan {

    public static <A extends Annotation> List<Class> getClasses(List<String> packages,Class<A> annotationType){
        List<Class> classes=new ArrayList<Class>();
        if(packages==null||packages.size()==0){
            return classes;
        }
        for(String p:packages){
            classes.addAll(findClasses(p,annotationType));
        }
        return classes;
    }

    private static boolean isMatch(MetadataReader metadataReader,Class annotationType)throws ClassNotFoundException{
        Class c=Class.forName(metadataReader.getClassMetadata().getClassName());
        if(c.getAnnotation(annotationType)!=null){
            return true;
        }
        return false;
    }

    public static <A extends Annotation> List<Class> findClasses(String packageName,Class<A> annotationType){
        List<Class> classes=new ArrayList<Class>();

        ResourcePatternResolver resourcePatternResolver=new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory=new CachingMetadataReaderFactory(resourcePatternResolver);

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(packageName))
                + "/**/*DAO.class";
        try{
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            for(Resource resource:resources){
                if(resource.isReadable()){
                    MetadataReader metadataReader=metadataReaderFactory.getMetadataReader(resource);
                    if(isMatch(metadataReader,annotationType)){
                       classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                    }

                }
            }

        }catch(Exception e){

        }
        return classes;
    }
}
