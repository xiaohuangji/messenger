package com.dajie.message.util.scan;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

public class InterfaceScan{
	public static<A extends Annotation> List<Class<?>> getClasses(List<String> packages,Class<A> annotationType){
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for(String p : packages){
			try {
				classes.addAll(findClasses(p,annotationType));
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return classes;
	}
	
	
	private static <A extends Annotation> List<Class<?>> findClasses(String basePackage,Class<A> annotationType) throws IOException,
			ClassNotFoundException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
				resourcePatternResolver);

		List<Class<?>> candidates = new ArrayList<Class<?>>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ resolveBasePackage(basePackage) + "/" + "**/*.class";
		Resource[] resources = resourcePatternResolver
				.getResources(packageSearchPath);
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader metadataReader = metadataReaderFactory
						.getMetadataReader(resource);
				if (isCandidate(metadataReader,annotationType)) {
					candidates.add(Class.forName(metadataReader
							.getClassMetadata().getClassName()));
				}
			}
		}
		return candidates;
	}

	private static String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils
				.resolvePlaceholders(basePackage));
	}

	private static <A extends Annotation> boolean isCandidate(MetadataReader metadataReader,Class<A> annotation)
			throws ClassNotFoundException {
		try {
			 Class<?> c =Class.forName(metadataReader.getClassMetadata().getClassName());
			 if (c.getAnnotation(annotation) != null) {
				  return true;
			 }
			 return false;
		} catch (Exception e) {
		}
		return false;
	}
}
