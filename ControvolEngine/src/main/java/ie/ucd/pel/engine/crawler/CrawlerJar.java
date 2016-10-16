package ie.ucd.pel.engine.crawler;

import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.googlecode.objectify.annotation.AlsoLoad;

public class CrawlerJar implements ICrawler {
	
	public CrawlerJar(){}

	public MApplication getApplication(String pathToJar){
		MApplication app = new MApplication(pathToJar);
		try {
			JarFile jarFile = new JarFile(pathToJar);
			Enumeration<JarEntry> e = jarFile.entries();

			URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
			ClassLoader cl = URLClassLoader.newInstance(urls);
			
			while (e.hasMoreElements()) {
				JarEntry je = (JarEntry) e.nextElement();
				if(je.isDirectory() || !je.getName().endsWith(".class")){
					continue;
				}
				// -6 because of ".class"
				String className = je.getName().substring(0,je.getName().length()-6);
				className = className.replace('/', '.');
				//System.out.println("> "+className);
				try {
					Class<?> clas = cl.loadClass(className);
					MEntity entity = this.crawlClass(clas);
					if (entity != null){
						app.addEntity(entity);
					} 
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			jarFile.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return app;
	}

	private MEntity crawlClass(Class<?> clas){
		MEntity classDesc = null;
		Boolean isEntity = clas.isAnnotationPresent(com.googlecode.objectify.annotation.Entity.class);
		Boolean isAbstract = Modifier.isAbstract(clas.getModifiers());
		if (isEntity && !isAbstract){
			classDesc = new MEntity(clas.getName(), "");
			Field[] fields = clas.getDeclaredFields();
			for (int i = 0 ; i < fields.length ; i++){
				Field field = fields[i];
				Boolean isIgnored = field.isAnnotationPresent(com.googlecode.objectify.annotation.Ignore.class);
				if (!isIgnored){
					MAttribute attDesc = new MAttribute(field.getName(), field.getType().toString(), classDesc.getLocation() + classDesc.getEntityName());  
					if (field.isAnnotationPresent(AlsoLoad.class)){
						AlsoLoad al = field.getAnnotation(com.googlecode.objectify.annotation.AlsoLoad.class);
						String[] values = al.value();
						for (int j = 0 ; j < values.length ; j++){
							attDesc.addFormerName(values[j]);
						}
					}
					classDesc.addAttribute(attDesc);
				}
			}

		}
		return classDesc;
	}

}
